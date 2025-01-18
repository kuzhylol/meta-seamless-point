DESCRIPTION = "AirPlay audio player. Shairport Sync adds multi-room capability with Audio Synchronisation"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=ee2d008df96f0812830f2262b10ca8e0 \
                    file://LICENSES;md5=9f329b7b34fcd334fb1f8e2eb03d33ff \
                    file://tinyhttp/LICENSE;md5=38d4c96de11bf8a62631878ab7cd01a3"

SRC_URI = "git://github.com/mikebrady/shairport-sync.git;protocol=https;branch=master"

PV = "4.3.5+git"
SRCREV = "a92df98fadc20dbc4fad880c531ee9e83a38aeca"

S = "${WORKDIR}/git"

DEPENDS = "libgcrypt libav alac openssl libconfig mbedtls popt libsodium libplist glib-2.0 libsndfile1 alsa-lib libdaemon util-linux-libuuid avahi soxr xxd-native"

inherit autotools pkgconfig systemd

FILES:${PN}-doc += "${mandir}/*"
RDEPENDS:${PN} += "avahi"
