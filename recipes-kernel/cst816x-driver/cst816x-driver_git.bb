LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=570a9b3749dd0463a1778803b12a6dce"

SRC_URI = "git://github.com/kuzhylol/cst816x-driver.git;protocol=https;branch=main"

PV = "1.0+git"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit module

DEPENDS += "dtc-native"

do_compile:append:raspberrypi4-64() {
    dtc -@ -I dts -O dtb -o hynitron-cst816s.dtbo rpi4b/hynitron-cst816s.dts
}

do_install:append:raspberrypi4-64() {
    install -d ${D}/boot/overlays
    install -m 0644 hynitron-cst816s.dtbo ${D}/boot/overlays/
}

do_buildclean[noexec] = "1"

FILES:${PN} += "/boot/overlays/hynitron-cst816s.dtbo"
