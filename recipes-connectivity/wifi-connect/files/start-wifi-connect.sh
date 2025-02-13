#!/bin/sh -x

IFACE=$1   # Interface name (e.g., wlan0)
EVENT=$2   # Event type (e.g., up, down, pre-up, etc.)

mac_address=$(cat /sys/class/net/wlan0/address | tr -d ':' | cut -c 1-8)

wget --spider http://google.com 2>&1

if [ $? -eq 0 ]; then
    printf 'Skipping WiFi Connect\n'
else
    printf 'Starting WiFi Connect\n'
    wifi-connect -u /usr/share/wifi-connect/ui --portal-ssid "SeamlessPoint-${mac_address}"
fi
