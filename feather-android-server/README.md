# Feather server on Android (Termux)

This helper prepares the official Feather server to use Chromium installed from Termux repositories on an Android ARM64 phone.

## Requirements
- Android phone with Termux installed.
- Internet connection for initial package installation.

## Install
Save the file named install-termux.sh on the Android phone and run it from Termux:

    bash install-termux.sh

The script installs Node.js and Chromium, downloads the official Feather source, removes the optional sharp optimization (full JPEG frames still work), and configures Chromium for headless operation.

## Start

    bash ~/start-feather-server.sh

The first start prints an access token. Keep it private. On the Q10, enter the Android phone's local Wi-Fi IP and port in Feather settings:

    http://ANDROID-LAN-IP:8080/api

Both devices must use the same trusted Wi-Fi/hotspot. Keep Termux open while browsing. Do not expose port 8080 to the public internet.

## Important
This is an experimental Android/Termux adaptation, not a verified Android build. It has not yet been tested on the target Samsung phone or a BlackBerry Q10. Chromium package availability and Playwright compatibility can vary by Termux repository version. If startup fails, save the complete Termux error output before making further changes.

The Feather client remains the official BlackBerry 10 .bar release; this helper only prepares its server.
