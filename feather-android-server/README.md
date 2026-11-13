# Feather server on Android (Termux)

This helper is an experimental adaptation of the official Feather server for an ARM64 Android phone. It installs Node.js and Chromium from Termux repositories and configures the server for local Wi-Fi access.

## Install
In Termux:

    pkg install -y git
    git clone -b feather-android-server https://github.com/dickytujuh7-png/YouTube-for-BlackBerry.git
    cd YouTube-for-BlackBerry/feather-android-server
    bash install-termux.sh

## Start

    bash ~/start-feather-server.sh

The first start prints the access token. Keep it private. Find the phone's local Wi-Fi IPv4 address in Android network settings.

## Connect from Q10
Install the `FeatherBrowser-Q10-ARMv7` APK built by the GitHub Actions workflow. In its server field, enter:

    http://ANDROID-PHONE-IP:8080

Enter the access token and tap **Hubungkan**. Both devices must use the same trusted Wi-Fi network or hotspot. Keep Termux open while browsing.

## Safety and limitations
- The server listens on the local network; do not expose port 8080 to the public internet.
- This Termux/Chromium/Playwright adaptation is experimental and has not yet been tested on the target phone.
- The first APK client supports basic navigation, page frames, tap, scroll, back, and reload. Tabs, downloads, and audio/video streaming are not included.
