#!/bin/sh

AUX_SERVICE_NAME="aux"
AUX_DEV="usbstream"
AUX_DEFAULT_SYMBOL="🎧"
AUX_DEFAULT_NAME="Headphones"

set_aux_env() {
    local auxenv="/tmp/${1}-${AUX_SERVICE_NAME}-extraopts.env"
    if [ ! -r ${auxenv} ]; then
        local devname="${AUX_DEFAULT_SYMBOL} $(aplay -l 2>/dev/null | awk -F' \[|\]' '/card [0-9]+: / {print $2}')"
        local alsadev="$(aplay -L 2>/dev/null | grep ${AUX_DEV} | awk -F'=' '{print $2}')"
        echo "DEVICE_NAME=\"${devname}\"" > ${auxenv}
        echo "ALSA_DEVICE=\"${AUX_ALSA_DEV}\"" >> ${auxenv}
        echo "OPTS=\"--name '${devname}' --output=alsa -- -d hw:${alsadev}\"" >> ${auxenv}
    fi
}

remove_aux_env() {
    local auxenv="/tmp/${1}-${AUX_SERVICE_NAME}-extraopts.env"
    if [ -r ${auxenv} ]; then
        rm -f ${auxenv}
    fi
}

start_services() {
    for version in v1 v2; do
        systemctl is-active --quiet shairport-sync-${version}@${AUX_SERVICE_NAME}.service
        if [ ${?} -ne 0 ]; then
            set_aux_env ${version}
            systemctl start shairport-sync-${version}@${AUX_SERVICE_NAME}.service
        fi
    done
}

stop_services() {
    for version in v2 v1; do
        systemctl is-active --quiet shairport-sync-${version}@${AUX_SERVICE_NAME}.service
        if [ ${?} -eq 0 ]; then
            remove_aux_env ${version}
            systemctl stop shairport-sync-${version}@${AUX_SERVICE_NAME}.service
        fi
    done
}

start() {
    while true; do
        # Detect AUX device
        if aplay -L 2>/dev/null | grep -q ${AUX_DEV}; then
            start_services
        else
            stop_services
        fi
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
