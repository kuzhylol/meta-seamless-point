#!/bin/sh -x

MAX_RETRIES=30
SLEEP_TIME=1
RETRY=0

start() {
    echo "Starting wifi-connect..."
    mac_address=$(cat /sys/class/net/wlan0/address | tr -d ':' | cut -c 1-8)
    wifi-connect -u /usr/share/ui/build --portal-ssid "SeamlessPoint-${mac_address}"
}

if [ "$1" = "start" ]; then
    while [ $RETRY -lt $MAX_RETRIES ]; do
        if ip route | grep -q "default"; then
            echo "Default route exists, skipping start"
            exit 0
        else
            echo "No default route found. Attempt #$((RETRY + 1))"
            sleep $SLEEP_TIME
            RETRY=$((RETRY + 1))
        fi
    done

    start
fi

if [ "$1" = "wlan0" ] && [ "$2" = "down" ]; then
    systemctl start wifi-connect.service
fi
