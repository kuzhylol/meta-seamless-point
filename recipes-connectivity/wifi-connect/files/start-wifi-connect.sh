#!/bin/sh -x

sleep 15

nmcli -t -f TYPE connection show --active | grep -q 802-11-wireless && exit 0

mac_address=$(cat /sys/class/net/wlan0/address | tr -d ':' | cut -c 1-8)

wifi-connect -u /usr/share/wifi-connect/ui --portal-ssid "SeamlessPoint-${mac_address}"
