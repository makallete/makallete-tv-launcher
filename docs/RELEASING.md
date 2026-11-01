# Release / yayın

Release builds must use a private signing key. No key, password or signed APK belongs in Git. The provided release configuration reads these environment variables:

- `RELEASE_STORE_FILE`: absolute path to a keystore outside the repository.
- `RELEASE_STORE_PASSWORD`, `RELEASE_KEY_ALIAS`, `RELEASE_KEY_PASSWORD`: credentials passed privately to Gradle.

With those variables set, run `./gradlew assembleRelease lintRelease`. Without them, `assembleRelease` produces an **unsigned** APK, which cannot be installed. Never publish a debug-signed file as a production release. Do not print secret values, put them in shell history or save them under `site/`. Keep encrypted backups of the key and credentials: future in-place updates require the same key. A lost key cannot be recovered from the APK.

1. Run debug/release build and lint; review warnings and document supported OS/device scope.
2. Check the release certificate with Android SDK `apksigner verify --print-certs` and record only the public certificate fingerprint.
3. Test the signed APK on a TV: clean install, remote, app launch, long OK, Home, settings save/restart, city search/offline/disabled, HDMI/tuner and optional AirPlay. Test updating from the previous release if one exists. A debug installation uses a different key; do not silently uninstall it.
4. Scan the exact source archive and staged Git files for secrets, personal values, device logs and signing material. Never import the old handoff, sibling maintenance folder, screenshots or build history.
5. Upload the signed APK and SHA256SUMS to GitHub Releases, with release notes identifying build checks and actual device tests. A source archive is not an installable APK.
6. Netlify: import the repository and use `site` as the publish directory, with no build command; `netlify.toml` already contains these settings. Alternatively drag the contents of `site/` into Netlify's manual deploy UI. Only the site folder is public web content.

This project ships as alpha until the edited release has completed the physical-TV checklist. Development history in the original handoff describes the earlier private build, not verification of this release.

## Türkçe kısa not

İmza anahtarı ve şifreleri repo dışında saklanır; bu paketin kaynak kodunda yoktur. Güncelleme için aynı anahtar gerekir. Anahtarı şifreli yedekleyin. Siteyi yayınlamak için Netlify'a yalnızca `site/` içeriği verilir. Fiziksel TV kontrolü yapılmamış sürüm için test edildi iddiası kullanmayın.

Source archives, if needed for GitHub Releases, are generated with `python3 scripts/package-source.py` under `build/distributions/`. Do not copy them into `site/`; the website sends visitors to GitHub.
