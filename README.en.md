# Makallete TV Launcher

[Türkçe](README.md) · [Installation guide](docs/SETUP.md)

A lightweight, ad-free home screen for Android TV. Apps, HDMI inputs and a handful of useful tools in one place. Built with Kotlin and Android platform Views, with no third-party runtime libraries.

**Alpha:** The original app was used on a Philips 65PUS7906/62 running Android 11. See the release notes for checks performed on this public edition. Other devices are unverified. The app UI is currently Turkish; the website and installation documentation are available in Turkish and English.

## Features

- Automatically discovers installed TV apps and their banners. Navigate with the remote, press OK to launch, or hold OK for app details.
- Focus-driven ambient color, clock, date and optional personal greeting.
- HDMI/CEC and tuner inputs; Ambilight panel shortcut on supported Philips TVs.
- Optional city weather from Open-Meteo, manual Cloudflare download/latency test, and memory tools.
- Optional automatic handover to a **separately installed** AirPlay receiver during mirroring.

Names, weather and the AirPlay watcher are off by default. Use **Düzenle** (Edit) to set a greeting and search for your city. Preferences stay on the TV; city queries and selected coordinates are sent to Open-Meteo. No location permission or account is needed.

## Install

1. Follow the [numbered guide](docs/SETUP.md) for exact downloads, folders and TV actions.
2. If a signed APK is available, download it from this repository's Releases page. Otherwise build the source with Android Studio. A source ZIP cannot be installed as an APK.
3. For Codex or Claude Code, open the project folder and use the [installation prompt](docs/INSTALL_PROMPT.en.txt).

## Build

Use JDK 17, Android SDK platform 34 and build-tools 34.0.0. Open the repository root in Android Studio and install these packages with SDK Manager. Keep its generated `local.properties` private.

```sh
./gradlew assembleDebug lintDebug
```

Windows: `gradlew.bat assembleDebug lintDebug`. Output: `app/build/outputs/apk/debug/app-debug.apk`. Debug builds are for development. [Release signing](docs/RELEASING.md) uses a private key outside the repository; release builds no longer use the debug key.

## Limitations

Requires Android 11 / API 30 or newer. HDMI, tuner and Ambilight support depend on the vendor. The Philips DTV ID is preferred, with the first tuner as fallback. App order and hidden system packages are still defined in code. Memory cleanup is OS-dependent, especially restricted on recent Android versions. AirPlay handover depends on the receiver's log messages and may break after upstream updates; startup readiness may be delayed. This sideloaded app does not target current Play Store submission requirements.

## Website and privacy

The static bilingual site is in `site/`, with Netlify settings in `netlify.toml`. Run `python3 scripts/package-source.py` to generate the source download ZIP. Preview locally with `python3 -m http.server 8080 --directory site`. The preview uses real app brands and İstanbul as the example city. Time and date follow live Türkiye time; weather and device readings are illustrative. The room image was generated from a Philips 65PUS7906/62 reference and is not a device capture. See [SECURITY.md](SECURITY.md) for network and permission details.

## License

MIT is the suggested license included for the launcher and site code. Fonts retain their OFL licenses. The optional GPL-3.0 AirPlay receiver is a separate project and is not bundled. See [LICENSE](LICENSE) and [third-party notices](THIRD_PARTY_NOTICES.md).

Developed by makallete
