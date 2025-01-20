# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "CLOSED"

SRC_URI = "file://99-seamless-point.rules \
           file://bt-interface.service \
           file://bt-interface.sh \
           file://usb-interface.service \
           file://usb-interface.sh \
           file://usbsound-controller.service \
          "

S = "${WORKDIR}"

do_install() {
    install -d ${D}${bindir}/
    install -d ${D}${systemd_unitdir}/system/

    install -m 0644 ${WORKDIR}/usb-interface.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/bt-interface.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/usbsound-controller.service ${D}${systemd_unitdir}/system/

    install -m 0755 ${WORKDIR}/usb-interface.sh ${D}${bindir}/
    install -m 0755 ${WORKDIR}/bt-interface.sh ${D}${bindir}/

    install -d ${D}${sysconfdir}/udev/rules.d/
    install -m 0755 ${WORKDIR}/99-seamless-point.rules ${D}/${sysconfdir}/udev/rules.d/
}

RDEPENDS:${PN} += "shairport-sync-v1 shairport-sync-v2 bluez-tools bluealsa bluez5 alsa-utils ntp nqptp gawk wpa-supplicant networkmanager python3-evdev python3-asyncio-glib coreutils bash"
RRECOMMENDS:${PN} += "\
    ${MACHINE_EXTRA_RRECOMMENDS} \
"

FILES:${PN} += "${systemd_unitdir}/system/*.service \
                ${systemd_unitdir}/system-preset/* \
                ${sysconfdir}/udev/rules.d/99-seamless-point.rules \
               "
