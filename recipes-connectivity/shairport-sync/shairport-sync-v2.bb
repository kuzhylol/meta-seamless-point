include shairport-sync_git.inc

SRC_URI += "file://shairport-sync-v2-bt.conf \
            file://shairport-sync-v2-usb.conf \
            file://shairport-sync-v2@.service \
            file://shairport-sync-v2@usb.service \
           "

EXTRA_OECONF = "--with-airplay-2"
PACKAGECONFIG = " alac alsa avahi soxr openssl"

DEPENDS += "libav xxd-native libsodium libgcrypt libconfig libplist"

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
