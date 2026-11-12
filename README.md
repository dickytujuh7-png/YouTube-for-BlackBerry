# YouTube for BlackBerry

Experimental Android WebView wrapper for BlackBerry 10 Android Runtime.

- Minimum Android API: 18 (Android 4.3)
- Target SDK: 28
- Uses the device's built-in Android System WebView to open YouTube mobile.

GitHub Actions builds a debug APK on push to main or manual workflow dispatch. The APK is uploaded as `YouTube-for-BlackBerry-debug`.

Important: a successful build does not guarantee current YouTube playback or sign-in on BlackBerry Q10. This depends on the device's old WebView, TLS and YouTube compatibility.
