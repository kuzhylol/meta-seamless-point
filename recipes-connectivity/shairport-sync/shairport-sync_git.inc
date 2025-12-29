DESCRIPTION = "AirPlay audio player. Shairport Sync adds multi-room capability with Audio Synchronisation"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=ee2d008df96f0812830f2262b10ca8e0 \
                    file://LICENSES;md5=9f329b7b34fcd334fb1f8e2eb03d33ff \
                    file://tinyhttp/LICENSE;md5=38d4c96de11bf8a62631878ab7cd01a3"

SRC_URI = "git://github.com/mikebrady/shairport-sync.git;protocol=https;branch=master"

PACKAGECONFIG[alsa] = "--with-alsa,,alsa-lib"
PACKAGECONFIG[sndio] = "--with-sndio,,sndio"
PACKAGECONFIG[pulseaudio] = "--with-pa,,pulseaudio"
PACKAGECONFIG[pipewire] = "--with-pw,,pipewire"
PACKAGECONFIG[ao] = "--with-ao,,libao"
PACKAGECONFIG[jack] = "--with-jack,,jack"
PACKAGECONFIG[soundio] = "--with-soundio,,libsoundio"
PACKAGECONFIG[stdout] = "--with-stdout"
PACKAGECONFIG[pipe] = "--with-pipe"

PACKAGECONFIG[soxr] = "--with-soxr,,soxr"
PACKAGECONFIG[alac] = "--with-apple-alac,,alac"
PACKAGECONFIG[convolution] = "--with-convolution,,libsndfile1"

PACKAGECONFIG[daemon] = "--with-daemon,,libdaemon"

PACKAGECONFIG[mqtt] = "--with-mqtt-client"
PACKAGECONFIG[dbus] = "--with-dbus-interface,,dbus"
PACKAGECONFIG[dbus-test] = "--with-dbus-test-client"
PACKAGECONFIG[mpris] = "--with-mpris-interface,,dbus"
PACKAGECONFIG[mpris-test] = "--with-mpris-test-client"

PACKAGECONFIG[systemv] = "--with-systemv"
PACKAGECONFIG[freebsd-service] = "--with-freebsd-service"
PACKAGECONFIG[sygwin-service] = "--with-sygwin-service"

PACKAGECONFIG[openssl] = "--with-ssl=openssl,,openssl"
PACKAGECONFIG[mbedtls] = "--with-ssl=mbedtls,,mbedtls"
PACKAGECONFIG[polarssl] = "--with-ssl=polarssl,,polarssl"

PACKAGECONFIG[avahi] = "--with-avahi,,avahi,avahi-daemon"
PACKAGECONFIG[tinysvcmdns] = "--with-tinysvcmdns"
PACKAGECONFIG[external-mdns] = "--with-external-mdns"
PACKAGECONFIG[dns-sd] = "--with-dns_sd,,avahi,avahi-daemon"

PV = "4.3.7+git"
SRCREV = "3c8ceb7c97c8782903ec48e280023436711e0913"

S = "${UNPACKDIR}/git"

DEPENDS = "popt glib-2.0"

python () {
    if 'dns-sd' in d.getVar('PACKAGECONFIG').split():
        d.setVar('EXTRA_OEMAKE', f"CFLAGS=-I{d.getVar('STAGING_INCDIR')}/avahi-compat-libdns_sd")
}

inherit autotools pkgconfig systemd

FILES:${PN}-doc += "${mandir}/*"
