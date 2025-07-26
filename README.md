Seamless Point
================
![Seamless Point Logo](./assets/seamless-point-github-logo.png)

**The Universal Audio Hub for Music, Media & Gaming**

Seamless Point is a minimalist audio device that seamlessly connects all your sound outputs, whether you're listening to music, watching movies, or playing on PC.  
Compatible with **[AirPlay](https://www.pocket-lint.com/apple-airplay-2-vs-airplay-what-s-the-difference/), USB audio, and wireless speakers**, it acts as a smart audio bridge between your devices and your environment.  

Designed for gamers and audiophiles alike, Seamless Point doubles as a **dedicated PC gaming audio station**, offering flexible device switching.  
Whether you're using USB headphones or Bluetooth headsets, Seamless Point keeps everything in sync—so your experience is uninterrupted, immersive, and effortless.  

![Hardware Render](./assets/seamless-point-hardware-render.png)

Support
=======
Hardware: Bluetooth Audio and USB Audio  
Sofware: Linux-based, built on Yocto Project  
Yocto version: Walnascar (5.2.2)  
Airplay: AirPlay and Airplay 2 - Integration of [mikebrady/shairport-sync](https://github.com/mikebrady/shairport-sync)  
WiFi Provisioning: Integration of [balena-os/wifi-connect](https://github.com/balena-os/wifi-connect)  
Chromecast: None  

iOS macOS: Native  
Windows: [Tune Blade](http://www.tuneblade.com/)  
Android: None  

Supported Hardware platforms: Raspberry Pi 4b

Roadmap
=======
- [ ] Implement LVGL UI for [1.28inch Round LCD Display Module with Touch panel](https://www.waveshare.com/1.28inch-touch-lcd.htm) with [touchscreen support](https://github.com/kuzhylol/cst816x-driver)
- [ ] Tailor [seamless point web-UI](https://github.com/kuzhylol/seamless-point-ap-ui) for WiFi provisioning
- [ ] Enchance AirPlay latency to support low-latency audio streaming to real-time gaming
- [ ] Implement Chromecast support for Android
- [ ] Port to other hardware platforms (e.g. [Radxa ZERO 3W](https://radxa.com/products/zeros/zero3w)
- [ ] Port [OSTree](https://ostreedev.github.io/ostree/introduction/) for atomic updates
- [ ] Port Secure Boot
- [ ] Design industrial-grade PCB & plastic case

Quick start
===========

Dependencies
------------
Requirements: [Compatible Linux Distribution](https://docs.yoctoproject.org/5.2.2/brief-yoctoprojectqs/index.html#compatible-linux-distribution)  
Install [Yocto host packages](https://docs.yoctoproject.org/5.2.2/brief-yoctoprojectqs/index.html#build-host-packages)  
Install [repo](https://source.android.com/setup/develop/repo)  

Sync Yocto sources
------------------
```bash
repo init -u git@github.com:kuzhylol/seamless-point-manifest.git -b walnascar  
repo sync -j$(nproc)
```

Build Yocto image
-----------------
```bash
TEMPLATECONF=meta-seamless-point/conf/templates/sp-main source poky/oe-init-build-env
bitbake core-image-minimal
```

Flash image to SD card
----------------------
Use bmaptool to copy the generated .wic.bz2 file to the SD card
