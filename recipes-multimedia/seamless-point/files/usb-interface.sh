#!/bin/sh -x

source /tmp/usbsound_plug.env

if [ -z $CARD_ID ]; then
    echo "No USB device available"
    exit 1
fi

if [ -z $CARD_MODEL ]; then
    echo "No USB Sound Card model defined, continue..."
    CARD_MODEL="Headphones"
fi

USB_ID="$CARD_ID"
USB_DEV_NAME="$CARD_MODEL"

USB_SERVICE_NAME=usb
USB_DEFAULT_SYMBOL=🎧

set_usb_env() {
    echo USB_DEV_NAME=$USB_DEV_NAME > /tmp/${1}-${USB_SERVICE_NAME}-extraopts.env
    echo USB_ID=${USB_ID} >> /tmp/${1}-${USB_SERVICE_NAME}-extraopts.env
    echo OPTS=\"--name \'$USB_DEFAULT_SYMBOL $USB_DEV_NAME\' --output=alsa -- -d hw:$USB_ID\" >> /tmp/${1}-${USB_SERVICE_NAME}-extraopts.env
}

remove_usb_env() {
    [ -r /tmp/${1}-${USB_SERVICE_NAME}-extraopts.env ] && \
        rm -f /tmp/${1}-${USB_SERVICE_NAME}-extraopts.env
}

start_services() {
    for service_version in v1 v2; do
        set_usb_env $service_version
        systemctl is-active --quiet shairport-sync-${service_version}@${USB_SERVICE_NAME}.service
        if [ ${?} -ne 0 ]; then
            set_usb_env ${service_version}
            systemctl start shairport-sync-${service_version}@${USB_SERVICE_NAME}.service
        fi
    done
}

stop_services() {
    for service_version in v2 v1; do
        systemctl is-active --quiet shairport-sync-${service_version}@${USB_SERVICE_NAME}.service
        if [ ${?} -eq 0 ]; then
            remove_usb_env $service_version
            systemctl stop shairport-sync-${service_version}@${USB_SERVICE_NAME}.service
        fi
    done
}

start() {
    start_services
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
