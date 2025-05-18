# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   lvgl/.github/workflows/verify_font_license.yml
#   lvgl/COPYRIGHTS.md
#   lvgl/scripts/built_in_font/font_license/DejaVuSans/LICENSE
#   lvgl/scripts/built_in_font/font_license/FontAwesome5/LICENSE.txt
#   lvgl/scripts/built_in_font/font_license/SourceHanSansSC/LICENSE.txt
#   lvgl/scripts/font_license_verify.py
#   lvgl/src/libs/freetype/LICENSE.txt
#   lvgl/src/libs/gif/LICENSE.txt
#   lvgl/src/libs/lz4/LICENSE.txt
#   lvgl/src/libs/qrcode/LICENSE.txt
#   lvgl/src/libs/tiny_ttf/LICENSE.txt
#   lvgl/src/libs/tjpgd/LICENSE.txt
#   lvgl/src/stdlib/builtin/LICENSE_SPRINTF.txt
#   lvgl/src/stdlib/builtin/LICENSE_TLSF.txt
#
# NOTE: multiple licenses have been detected; they have been separated with &
# in the LICENSE value for now since it is a reasonable assumption that all
# of the licenses apply. If instead there is a choice between the multiple
# licenses then you should change the value to separate the licenses with |
# instead of &. If there is any doubt, check the accompanying documentation
# to determine which situation is applicable.
LICENSE = "BSD-2-Clause & MIT & Zlib"
LIC_FILES_CHKSUM = "file://LICENSE;md5=802d3d83ae80ef5f343050bf96cce3a4 \
                    file://lvgl/.github/workflows/verify_font_license.yml;md5=030679084385542b2eb05aeb062e4451 \
                    file://lvgl/COPYRIGHTS.md;md5=9d249e087fb5e0235f04cb8915cdb536 \
                    file://lvgl/LICENCE.txt;md5=4570b6241b4fced1d1d18eb691a0e083 \
                    file://lvgl/scripts/built_in_font/font_license/DejaVuSans/LICENSE;md5=9f867da7a73fad2715291348e80d0763 \
                    file://lvgl/scripts/built_in_font/font_license/FontAwesome5/LICENSE.txt;md5=57f9201afe70f877988912a7b233de47 \
                    file://lvgl/scripts/built_in_font/font_license/SourceHanSansSC/LICENSE.txt;md5=28010d6596196c21d55c40589bac441f \
                    file://lvgl/scripts/font_license_verify.py;md5=5381f8765833f66cba88c52ea32e06c7 \
                    file://lvgl/src/libs/barcode/LICENSE.txt;md5=b83ada61bff6502642f9e4a836e0a091 \
                    file://lvgl/src/libs/expat/LICENSE.txt;md5=f4fedd6116da0e171f7cb4d2923d7ac2 \
                    file://lvgl/src/libs/freetype/LICENSE.txt;md5=8204d4c9f845ceb938efdf5f1c41c893 \
                    file://lvgl/src/libs/gif/LICENSE.txt;md5=f0f3dab4f3dfdda8096974e1e49b3e53 \
                    file://lvgl/src/libs/lodepng/LICENSE.txt;md5=f2d8f4ae2038be44c68140e97ddea022 \
                    file://lvgl/src/libs/lz4/LICENSE.txt;md5=5cd5f851b52ec832b10eedb3f01f885a \
                    file://lvgl/src/libs/qrcode/LICENSE.txt;md5=33933b8e344d74b8f3d6bd9dde33cb94 \
                    file://lvgl/src/libs/thorvg/LICENSE.txt;md5=8d5712e3d7ddc640ce860065f53b9984 \
                    file://lvgl/src/libs/tiny_ttf/LICENSE.txt;md5=16669cc73f390986b2ba41733ff37193 \
                    file://lvgl/src/libs/tjpgd/LICENSE.txt;md5=b9c04acd8926ed7727fd6fab023b70f6 \
                    file://lvgl/src/stdlib/builtin/LICENSE_SPRINTF.txt;md5=2cddead72fdfcf0c7f5e6af22deedd08 \
                    file://lvgl/src/stdlib/builtin/LICENSE_TLSF.txt;md5=887c073d6c857dff20183c4ddb072f1b \
                    file://lvgl/tests/src/test_libs/rnd_unicodes/rnd_unicodes/LICENSE;md5=acb0454e3ea1c914a0f3c336a3f1a12b"

SRC_URI = "gitsm://github.com/lvgl/lv_port_linux.git;protocol=https;branch=master \
           file://lvglbenchmark.service \
           file://0002-cmake-lvgl-demo.patch \
           file://0001-adjust-lv_conf.h-for-fbdev.patch \
           file://0002-disable-slide-show.patch \
           "

# Modify these as desired
PV = "1.0+git"
SRCREV = "adbca697c18f3f8e0df2f7b6c3f90f2e2ab7782a"

S = "${WORKDIR}/git"

# NOTE: unable to map the following pkg-config dependencies: sdl2 glew glfw3 SDL2_image
#       (this is based on recipes that have previously been built and packaged)
DEPENDS = "libxkbcommon libdrm libx11 wayland libevdev wayland-protocols"

inherit cmake pkgconfig systemd

SYSTEMD_SERVICE:${PN} = "lvglbenchmark.service"

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = ""
CMAKE_PROJECT_NAME = "lvgl_app"
CMAKE_PROJECT_VERSION = "2.0"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/bin/lvglbenchmark ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/lvglbenchmark.service ${D}${systemd_system_unitdir}
}
