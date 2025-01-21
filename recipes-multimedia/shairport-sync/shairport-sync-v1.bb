include shairport-sync_git.inc

SRC_URI += "file://0001-Split-advertise-per-version-support.patch \
            file://shairport-sync-v1-usb.conf \
            file://shairport-sync-v1-bt.conf \
            file://shairport-sync-v1@.service \
            file://shairport-sync-v1@usb.service \
           "

EXTRA_OECONF = "--with-apple-alac --with-alsa --with-dns_sd --with-soxr --with-ssl=mbedtls"
EXTRA_OEMAKE = "CFLAGS=-I${STAGING_INCDIR}/avahi-compat-libdns_sd"

do_install:append() {
    install -d ${D}${sysconfdir}/
    install -d ${D}${systemd_unitdir}/system/

    install -m 0644 ${WORKDIR}/${PN}-usb.conf ${D}${sysconfdir}/
    install -m 0644 ${WORKDIR}/${PN}-bt.conf ${D}${sysconfdir}/

    install -m 0644 ${WORKDIR}/${PN}@usb.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/${PN}@.service ${D}${systemd_unitdir}/system/

    mv ${D}${bindir}/shairport-sync ${D}${bindir}/${PN}
}

FILES:${PN} += "${systemd_unitdir}/system/*"
FILES:${PN}-dev += "${sysconfdir}/shairport-sync.conf ${sysconfdir}/shairport-sync.conf.sample"

CONFFILES:${PN} = "${sysconfdir}/${PN}-usb.conf ${sysconfdir}/${PN}-bt.conf"
