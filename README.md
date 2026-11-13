# FeatherBrowser for BlackBerry Q10

A lightweight Android APK client for BlackBerry Q10 (Android Runtime API 18+, ARMv7). Web pages are rendered by Chromium on a separate Android phone and sent to Q10 over a local WebSocket connection.

## APK build
The `feather-android-server` branch builds the APK using GitHub Actions. Open the repository's Actions tab, select the latest successful **Build FeatherBrowser APK** run, and download the `FeatherBrowser-Q10-ARMv7` artifact ZIP. The ZIP contains the installable APK.

## First-version features
- Connect to a Feather server using its local IP and access token
- Address/search field
- JPEG page display
- Tap and scroll
- Back and reload

## Android phone server (experimental)
Install Termux on the Android phone, then run:

    pkg install -y git
    git clone -b feather-android-server https://github.com/dickytujuh-png/YouTube-for-BlackBerry.git
    cd YouTube-for-BlackBerry/feather-android-server
    bash install-termux.sh

Start the server:

    bash ~/start-feather-server.sh

The first run prints an access token. Keep it private. Find the Android phone's local Wi-Fi IP in Android network settings. On Q10, enter `http://PHONE-IP:8080`, enter the token, then tap **Hubungkan**.

Both devices must be on the same trusted Wi-Fi or hotspot. Keep Termux active while browsing. Do not expose port 8080 to the public internet.

## Important limitations
The APK build succeeded, but it has not yet been tested on a physical Q10. The Android/Termux Chromium + Playwright server setup is experimental and also needs real-device testing. This initial client does not include tabs, bookmarks, downloads, or audio/video streaming.