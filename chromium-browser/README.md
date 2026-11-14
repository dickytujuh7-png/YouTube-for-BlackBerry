# Q10 Chromium Browser

A dedicated subproject for a custom Chromium-based browser targeting BlackBerry Q10 / BlackBerry 10 OS 10.3.

> This project is currently in the architecture and compatibility-audit stage. No Chromium engine binary is bundled here yet.

## Target

- Device: BlackBerry Q10
- OS: BlackBerry 10.3.x
- CPU: ARMv7
- Package: BAR
- Native UI: Cascades / C++
- Browser engine: Chromium port compatible with BlackBerry 10 (candidate: chromium-for-bb10 / Berry Browser V3)

## Important architecture decision

Do not attempt to embed desktop Chromium or compile an arbitrary modern Chromium release with the Momentics Windows IDE. The engine port must match the BlackBerry 10 QNX ABI, graphics stack, and available system libraries.

The native Cascades shell and the Chromium engine are separate deliverables. Integration is only valid after the exact engine interface, binary architecture, runtime dependencies, and redistribution license have been verified.

## Milestones

1. Audit the upstream Chromium-for-BB10 source, release artifacts, license, and build instructions.
2. Verify the engine's QNX ABI, ARMv7 compatibility, graphics integration, and runtime dependencies.
3. Build a minimal engine proof-of-concept on a Q10-compatible environment.
4. Implement the Cascades browser shell: address/search bar, navigation controls, tabs, bookmarks, history, and downloads.
5. Integrate the engine through its supported interface.
6. Package, install, and test a signed/debug BAR on a physical Q10.

## Repository layout

- `chromium-browser/README.md` — project scope, architecture, and milestones.
- Future source directories will be added only after the upstream engine interface and build environment are verified.

## Current status

Planning / upstream compatibility audit. A working BAR has not yet been produced.

## License

The browser shell's license will be selected before source code is added. Chromium and any upstream port retain their respective third-party licenses and notices.
