const english = {
  "skip": "Skip to content",
  "title": "Install your own<br> TV launcher.",
  "intro": "A simple, ad-free home screen for Android TV. Your apps and TV inputs in one place.",
  "start": "View on GitHub",
  "source": "APK releases",
  "support": "Android 11 and newer · Open source · Alpha",
  "demoSpeed": "Speed test",
  "apps": "Apps <span>5</span>",
  "inputs": "Inputs",
  "antenna": "📡 Antenna",
  "caption": "The clock shows current Türkiye time. Weather is illustrative.",
  "featureTitle": "Apps and TV inputs",
  "featureBody": "Installed apps appear with their banners. The selected app adds its color to the background. HDMI and antenna inputs are on the same screen.",
  "featureMore": "The top bar holds the clock, weather and shortcuts. Use Edit to set your name and city, or Ambilight to open the TV’s lighting settings.",
  "setupTitle": "Setup",
  "setupIntro": "Keep your computer and TV on the same network. If you use Codex or Claude Code, start with the prompt below.",
  "step1": "Check the TV’s Android version.",
  "body1": "Open Settings → System / Device Preferences → About. Android 11 or newer is required. The project was developed for Philips 65PUS7906/62; other models have not been tested.",
  "step2": "Review the project on GitHub.",
  "body2": "The source code and setup guide are on GitHub. The signed APK is available under Releases.",
  "download": "Open GitHub",
  "step3": "Send the setup prompt.",
  "body3": "Start a new Codex or Claude Code chat and paste the prompt below. It will fetch the project from GitHub, prepare the tools and explain what to do on the TV.",
  "tools": "Google Platform-Tools",
  "step4": "Authorize the TV connection.",
  "body4": "In About, press Build seven times. Enable network / USB debugging in Developer options, then accept the computer’s connection prompt on the TV. Menu names may vary.",
  "step5": "Install the launcher and AirPlay.",
  "body5": "The prompt prepares the launcher, AirPlay receiver and settings together. Test apps and TV inputs, then choose Makallete TV Launcher as your home screen.",
  "releases": "APK releases and release notes",
  "step6": "Test mirroring and finish setup.",
  "body6": "Select your TV from Screen Mirroring on your Mac or iPhone. Check that stopping mirroring returns to the launcher. Set your name and city in Edit, then disable TV debugging.",
  "guide": "Detailed setup and recovery steps",
  "promptTitle": "Setup prompt",
  "promptBody": "Copy this into Codex or Claude Code. It includes the repository URL and the full setup, including AirPlay.",
  "promptLabel": "Send to Codex or Claude Code",
  "copy": "Copy prompt",
  "beforeTitle": "Questions",
  "compatTitle": "Will it work on my TV?",
  "compatBody": "Requires Android 11 or newer. Developed for Philips 65PUS7906/62. HDMI, tuner and home selection may vary on other models. The app interface is Turkish.",
  "airTitle": "How is AirPlay added?",
  "airBody": "A separate receiver is installed on the TV. The prompt prepares the receiver, permissions and return-to-home settings together. Your iPhone or Mac and TV must be on the same network.",
  "privacyTitle": "What information is used?",
  "privacyBody": "Your name and preferences stay on the TV. Weather requests send your selected city coordinates to Open-Meteo. The speed test uses Cloudflare and downloads about 25 MB.",
  "restoreTitle": "Can I go back to my old home screen?",
  "restoreBody": "Yes. Select your previous home screen in TV settings. The guide also includes ADB recovery steps. Uninstalling the launcher removes its saved preferences.",
  "airplayStoryTitle": "Mirror your Mac or iPhone<br> on the TV.",
  "airplayStoryBody": "The Philips 65PUS7906/62 has no built-in AirPlay support. This setup adds a receiver so you can mirror your Mac or iPhone screen wirelessly.",
  "airplayStoryNote": "The setup prompt installs the receiver and prepares its permissions. Mirroring opens the receiver; stopping returns to the launcher.",
  "airplayGuide": "See AirPlay setup",
  "mirrorCaption": "Open Screen Mirroring in Control Center and select your TV.",
  "mirrorStatus": "Screen Mirroring · Makallete TV",
  "licenseLink": "MIT License",
  "weatherCredit": "Weather: Open-Meteo · CC BY 4.0",
  "shareStop": "Stop Mirroring",
  "shareWaiting": "Waiting for a connection",
  "shareStart": "Mirror to TV",
  "screenSources": "Screenshots:"
};
const prompts = {"tr": "Makallete TV Launcher’ı AirPlay desteğiyle birlikte Android TV’me kur.\n\nRepo: https://github.com/makallete/makallete-tv-launcher\n\nProjeyi bilgisayarımda kalıcı bir klasöre klonla. Repo zaten açıksa onu kullan; mevcut dosyalarımı değiştirmeden önce kontrol et. README.md, docs/KURULUM.md ve kaynak kodu oku. Bilgisayardaki komutları sen çalıştır; TV’de yapmam gerekenleri adım adım anlat.\n\n1. TV modelini, Android sürümünü ve mevcut araçları kontrol et. Android 11’den eskiyse kurulumu durdur. Birden fazla ADB cihazı varsa doğru TV’yi seç. Bağlantı iznini TV’de benim onaylamamı bekle.\n2. Bağlantı için resmi Platform-Tools’u kullan. İmzalı APK varsa GitHub Releases’ten al ve SHA-256 değerini doğrula. Kaynaktan derlemek gerekiyorsa JDK 17, Android SDK 34 ve Build-Tools 34.0.0 hazırla. İmza uyuşmazlığı çıkarsa çalışan uygulamayı kendiliğinden kaldırma.\n3. Mevcut ana ekranı geri getirebilmek için HOME bileşenini kaydet. Launcher’ı kur; kumanda, OK, uzun OK, uygulama açma, HDMI ve anten girişlerini kontrol et. Ardından varsayılan ana ekran olarak seçmeme yardım et. Sistem uygulamalarını kaldırma veya devre dışı bırakma.\n4. Düzenle bölümünden selamlama ve şehir ayarını yapmama yardım et. Hava durumu için şehir koordinatlarının Open-Meteo’ya gönderildiğini açıkla.\n5. AirPlay alıcısı io.github.jqssun.airplay’i resmi kaynağından kur. Zaten yüklüyse çalışan sürümü ve ayarlarını kontrol et. READ_LOGS, kullanım erişimi ve ekran üstüne çizme izinlerinin ne işe yaradığını anlatıp gerekli izinleri ayarla. Alıcıda launch_on_connect’i kapat, launcher’ın Düzenle bölümünden otomatik geçişi aç. AirPlay bu kurulumun parçası; ayrıca isteyip istemediğimi sorma.\n6. Mac veya iPhone’dan ekran yansıtmayı birlikte dene. Yansıtma başlayınca alıcının açıldığını, bitince launcher’a döndüğünü ve TV yeniden açıldığında sistemin çalıştığını kontrol et. Alıcı sürümüyle uyumsuzluk veya erişim sorunu çıkarsa nedenini ve yapılması gerekeni belirt.\n7. Sonunda eski ana ekrana dönme ve uygulamayı kaldırma adımlarını ver. ADB bağlantısını kapat; TV’de hata ayıklamayı kapatmamı hatırlat. Doğrulayamadığın adımları belirt. Kişisel bilgileri, ekran görüntülerini, şifreleri veya imza anahtarlarını Git’e ya da sohbet çıktısına koyma.\n", "en": "Install Makallete TV Launcher and AirPlay support on my Android TV.\n\nRepository: https://github.com/makallete/makallete-tv-launcher\n\nClone the project into a permanent folder on my computer. If the repository is already open, use it and check before changing existing files. Read README.en.md, docs/SETUP.md and the source. Run computer commands yourself and guide me through the TV actions step by step.\n\n1. Check the TV model, Android version and existing tools. Stop below Android 11. Select the correct TV if several ADB devices exist. Wait for me to authorize the connection on the TV.\n2. Use official Platform-Tools. Get the signed APK from GitHub Releases when available and verify its SHA-256. If building from source is necessary, prepare JDK 17, Android SDK 34 and Build-Tools 34.0.0. Do not automatically remove a working installation after a signature mismatch.\n3. Record the current HOME component for recovery. Install the launcher and check remote navigation, OK, long OK, app launch, HDMI and tuner inputs. Then help me select it as the default home. Do not remove or disable system apps.\n4. Help me set the greeting and city in Düzenle (Edit). Explain that weather requests send city coordinates to Open-Meteo.\n5. Install io.github.jqssun.airplay from its official source, or check its working version and settings if it is already installed. Explain log, usage and overlay access, then grant the permissions needed for handover. Disable launch_on_connect in the receiver and enable automatic handover in the launcher’s Edit screen. AirPlay is part of this installation; do not ask separately whether I want it.\n6. Test screen mirroring from a Mac or iPhone with me. Check that mirroring opens the receiver, stopping returns to the launcher, and the setup works after restarting the TV. Explain any receiver compatibility or access problem and the next action needed.\n7. Provide steps to restore the old home and uninstall the app. Disconnect ADB and remind me to disable TV debugging. State which checks you could not complete. Keep personal information, screenshots, passwords and signing keys out of Git and chat output.\n"};
const originals = new Map([...document.querySelectorAll('[data-i18n]')].map(node => [node.dataset.i18n, node.innerHTML]));
let language = 'tr';
function setLanguage(value) {
  language = value === 'en' ? 'en' : 'tr';
  document.documentElement.lang = language;
  document.querySelectorAll('.claude-official-shot').forEach(image => { image.alt = language === 'tr' ? 'Anthropic’in yayımladığı Claude Code terminal görüntüsü' : 'Claude Code terminal screenshot published by Anthropic'; });
  document.querySelectorAll('.ios-official-shot').forEach(image => { image.alt = language === 'tr' ? 'Apple’ın iOS 27 kılavuzundaki iPhone ana ekranı' : 'iPhone Home Screen from Apple’s iOS 27 User Guide'; });
  document.querySelectorAll('[data-i18n]').forEach(node => { node.innerHTML = language === 'en' ? english[node.dataset.i18n] : originals.get(node.dataset.i18n); });
  document.querySelectorAll('[data-lang]').forEach(button => button.setAttribute('aria-pressed', String(button.dataset.lang === language)));
  document.getElementById('setup-prompt').value = prompts[language];
  document.getElementById('guide').href = 'https://github.com/makallete/makallete-tv-launcher/blob/main/docs/' + (language === 'tr' ? 'KURULUM.md' : 'SETUP.md');
  document.getElementById('copy-status').textContent = '';
  document.querySelector('meta[name="description"]').content = language === 'tr' ? 'Android TV için sade, reklamsız bir ana ekran. Makallete TV Launcher kurulum rehberi.' : 'A simple, ad-free home screen for Android TV. Step-by-step setup with Codex or Claude Code.';
  document.title = 'Makallete TV Launcher — ' + (language === 'tr' ? 'Kurulum' : 'Setup');
  document.getElementById('demo-title').textContent = selectedApp;
  document.getElementById('airplay-guide').href = document.getElementById('guide').href;
  document.querySelector('.app-row').setAttribute('aria-label', language === 'tr' ? 'TV uygulamaları' : 'TV apps');
  document.querySelector('.mirror-controls').setAttribute('aria-label', language === 'tr' ? 'Yansıtılacak cihaz' : 'Device to mirror');
  updateTurkeyClock();
  try { localStorage.setItem('language', language); } catch (_) {}
}
document.querySelectorAll('[data-lang]').forEach(button => button.addEventListener('click', () => setLanguage(button.dataset.lang)));
document.getElementById('copy').addEventListener('click', async () => {
  const prompt = document.getElementById('setup-prompt');
  try {
    await navigator.clipboard.writeText(prompt.value);
    document.getElementById('copy-status').textContent = language === 'tr' ? 'Kopyalandı. Codex veya Claude Code’a yapıştırabilirsin.' : 'Copied. Paste it into the chat where you opened the project.';
  } catch (_) {
    prompt.focus(); prompt.select();
    document.getElementById('copy-status').textContent = language === 'tr' ? 'Kopyalanamadı. Seçili metni Ctrl+C veya ⌘C ile kopyalayabilirsin.' : 'Automatic copying failed. Copy the selected text with Ctrl+C or ⌘C.';
  }
});
let selectedApp = 'Netflix';
document.querySelectorAll('[data-app]').forEach(button => {
  function selectApp() {
    selectedApp = button.dataset.app;
    document.querySelectorAll('[data-app]').forEach(other => { other.classList.toggle('selected', other === button); other.setAttribute('aria-pressed', String(other === button)); });
    document.getElementById('demo-title').textContent = selectedApp;
    document.getElementById('room-scene').dataset.tone = button.dataset.tone;
  }
  button.addEventListener('click', selectApp);
  button.addEventListener('focus', selectApp);
  button.addEventListener('keydown', event => {
    if (!['ArrowLeft', 'ArrowRight'].includes(event.key)) return;
    event.preventDefault();
    const buttons = [...document.querySelectorAll('[data-app]')];
    buttons[Math.max(0, Math.min(buttons.length - 1, buttons.indexOf(button) + (event.key === 'ArrowRight' ? 1 : -1)))].focus();
  });
});
let initial = new URLSearchParams(location.search).get('lang');
if (!['tr','en'].includes(initial)) { try { initial = localStorage.getItem('language'); } catch (_) {} }
setLanguage(initial || 'tr');

// The timezone stays in Türkiye even when the visitor is in another country.
function updateTurkeyClock() {
  const now = new Date();
  const locale = language === 'tr' ? 'tr-TR' : 'en-GB';
  const zone = {timeZone: 'Europe/Istanbul'};
  const clock = document.getElementById('turkey-clock');
  clock.textContent = new Intl.DateTimeFormat(locale, {...zone, hour:'2-digit', minute:'2-digit', hourCycle:'h23'}).format(now);
  document.querySelectorAll('.mirror-clock').forEach(element => { element.textContent = clock.textContent; });
  document.querySelectorAll('.mirror-day').forEach(element => { element.textContent = new Intl.DateTimeFormat(locale, {...zone, day:'numeric'}).format(now); });
  clock.dateTime = now.toISOString();
  clock.setAttribute('aria-label', language === 'tr' ? 'Güncel Türkiye saati' : 'Current time in Türkiye');
  document.getElementById('turkey-date').textContent = new Intl.DateTimeFormat(locale, {...zone, weekday:'short', day:'numeric', month:'short'}).format(now);
  const hour = Number(new Intl.DateTimeFormat('en-GB', {...zone, hour:'2-digit', hourCycle:'h23'}).format(now));
  const period = hour >= 5 && hour < 12 ? 0 : hour < 18 && hour >= 12 ? 1 : hour < 23 && hour >= 18 ? 2 : 3;
  const greetings = language === 'tr' ? ['Günaydın', 'İyi günler', 'İyi akşamlar', 'İyi geceler'] : ['Good morning', 'Good afternoon', 'Good evening', 'Good night'];
  document.getElementById('demo-greeting').textContent = greetings[period];
}
setInterval(updateTurkeyClock, 1000);
document.addEventListener('visibilitychange', updateTurkeyClock);

// The controls follow Apple's current Control Center / Screen Mirroring flow.
const mirrorTabs = [...document.querySelectorAll('[data-mirror-device]')];
let mirroredDevice = 'mac';
let mirroringActive = true;
function renderMirroring() {
  const demo = document.getElementById('airplay-demo');
  demo.dataset.device = mirroredDevice;
  demo.dataset.mirroring = String(mirroringActive);
  mirrorTabs.forEach(tab => { const selected = tab.dataset.mirrorDevice === mirroredDevice; tab.setAttribute('aria-selected', String(selected)); tab.tabIndex = selected ? 0 : -1; });
  const mac = document.getElementById('source-mac');
  const phone = document.getElementById('source-iphone');
  mac.hidden = mirroredDevice !== 'mac'; phone.hidden = mirroredDevice !== 'iphone';
  const source = mirroredDevice === 'mac' ? mac : phone;
  document.getElementById('mirror-tv-content').innerHTML = source.querySelector('.share-content').outerHTML;
  document.querySelectorAll('[data-start-mirroring]').forEach(button => { button.setAttribute('aria-pressed', String(mirroringActive)); button.disabled = mirroringActive; });
  document.querySelectorAll('[data-stop-mirroring]').forEach(button => { button.disabled = !mirroringActive; });
}
mirrorTabs.forEach((tab, index) => {
  tab.addEventListener('click', () => { mirroredDevice = tab.dataset.mirrorDevice; mirroringActive = true; renderMirroring(); });
  tab.addEventListener('keydown', event => {
    if (!['ArrowLeft','ArrowRight'].includes(event.key)) return;
    event.preventDefault(); const next = mirrorTabs[1 - index]; next.focus(); mirroredDevice = next.dataset.mirrorDevice; mirroringActive = true; renderMirroring();
  });
});
document.querySelectorAll('[data-start-mirroring]').forEach(button => button.addEventListener('click', () => { mirroringActive = true; renderMirroring(); }));
document.querySelectorAll('[data-stop-mirroring]').forEach(button => button.addEventListener('click', () => { mirroringActive = false; renderMirroring(); }));
