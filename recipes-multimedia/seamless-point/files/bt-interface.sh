#!/bin/sh

BT_MAC_DEVICES="bluetoothctl devices | grep "Device" | cut -d ' ' -f 2"
BT_DEFAULT_SYMBOL="🔈"
BT_SERVICE_NAME="bt"

TEMPDIR="/tmp"

generate_arbitrary_port() {
    local mac="$1"
    local min_port=1     # Minimum port number
    local max_port=255   # Maximum port number
    local range=$((max_port - min_port + 1))

    _hash="$(echo -n "$mac" | md5sum | cut -d' ' -f1)"
    hash_decimal="$((16#${_hash}))"
    port_number="$((min_port + (hash_decimal % range)))"

    echo "${port_number}"
}

process_mac() {
    local mac="${1}"

    # Convert MAC address to lowercase plus remove colons
    echo "${mac}" | tr '[:upper:]' '[:lower:]' | tr -d ':'
}

stop_airplay() {
    local mac="${1}"
    local alsadev="$(process_mac "${mac}")"

    # Drop running shairport-sync services to start them after connection
    for version in v2 v1; do
        systemctl is-active --quiet shairport-sync-${version}@${alsadev}.service
        if [ ${?} -eq 0 ]; then
            systemctl stop shairport-sync-${version}@${alsadev}.service

            # Drop environment files
            [  -r ${TEMPDIR}/${version}-${alsadev}-extraopts.env ] && \
                rm -f ${TEMPDIR}/${version}-${alsadev}-extraopts.env

            for file in ${TEMPDIR}/shairport-sync-${version}-*-bt.conf; do
                [ -e "${file}" ] && unlink "${file}"
            done
        fi
    done
}

stop_services() {
    local active_macs="$(eval ${BT_MAC_DEVICES})"

    for mac in ${active_macs}; do
        stop_airplay ${mac}
        bluetoothctl -- disconnect ${mac}
    done
}

update_sp_config() {
    local config_file_path="${1}"
    local new_device_id_offset="${2}"

    # Use sed to find and replace the airplay_device_id_offset value
    sed -i -E "s/(airplay_device_id_offset\s*=\s*)[0-9]+;/\1${new_device_id_offset};/" "$config_file_path"
}

process_connected() {
    mac="${1}"
    alsadev="$(process_mac "${mac}")"

    # Fetch device name from Bluetooth attributes
    for version in v1 v2; do
        offset="$(generate_arbitrary_port ${alsadev})"

        if [ ${version} == "v1" ]; then
            port="--port $((5000 + ${offset}))"
        elif [ ${version} == "v2" ]; then
            port="--port $((7000 + ${offset}))"
        else
            port=""
        fi

        # Process routine only in case shairport-sync service is not running
        systemctl is-active --quiet ${SHAIRPORT_SYNC}-${version}@${alsadev}.service
        if [ ${?} -ne 0 ]; then
            ln -fsr /etc/shairport-sync-${version}-bt.conf ${TEMPDIR}/shairport-sync-${version}-${alsadev}-bt.conf
            update_sp_config ${TEMPDIR}/shairport-sync-${version}-${alsadev}-bt.conf ${offset}

            # Update the device name from Bluetooth attributes, otherwise keep default "Bluetooth Speaker" name
            local device_name="$(bluetoothctl info ${mac} | grep Name: | awk -F': ' '{print $2}')"
            if [ -n "${device_name}" ]; then
                local env=${TEMPDIR}/${version}-${alsadev}-extraopts.env
                local devname="${BT_DEFAULT_SYMBOL} ${device_name}"

                # JBL Speaker
                echo "DEVICE_NAME=\"${devname}\"" > ${env}
                # Sound card name: e.g. f85c7de6c3aa
                echo "ALSA_DEVICE=\"${alsadev}\"" >> ${env}
                # 7xxx 5xxx
                echo "PORT=\"${port}\"" >> ${env}
                # v1 v2
                echo "VERSION=\"${version}\"" >> ${env}
                # -c /tmp/shairport-sync shairport-sync-bt.conf 7002 --name JBL Speaker --output alsa -d f85c7de6c3aa
                echo "OPTS=\"-c ${TEMPDIR}/shairport-sync-${version}-${alsadev}-bt.conf ${port} --name '${devname}' --output=alsa -- -d ${alsadev}\"" >> ${env}
            fi

            systemctl start shairport-sync-${version}@${alsadev}.service
        fi
    done
}

start() {
    while true; do
        local active_macs="$(eval "${BT_MAC_DEVICES}")"
        [ -z "${active_macs}" ] && sleep 10 && continue

        for mac in ${active_macs}; do
            if bluetoothctl info ${mac} 2>/dev/null | grep -q 'Connected: yes'; then
                process_connected ${mac}
            else
                stop_airplay ${mac}
            fi
        done
    done
}

stop() {
    stop_services
}

case "${1}" in
    start|stop)
        ${1}
        ;;
    restart)
        stop
        start
        ;;
    *)
        echo "Usage: $0 {start|stop|restart}"
        ;;
esac
