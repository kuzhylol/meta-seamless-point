SUMMARY = "Utility for dynamically setting the WiFi configuration via a captive portal"
HOMEPAGE = "https://www.balena.io/blog/resin-wifi-connect/"
LICENSE = "Apache-2.0"

inherit cargo

SRC_URI = "git://github.com/balena-io/wifi-connect.git;protocol=https;branch=master"
SRCREV ="ac333eb6a809b4daf3ac2e41f6c56799852caddc"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3bfd34238ccc26128aef96796a8bbf97"

S = "${WORKDIR}/git"
CARGO_SRC_DIR = ""

SRC_URI += "file://cargo_update.patch \
            file://start-wifi-connect.sh \
            file://wifi-connect.service \
           "

DEPENDS = "libdbus-c++ pkgconfig-native"

RDEPENDS:${PN} += " networkmanager"

do_install:append () {
    install -d ${D}${datadir}/wifi-connect/ui
    cp -r ${S}/ui/build/* ${D}${datadir}/wifi-connect/ui

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/wifi-connect.service ${D}${systemd_unitdir}/system
    fi

    install -c -m 755 ${WORKDIR}/start-wifi-connect.sh ${D}${bindir}
}

FILES:${PN} += " \
    ${datadir}/ui \
    ${systemd_unitdir}/system/${BPN}.service \
"

inherit systemd

SYSTEMD_SERVICE:${PN} = "${BPN}.service"

require ${BPN}-crates.inc
