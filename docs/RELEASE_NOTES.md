# 0.1.0-alpha

Public-source preparation of the original Android TV launcher.

- Removed fixed household greeting and location. Added private device preferences, city lookup and opt-in AirPlay handover.
- Preserved dynamic app banners, focus color, remote navigation, inputs and existing utility actions.
- Hide Ambilight on devices without a resolvable Philips action. Added launcher icon/banner and TV app entry point.
- Release builds no longer fall back to the debug key. Private signing material stays outside the source tree.
- Added Turkish/English README, numbered setup, agent prompts, MIT license, font notices, CI and a bilingual static Netlify site with fictional demonstration data.

## Verification

JDK 17 / SDK 34: `assembleDebug lintDebug assembleRelease lintRelease` passed. Lint retains warnings about platform View constructors, Turkish app strings, draw allocation, TV landscape orientation and banner size. Only `ExpiredTargetSdkVersion` is disabled, explicitly because this release is an Android 11 sideload app rather than a Play Store submission. Target/min SDK remain 30 to preserve the original device behavior.

Signed APK verified with Android SDK apksigner. Public signing certificate SHA-256:
`fa4a021c1069a04bcb48a3dd424de07e7ccc4f4bd5b7d480e31cc82f525ac051`.

Website checked at desktop and 390px mobile: Turkish/English switch, matching prompt, copy action, fictional app focus and horizontal overflow. Build artifacts, local SDK paths, old handoff, device maintenance records, private screenshots and signing keys are excluded from the public source archive.

**Physical TV validation has not been performed on this edited release.** The original private version's device history is not a test result for this APK. Treat this as an alpha and follow the compatibility/recovery guide. Android app UI remains Turkish.
