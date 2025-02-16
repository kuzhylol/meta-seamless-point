#!/bin/sh -x

nmcli -t -f TYPE connection show --active | grep -q wifi && exit 0

mac_address=$(cat /sys/class/net/wlan0/address | tr -d ':' | cut -c 1-8)

wifi-connect -u /usr/share/wifi-connect/ui --portal-ssid "SeamlessPoint-${mac_address}"
