#!/bin/sh

BT_MAC_DEVICES="bluetoothctl devices | grep "Device" | cut -d ' ' -f 2"
BT_DEFAULT_SYMBOL="🔈"
BT_SERVICE_NAME="bt"

SHAIRPORT_SYNC="shairport-sync"
BASEDIR="/tmp"

generate_random_port() {
    local mac_address="$1"
    local min_port=1     # Minimum port number
    local max_port=100   # Maximum port number
    local range=$((max_port - min_port + 1))

    _hash="$(echo -n "$mac_address" | md5sum | cut -d' ' -f1)"
    hash_decimal="$((16#${_hash}))"
    port_number="$((min_port + (hash_decimal % range)))"

    echo "${port_number}"
}

process_mac() {
    local mac="${1}"

    # Convert MAC address to lowercase and remove colons
    local processed_mac
    processed_mac=$(echo "${mac}" | tr '[:upper:]' '[:lower:]' | tr -d ':')

    # Output the processed MAC address
    echo "${processed_mac}"
}

stop_airplay() {
    local mac="${1}"
    local alsadev="$(process_mac "${mac}")"

    # Drop running shairport-sync services to start them after connection
    for version in v2 v1; do
        systemctl is-active --quiet ${SHAIRPORT_SYNC}-${version}@${alsadev}.service
        if [ ${?} -eq 0 ]; then
            systemctl stop ${SHAIRPORT_SYNC}-${version}@${alsadev}.service
            # Drop env files
            [  -r ${BASEDIR}/${version}-${alsadev}-extraopts.env ] && \
                rm -f ${BASEDIR}/${version}-${alsadev}-extraopts.env

            for file in ${BASEDIR}/${SHAIRPORT_SYNC}-${version}-*-bt.conf; do
                if [ -e "${file}" ]; then
                    unlink "${file}"
                fi
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

update_config() {
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
        offset="$(generate_random_port ${alsadev})"

        if [ ${version} == "v1" ]; then
            port="--port $((5000+ ${offset}))"
        elif [ ${version} == "v2" ]; then
            port="--port $((7000 + ${offset}))"
        else
            port=""
        fi

        # Process routine only in case shairport-sync service is not running
        systemctl is-active --quiet ${SHAIRPORT_SYNC}-${version}@${alsadev}.service
        if [ ${?} -ne 0 ]; then
            ln -fsr /etc/${SHAIRPORT_SYNC}-${version}-bt.conf ${BASEDIR}/${SHAIRPORT_SYNC}-${version}-${alsadev}-bt.conf
            update_config ${BASEDIR}/${SHAIRPORT_SYNC}-${version}-${alsadev}-bt.conf ${offset}

            # Update the device name from Bluetooth attributes, otherwise keep default "Bluetooth Speaker" name
            local device_name="$(bluetoothctl info ${mac} | grep Name: | awk -F': ' '{print $2}')"
            if [ -n "${device_name}" ]; then
                # =JBL Speaker
                # =f85c7de6c3aa
                # =7002
                # =v1
                # =-c /tmp/shairport-sync shairport-sync-bt.conf 7002 --name JBL Speaker --output alsa -d f85c7de6c3aa
                local env=${BASEDIR}/${version}-${alsadev}-extraopts.env
                local devname="${BT_DEFAULT_SYMBOL} ${device_name}"
                echo "DEVICE_NAME=\"${devname}\"" > ${env}
                echo "ALSA_DEVICE=\"${alsadev}\"" >> ${env}
                echo "PORT=\"${port}\"" >> ${env}
                echo "VERSION=\"${version}\"" >> ${env}
                echo "OPTS=\"-c ${BASEDIR}/${SHAIRPORT_SYNC}-${version}-${alsadev}-bt.conf ${port} --name '${devname}' --output=alsa -- -d ${alsadev}\"" >> ${env}
            fi

            systemctl start ${SHAIRPORT_SYNC}-${version}@${alsadev}.service
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
