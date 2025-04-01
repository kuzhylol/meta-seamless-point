SUMMARY = "Seamless Point WiFi AP UI"
HOMEPAGE = "https://github.com/kuzhylol/seamless-point-ap-ui"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI = "git://github.com/kuzhylol/seamless-point-ap-ui.git;protocol=https;branch=main"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

DEPENDS = "nodejs"

inherit npm

do_compile() {
    cd ${S}
    npm install
    npm run build
}

do_install() {
    install -d ${D}/usr/share/ui
    cp -r ${S}/build/* ${D}/usr/share/ui/
}

FILES:${PN} = "/usr/share/ui"
