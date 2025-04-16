#!/bin/sh -x

start() {
    mac_address=$(cat /sys/class/net/wlan0/address | tr -d ':' | cut -c 1-8)
    wifi-connect -u /usr/share/ui/build --portal-ssid "SeamlessPoint-${mac_address}"
}

if [ "$1" = "wlan0" ] && [ "$2" = "down" ]; then
    echo "Starting wifi-connect..."
    systemctl start wifi-connect.service
    exit 0
fi

if [ "$1" = "start" ]; then
    ip route | grep -q "default" || start
fi

