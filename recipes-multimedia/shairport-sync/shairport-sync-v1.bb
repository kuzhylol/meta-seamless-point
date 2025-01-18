include shairport-sync_git.inc

SRC_URI += "file://shairport-sync-v1-usb.conf \
            file://shairport-sync-v1-bt.conf \
            file://shairport-sync-v1@usb.service \
            file://shairport-sync-v1@.service"

EXTRA_OECONF = "--with-apple-alac --with-alsa --with-dns_sd --with-soxr --with-ssl=mbedtls"
EXTRA_OEMAKE = "CFLAGS=-I${STAGING_INCDIR}/avahi-compat-libdns_sd"

do_install:append() {
    install -d ${D}${sysconfdir}/
    install -d ${D}${systemd_unitdir}/system/

    install -m 0644 ${WORKDIR}/shairport-sync-v1-usb.conf ${D}${sysconfdir}/
    install -m 0644 ${WORKDIR}/shairport-sync-v1-bt.conf ${D}${sysconfdir}/

    install -m 0644 ${WORKDIR}/shairport-sync-v1@usb.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/shairport-sync-v1@.service ${D}${systemd_unitdir}/system/

    mv ${D}${bindir}/shairport-sync ${D}${bindir}/shairport-sync-v1
}

FILES:${PN} += "${systemd_unitdir}/system/*"
FILES:${PN}-dev += "${sysconfdir}/shairport-sync.conf ${sysconfdir}/shairport-sync.conf.sample"

CONFFILES:${PN} = "${sysconfdir}/shairport-sync-v1-usb.conf ${sysconfdir}/shairport-sync-v1-bt.conf"
