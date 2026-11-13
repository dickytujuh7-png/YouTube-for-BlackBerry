# BlackBerryFox

A minimal GeckoView browser shell intended for BlackBerry 10 Android Runtime devices.

- Name: BlackBerryFox
- Browser engine: GeckoView 68 Beta, ARMv7
- Minimum Android API: 18
- Target: BlackBerry Q10, 720 × 720 display
- Interface: compact address bar, back, forward, home, reload

Pushes to the `tiktok-lite` branch start the GitHub Actions build. A successful build uploads
`BlackBerryFox-ARMv7-debug` as a workflow artifact.

This is an initial build candidate. Installation and runtime compatibility still need testing
on a physical BlackBerry Q10; GeckoView 68 may not behave identically to the original Firefox APK.
