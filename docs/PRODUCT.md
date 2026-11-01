# Makallete TV Launcher
<!-- impeccable:product-schema 1 -->

## Platform
web

This document describes the promotional site. The product itself is a native Android TV app using Kotlin and platform Views.

## Users and purpose
Android TV owners who want a simple, ad-free home screen and need numbered installation instructions. Originally tested on Philips 65PUS7906/62, Android 11. Help a newcomer understand compatibility, get the source, and install with Codex or Claude Code.

## Capabilities and constraints
Dynamic TV app banners, focus-driven background light, HDMI/CEC and tuner inputs, clock, optional weather and AirPlay integration included in the full setup prompt, speed and memory tools. Vendor-specific features and AirPlay permissions need explicit explanation. Other devices are unverified. The app interface is Turkish; site and documentation are Turkish and English.

## Brand commitments
Inherit the existing Kindle Daybook site: white paper, dark type, generous whitespace, fine rules, restrained controls, visual product demonstration. Natural Turkish, English alternative. Only author signature: Developed by makallete. No private data, real household screenshots or unverified performance claims. The follow-up brief explicitly requests real app brands, İstanbul as the example city, a live Türkiye clock, PlayStation 1 on HDMI 1, antenna emoji, and a Philips 65PUS7906/62 room mockup with changing Ambilight. The site must distinguish this effect from actual hardware control.

## Implementation assumption
Static HTML/CSS/JavaScript selected for a small Netlify site; no build service, account, analytics or runtime secrets required. The user's instruction to finish the work governs routine implementation decisions.

## Distribution

The website routes source review and setup to GitHub, and signed APK downloads to GitHub Releases. The ready-to-use agent prompt includes the repository URL and clones it into a permanent local working folder. No direct source ZIP is hosted on Netlify.

The installation prompt includes AirPlay receiver installation, permission setup, automatic handover and end-to-end mirroring checks by default; it does not ask users to opt in separately. APK defaults remain unchanged until setup enables the watcher.

The published copy uses concise, natural Turkish and keeps licensing as a footer link. The AirPlay demonstration uses Control Center / Screen Mirroring with a selected Makallete TV receiver; iPhone mirrors a home screen with app icons and Mac mirrors a Claude Code terminal. GitHub marks identify repository/release links. No direct website source ZIP, photo-sharing or wallpaper-only example is offered.
