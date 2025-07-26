FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://journald.conf"

do_install:append() {
    install -m 0644 ${UNPACKDIR}/journald.conf ${D}${sysconfdir}/systemd/journald.conf
}

PACKAGECONFIG:remove = "networkd resolved nss-resolve"
