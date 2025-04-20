SUMMARY = "Utility for dynamically setting the WiFi configuration via a captive portal"
HOMEPAGE = "https://www.balena.io/blog/resin-wifi-connect/"
LICENSE = "Apache-2.0"

inherit cargo cargo-update-recipe-crates

SRC_URI = "git://github.com/balena-io/wifi-connect.git;protocol=https;branch=master \
           file://start-wifi-connect.sh \
           file://wifi-connect.service \
          "
SRCREV = "04e0008f87637fa71de76b3aa722eb7109dba2fd"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3bfd34238ccc26128aef96796a8bbf97"

S = "${WORKDIR}/git"
CARGO_SRC_DIR = ""

DEPENDS = "libdbus-c++ pkgconfig-native"

RDEPENDS:${PN} = "networkmanager dnsmasq"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/wifi-connect.service ${D}${systemd_unitdir}/system
    fi

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/start-wifi-connect.sh ${D}${bindir}

    install -d ${D}${sysconfdir}/NetworkManager/dispatcher.d
    ln -fsr ${D}${bindir}/start-wifi-connect.sh ${D}${sysconfdir}/NetworkManager/dispatcher.d/99-start-wifi-connect.sh
}

FILES:${PN} += "${bindir}/start-wifi-connect.sh \
                ${sysconfdir}/NetworkManager/dispatcher.d/99-start-wifi-connect.sh \
                ${systemd_unitdir}/system/${BPN}.service \
               "

inherit systemd

SYSTEMD_SERVICE:${PN} = "${BPN}.service"

require ${BPN}-crates.inc
