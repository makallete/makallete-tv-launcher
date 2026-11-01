---
name: Makallete TV Launcher promotional site
description: A restrained bilingual introduction and installation guide inheriting Kindle Daybook.
colors:
  ink: "#171717"
  paper: "#fff"
  secondary: "#555"
  line: "#d7d7d4"
  wash: "#f5f5f2"
  button-hover: "#333"
  link-hover-border: "#999"
  room-ground: "#413b36"
  tv-tile: "#101010"
  tv-screen: "#0c121d"
  tv-text: "#eff1f6"
  tv-title: "#f3f1eb"
  tv-muted: "#b4bfce"
  tv-selection: "#ece9df"
  ambilight-netflix: "#df1730"
  ambilight-youtube: "#ff6943"
  ambilight-tod: "#ffbc12"
  ambilight-bein: "#8e56dc"
  ambilight-hbo: "#6f87d8"
  tv-status: "#81c7a9"
typography:
  display:
    fontFamily: "Manrope, Arial, sans-serif"
    fontSize: "44px"
    fontWeight: 600
    lineHeight: 1.13
    letterSpacing: "-0.035em"
  headline:
    fontFamily: "Manrope, Arial, sans-serif"
    fontSize: "26px"
    fontWeight: 600
    lineHeight: 1.35
    letterSpacing: "-0.025em"
  title:
    fontFamily: "Manrope, Arial, sans-serif"
    fontSize: "17px"
    fontWeight: 600
    lineHeight: 1.5
  body:
    fontFamily: "Manrope, Arial, sans-serif"
    fontSize: "16px"
    lineHeight: 1.7
  label:
    fontFamily: "Manrope, Arial, sans-serif"
    fontSize: "13px"
    fontWeight: 600
  tv-display:
    fontFamily: "Bricolage, sans-serif"
    fontSize: "7cqw"
    fontWeight: 800
    lineHeight: 1.15
    letterSpacing: "-0.035em"
  tv-body:
    fontFamily: "Onest, sans-serif"
rounded:
  language: "4px"
  control: "5px"
  screen: "0"
  source-link: "12px"
  room: "10px"
  preview-app: ".65cqw"
  step: "50%"
spacing:
  control-gap: "16px"
  mobile-gutter: "20px"
  page-gutter: "28px"
  hero-gap: "40px"
components:
  button-primary:
    backgroundColor: "{colors.ink}"
    textColor: "{colors.paper}"
    typography: "{typography.label}"
    rounded: "{rounded.control}"
    padding: "10px 17px"
  button-primary-hover:
    backgroundColor: "{colors.button-hover}"
  source-link:
    textColor: "{colors.ink}"
    rounded: "{rounded.source-link}"
    padding: "9px 14px"
  source-link-hover:
    backgroundColor: "{colors.wash}"
  prompt-field:
    backgroundColor: "{colors.wash}"
    textColor: "{colors.ink}"
    rounded: "{rounded.control}"
    padding: "16px"
    width: "100%"
  language-active:
    backgroundColor: "{colors.ink}"
    textColor: "{colors.paper}"
    rounded: "{rounded.language}"
    padding: "7px"
  preview-app:
    backgroundColor: "{colors.tv-tile}"
    rounded: "{rounded.preview-app}"
  step-number:
    backgroundColor: "{colors.wash}"
    textColor: "{colors.ink}"
    rounded: "{rounded.step}"
    size: "42px"
---

# Design System: Makallete TV Launcher promotional site

## Overview

**Creative North Star: “Inherited white-paper setup guide”**

The site inherits the existing Kindle Daybook authority: white paper, dark Manrope text, generous space, fine rules and restrained controls. The TV demonstration retains the launcher’s own Bricolage and Onest typography inside its dark screen. This is a continuation of the established visual system.

Authority: https://kindle-daybook-makallete.netlify.app/ (inspected during implementation on 2026-11-01). Product constraints are in `docs/PRODUCT.md`. Tokens were refreshed from `site/style.css`, component structure from `site/index.html`, and interaction behavior from `site/app.js`.

**Key Characteristics:**

- White paper and ink with fine neutral rules.
- Spacious numbered instructions and readable supporting copy.
- A Philips-based room demonstration with real app brands and focus-driven wall light.
- Natural Turkish and an English alternative.

## Colors

### Primary

Ink is the action color as well as the text color; there is no separate site accent. Primary controls reverse paper against ink, with a quieter dark hover.

### Neutral

Paper forms the page ground. Secondary supports explanatory text; line divides sections; wash distinguishes the prompt field and numbered markers. These preserve the supplied authority.

The TV tokens belong to the embedded product demonstration: a dark screen, pale text, muted metadata, warm selection outline and a green status light. Netflix, YouTube, TOD, beIN CONNECT and HBO Max each select a distinct Ambilight color. These are demonstration colors, not site accents. YouTube and beIN tiles use paper backgrounds; other tiles use the dark tile ground. Exact values are normative in the frontmatter; source CSS derives wall and screen light with color-mix from the selected color.

## Typography

Manrope is the locally hosted site family with Arial and sans-serif fallbacks. The local font weights are regular, semibold and bold. Bricolage (the CSS alias for Bricolage Grotesque) and Onest are reserved for the TV preview; their upstream OFL notices are preserved.

Display and section-heading roles use tight tracking. The hero introduction is 17px, supporting feature copy 15px, setup detail 14px, controls 13px, minor labels 12px, footer 11px, and preview caption 12px. Paragraph measure is capped at 72ch. The preview title is sized relative to its own inline-size container, so the entire screen scales together.

At the mobile breakpoint the display becomes 36px, section headings 24px, introduction 16px, and step headings 16px. The responsive hero removes the explicit line break; the source includes whitespace after it so words remain separated.

## Layout

The actual outer maximum width is **1160px**, including horizontal padding under border-box sizing. Desktop gutters are 28px, mobile gutters 20px. Header minimum height is 84px (82px mobile). Hero columns use a .7:1.6 ratio, 40px gap, 72px top and 68px bottom padding. The 1536×1024 room plate is 3:2; its HTML screen overlays the photographed television. The story uses 1:1.5 columns and a 55px gap. The dedicated AirPlay explanation repeats the story’s two-column structure, with 48px vertical padding, then 32px and one column on mobile. Setup, prompt and compatibility sections share an 820px maximum width and remain aligned with the content start.

At 900px and below, the hero becomes one column with a 38px gap and 45px top padding; the preview is capped at 780px. At 600px and below, the story becomes one column, hero spacing becomes 38px top/bottom and 32px gap, prompt controls stack, and the footer stacks. Setup begins with 62px top spacing on desktop and 38px on mobile. Numbered rows use a 68px text inset and 34px bottom margin, changing to 53px and 28px on mobile. Fine vertical rules connect the markers.

## Elevation & Depth

The surrounding page is flat: white ground, hairline rules and restrained filled controls. Depth belongs to the illustrative room photograph and the light cast around its TV. The photograph is darkened with brightness(.72). Desktop wall-light uses three screen-blended color-mix shadows, at 60%/60%/65% selected color; mobile uses two shorter shadows at 70%. The screen has a radial light at 29% of the same selected color. There are no floating page cards or decorative site glows.

The registered color property `--ambilight-color` transitions over .65s ease-out; app scale uses .25s ease-out. Smooth anchor scrolling is enabled. Reduced-motion preference disables transitions and restores automatic scrolling.

## Shapes

Site controls have small radii; source/guide links are more softly rounded. Numbered setup markers are circles. The room frame has 10px corners, reducing to 7px on mobile; the overlaid screen has square corners to fit the plate. Preview app corners scale with its container (.65cqw), while status dots are circular and screen chips are pill-shaped. Rules and control borders are 1px.

## Components

### Buttons and source links

Primary links and the copy button share the ink fill, semibold label, small radius and a 44px minimum height. Source and guide links use a light rule border, 42px minimum height and a wash hover; the mobile header link uses a 7px radius and 38px minimum height. Ordinary text links remain underlined with a 4px underline offset and gain a thicker underline on hover.

### Language navigation

Compact TR/EN buttons sit beside the GitHub link. The active language reverses ink/paper and exposes `aria-pressed`. Switching updates document language, title, description, copy, prompt, both guide links, preview group label and the live clock/date/greeting; URL query or local preference can choose the initial language. App UI language remains Turkish, which compatibility copy states explicitly.

### Numbered setup

Six ordered steps use outlined circular markers, a subtle wash and connecting fine rules. Mobile markers become 34px instead of 42px. Step titles remain prominent over quieter detail copy. Download and full-guide links are actual anchors.

### Prompt field

The readonly, vertically resizable textarea has a wash ground, line border, 5px radius, 16px padding and 13px/1.75 text. Minimum height is 200px, with eleven visible rows declared. Copy success and fallback use a polite live region; failed clipboard access selects the prompt for manual copying. Text-file alternatives appear when JavaScript is unavailable.

### Compatibility disclosure

Native details/summary elements use horizontal rules, semibold labels and plus/minus state indicators. Summary padding is 20px vertically; expanded body copy is 14px and ends with 22px padding. These cover compatibility, optional AirPlay, privacy, recovery and licensing.

### Interactive TV preview

The preview combines the generated Philips 65PUS7906/62 room plate with a live HTML launcher screen. The plate retains a PHILIPS badge; provenance, generation prompt, public product reference and brand asset sources are recorded in `site/assets/ASSET_SOURCES.md`. It is an illustrative room, not a photograph of a private home or evidence of brand partnerships. Brand marks remain their owners’ trademarks.

The screen overlay occupies left 14.24%, top 14.27%, width 71.65%, height 56.93% of the room. A separate light layer occupies left 13.9%, top 13.8%, width 72.4%, height 59.2%. Keep these paired with the plate; do not resize the screen independently.

Five real app brands appear: Netflix, YouTube, TOD, beIN CONNECT and HBO Max. Click or keyboard focus updates the title, selected state and room color; left/right arrows move through the buttons without wrapping. Selected tiles scale to 1.035 and expose `aria-pressed`. Five 16cqw tiles have 1.4cqw gaps. Logos are locally sourced assets using contain sizing. HDMI 1 shows 🎮 and PlayStation 1; the antenna label includes 📡. İstanbul is the example weather city.

Clock, date and greeting use `Europe/Istanbul` and localized `Intl.DateTimeFormat`, update every 1000ms, and refresh on language or document-visibility changes. The caption distinguishes the live Türkiye clock from sample weather/device readings and the illustrative room. The feature text explains that the real app opens Philips Ambilight settings and does not directly control the hardware wall light.

### AirPlay explanation

A prominent section before setup describes the separate, optional receiver and shows the textual path iPhone / Mac → AirPlay receiver → TV. It states the receiver is not bundled and automatic handover needs additional ADB permissions. The guide link follows the selected language; compatibility disclosure repeats the limitations.

### Accessibility states

Buttons, anchors, textarea and summary use a 2px ink focus outline with a 5px offset. Preview buttons instead use a 3px white outline with a 4px offset. A skip link becomes visible on focus. Preserve native controls, selection feedback, reduced-motion behavior and local font hosting.

## Do's and Don'ts

### Do:

- Do preserve the inherited Manrope, paper, ink and fine-rule vocabulary.
- Do distinguish the illustrative room and sample weather/device readings from the live Türkiye clock.
- Do keep setup content readable in document flow and downloads usable without JavaScript.
- Do retain keyboard focus, reduced-motion support and the exact author signature “Developed by makallete”.

### Don’t:

- Don’t extend the TV’s saturated palette or glow to the surrounding page.
- Don’t introduce private household data, implied brand partnerships or unverified performance claims.
- Don’t replace the inherited visual authority with a new identity.
