# Q10 Chromium Browser

Project space for a modern Chromium browser targeting BlackBerry Q10 / BlackBerry 10 OS 10.3.

## Engineering decision

Use the existing **Berry Browser V3 / chromium-for-bb10** port as the engine and browser implementation baseline. Do not start a Chromium build from scratch in Momentics: the upstream port already contains the QNX ARM32 engine, windowed rendering, browser UI, and BB10 packaging work.

- Upstream browser and releases: https://sw7ft.github.io/berry-browser/
- Upstream engine source: https://github.com/sw7ft/chromium-for-bb10
- Active upstream source branch: `berry-v3`

The upstream project advertises Chromium 120.0.6099.234, BB10 10.3.x, and automatic panel support including Q10/Q5 (720×720). The upstream release hub currently advertises version 3.0.2 build 106. This is upstream software, not a custom build produced by this repository.

## Target

- Device: BlackBerry Q10
- OS: BlackBerry 10.3.x
- CPU: ARMv7 / QNX
- Package: BAR
- Screen: 720×720

## Project status

**Upstream audit completed; custom rebrand/build not completed.**

A working custom BAR cannot be claimed until the upstream source is cloned, its build dependencies are available, the desired changes are implemented, and the result is built and tested on a physical Q10.

## Why this route

The upstream Chromium port documents a Linux build host, QNX SDP 8.0-compatible ARM toolchain, Clang 17, LLD 17, and device-side testing. Momentics 2.1.2 on Windows is useful for legacy Cascades work, but it is not by itself the documented environment for building this Chromium port.

See [UPSTREAM-AUDIT.md](UPSTREAM-AUDIT.md) for the verified findings and remaining work.

## Scope for a custom derivative

1. Pin an upstream `berry-v3` source revision and record its license/notices.
2. Confirm Q10 install requirements and test the unmodified upstream BAR first.
3. Decide the custom app identity, icon, name, and browser defaults.
4. Change the upstream app only where its source and license permit.
5. Build/sign the BAR using the documented QNX toolchain.
6. Test launch, touch mapping, keyboard, HTTPS, tabs, downloads, video, memory use, and exit/restart on Q10.

## Important

Do not rename or redistribute an upstream BAR as if it were our own build. Keep upstream attribution and third-party notices. Do not claim compatibility or successful installation until tested on the actual device.
