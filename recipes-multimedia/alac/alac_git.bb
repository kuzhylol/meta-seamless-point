DESCRIPTION = "Apple Lossless Audio Codec (ALAC)"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=8776da86de08b367a8518ae8cd7491ab \
                    file://LICENSE;md5=5cf67868b9e038eccb149ec80809d9f5 \
                    file://codec/APPLE_LICENSE.txt;md5=b180a94f894d2a868d40ea43da2bbaba"

SRC_URI = "git://github.com/mikebrady/alac.git;protocol=https;branch=master"

PV = "0.0.7+git"
SRCREV = "1832544d27d01335d823d639b176d1cae25ecfd4"

S = "${WORKDIR}/git"

inherit autotools

EXTRA_OECONF = ""
