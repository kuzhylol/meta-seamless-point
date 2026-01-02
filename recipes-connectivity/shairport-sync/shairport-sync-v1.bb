include shairport-sync_git.inc

SRC_URI += "file://shairport-sync-v1-usb.conf \
            file://shairport-sync-v1-bt.conf \
            file://shairport-sync-v1@.service \
            file://shairport-sync-v1@usb.service \
           "

PACKAGECONFIG ??= " avahi alac alsa mbedtls soxr"

DEPENDS += "libconfig"

do_install:append() {
    install -d ${D}${sysconfdir}/
    install -d ${D}${systemd_unitdir}/system/

    install -m 0644 ${UNPACKDIR}/${PN}-usb.conf ${D}${sysconfdir}/
    install -m 0644 ${UNPACKDIR}/${PN}-bt.conf ${D}${sysconfdir}/

    install -m 0644 ${UNPACKDIR}/${PN}@usb.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${UNPACKDIR}/${PN}@.service ${D}${systemd_unitdir}/system/

    mv ${D}${bindir}/shairport-sync ${D}${bindir}/${PN}

    rm -f ${D}${sysconfdir}/shairport-sync.conf
    rm -f ${D}${sysconfdir}/shairport-sync.conf.sample
}

FILES:${PN} += "${systemd_unitdir}/system/*"

CONFFILES:${PN} = "${sysconfdir}/${PN}-usb.conf ${sysconfdir}/${PN}-bt.conf"
