# Upstream audit — Q10 Chromium Browser

Audit date: 2026-11-14

## Findings

### 1. A BB10 Chromium browser already exists

The upstream Berry Browser V3 release hub advertises:
- Version 3.0.2, build 106
- Chromium 120.0.6099.234
- BB10 10.3.x
- Q10 and Q5 support with 720×720 panel detection
- A sideloadable BAR of approximately 55 MB
- Beta status; some heavyweight sites may still struggle

Official release hub: https://sw7ft.github.io/berry-browser/

### 2. Engine source exists

The engine source repository is https://github.com/sw7ft/chromium-for-bb10.

Its README identifies `berry-v3` as the active full-source branch containing the Chromium port, shims, launcher, and packaging. The `main` branch README is an older snapshot that advertises build 84, while the separate release hub advertises build 106. For that reason, pin and verify the exact source revision that corresponds to any selected release before making a derivative.

### 3. Build environment is not the user's current Windows IDE setup

The engine repository's documented source-build route calls for:
- Linux host (Ubuntu/Docker in the upstream instructions)
- Chromium source at the specified base commit
- QNX SDP 8.0-compatible ARM toolchain
- Clang 17 and LLD 17
- Device-side deployment/testing

Momentics 2.1.2 on Windows is not listed as a standalone build environment for the Chromium engine. A Cascades-only shell project would not magically provide Chromium rendering.

### 4. Compatibility and install caution

The release hub says BB10 10.3.x and lists Q10. Another upstream BerryCore page describes its early browser beta as requiring a rooted device, while the release hub describes ordinary sideloading. These statements may refer to different packages/releases. Confirm the selected build's install requirements before assuming a standard non-rooted Q10 can install it.

## Engineering decision

Do not reimplement Chromium. First test the official Berry Browser V3 build on the target Q10. If it works, the fastest path to a useful Q10 browser is to use that implementation as the baseline. A custom-branded derivative remains a separate engineering task requiring source revision pinning, license review, a Linux/QNX build environment, and physical-device testing.

## Remaining tasks

- [ ] Confirm exact release asset and checksum from the upstream release page.
- [ ] Confirm installation requirements for the selected build on a standard Q10 OS 10.3.x.
- [ ] Test the upstream BAR on the physical Q10.
- [ ] Pin the matching `berry-v3` commit and review license/notices.
- [ ] Only then implement branding/features and produce a derivative BAR.
