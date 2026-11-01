# Publication audit

Only a new clean source tree is used; no original Git history was imported.

Removed fixed household names and weather coordinates from application code. No automatic location discovery is used. Initial preferences are empty. The old handoff and sibling maintenance folder/screenshots were never copied.

Excluded: local.properties, Gradle/Kotlin caches, build outputs, APKs/AABs, keystores, private keys, environment files, IDE state and Netlify local state. Release APK belongs in Releases, not Git. Generated website download ZIP is built only from tracked source and does not include itself.

Reviewed network calls: Open-Meteo city lookup and weather; manually triggered Cloudflare speed download; AirPlay status on loopback. No developer telemetry endpoint. Preview names, app artwork and measurements are fictional. Fonts include upstream public copyright/license notices.

The source file set and download archive are scanned for credential formats, private key headers, personal filesystem paths, original private values, local network addresses and prohibited files. Automated checks supplement source review; they do not prove the absence of every possible secret.
