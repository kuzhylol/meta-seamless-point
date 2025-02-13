#!/bin/sh -x

IFACE=$1   # Interface name (e.g., wlan0)
EVENT=$2   # Event type (e.g., up, down, pre-up, etc.)

mac_address=$(cat /sys/class/net/wlan0/address | tr -d ':' | cut -c 1-8)

wifi-connect -u /usr/share/wifi-connect/ui --portal-ssid "SeamlessPoint-${mac_address}"
