DESCRIPTION = "Daemon that monitors timing data from PTP clocks"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=a059ae3bc5595cecba13e5305bc42c8d \
                    file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/mikebrady/nqptp.git;protocol=https;branch=main \
           file://nqptp.service \
          "

PV = "1.2.5+git"
SRCREV = "b8384c4a53632bab028c451a625ef51a1e767f29"

S = "${UNPACKDIR}/git"

inherit autotools pkgconfig systemd

EXTRA_OECONF = ""

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/nqptp ${D}${bindir}/nqptp
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${B}/nqptp.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE:${PN} = "${BPN}.service"
