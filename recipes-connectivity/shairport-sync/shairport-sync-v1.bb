include shairport-sync_git.inc

SRC_URI += "file://shairport-sync-v1-usb.conf \
            file://shairport-sync-v1-bt.conf \
            file://shairport-sync-v1@.service \
            file://shairport-sync-v1@usb.service \
           "

PACKAGECONFIG ??= " alac alsa mbedtls soxr dns-sd"

DEPENDS += "libconfig"

do_install:append() {
    install -d ${D}${sysconfdir}/
    install -d ${D}${systemd_unitdir}/system/

    install -m 0644 ${UNPACKDIR}/${PN}-usb.conf ${D}${sysconfdir}/
    install -m 0644 ${UNPACKDIR}/${PN}-bt.conf ${D}${sysconfdir}/

    install -m 0644 ${UNPACKDIR}/${PN}@usb.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${UNPACKDIR}/${PN}@.service ${D}${systemd_unitdir}/system/

    mv ${D}${bindir}/shairport-sync ${D}${bindir}/${PN}
}

FILES:${PN} += "${systemd_unitdir}/system/*"
FILES:${PN}-dev += "${sysconfdir}/shairport-sync.conf ${sysconfdir}/shairport-sync.conf.sample"

CONFFILES:${PN} = "${sysconfdir}/${PN}-usb.conf ${sysconfdir}/${PN}-bt.conf"
