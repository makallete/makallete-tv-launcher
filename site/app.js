const english = {
  "skip": "Skip to content",
  "title": "Install your own<br> custom TV launcher.",
  "intro": "Your Netflix, your YouTube, your console. All on a home screen that feels like yours. Give your Android TV a simple, ad-free home.",
  "start": "Start setup",
  "source": "Download source ↓",
  "support": "Android 11 and newer · Open source · Alpha",
  "demoSpeed": "Speed test",
  "apps": "Apps <span>5</span>",
  "inputs": "Inputs",
  "antenna": "📡 Antenna",
  "caption": "Choose an app to change the Ambilight on the wall. Illustrative room based on the Philips 65PUS7906/62; live Türkiye time, example weather and device readings.",
  "featureTitle": "A little room to breathe.",
  "featureBody": "Installed TV apps appear with their own banners. The selected app brings its color to your home screen. Your PlayStation on HDMI and antenna TV sit just below.",
  "featureMore": "Personalize your greeting and weather. In this room preview, choosing an app also changes the wall lighting. On the actual TV, the Ambilight button opens Philips settings; the launcher does not directly control the wall light.",
  "setupTitle": "Onto your TV, step by step.",
  "setupIntro": "Keep your computer and TV on the same local network. Source files stay on your computer; only the APK is installed on the TV.",
  "step1": "Check your TV model and Android version.",
  "body1": "Open Settings → System / Device Preferences → About. Android 11 or newer is required. The original app was used on a Philips 65PUS7906/62; other models are unverified.",
  "step2": "Download the project and extract it on your computer.",
  "body2": "Extract the source ZIP into a tv-setup folder on your Desktop. Open the project root containing README.md and gradlew in Codex or Claude Code. Do not copy the whole folder to your TV.",
  "download": "Download project files ↓",
  "step3": "Prepare the connection and build tools.",
  "body3": "Extract Google Platform-Tools into tv-setup. Its platform-tools folder should contain adb. Building from source requires Android Studio, JDK 17 and Android SDK 34. The prompt below will guide you through these steps.",
  "tools": "Google Platform-Tools",
  "step4": "Enable TV debugging and approve the connection.",
  "body4": "In About, press Build seven times, then enable network / USB debugging in Developer options. Connect ADB to the TV’s IP address and approve the computer on the TV. Menus and connection methods vary by model.",
  "step5": "Send the prompt, install the APK, and try it first.",
  "body5": "Codex or Claude Code can build an APK from source or use a signed release if available. Test remote navigation, apps and inputs. Once they work, choose Makallete TV Launcher with the Home button. Keep the original home app installed.",
  "releases": "APK releases and release notes",
  "step6": "Make it your home screen.",
  "body6": "Use Düzenle (Edit) to add an optional name and city. AirPlay needs a separate receiver and additional permissions; it is optional. When finished, disconnect ADB and turn off debugging.",
  "guide": "Open commands and recovery steps",
  "promptTitle": "A ready-to-use setup prompt",
  "promptBody": "Open the downloaded project folder in Codex or Claude Code. Send this prompt so it can handle the computer work and guide you through the TV steps.",
  "promptLabel": "Send to Codex / Claude Code",
  "copy": "Copy prompt",
  "beforeTitle": "Before you install.",
  "compatTitle": "Will it work on my TV?",
  "compatBody": "Requires Android 11 or newer. The first device was a Philips 65PUS7906/62; this public edition has not been verified on other TVs. HDMI, tuner, default home selection and Ambilight depend on your vendor. The app UI is currently Turkish; this site and the guides are bilingual.",
  "airTitle": "How does AirPlay work?",
  "airBody": "This Philips model has no built-in AirPlay receiver. Install io.github.jqssun.airplay separately to mirror an iPhone or Mac; it is not bundled with the launcher. Automatic handover needs ADB-granted log, usage and overlay permissions. The watcher follows the receiver’s log messages, so upstream changes can break it. Leave it off if you do not need it.",
  "privacyTitle": "What information is shared?",
  "privacyBody": "Greeting and city preferences stay on the TV. City searches and weather coordinates go to Open-Meteo. The speed test runs manually, contacts Cloudflare and may download about 25 MB. There is no developer backend or analytics. The website preview contains no real personal data.",
  "restoreTitle": "Can I go back to my old home screen?",
  "restoreBody": "Yes. Record the old HOME component during setup and keep the system launcher installed. Restore it through default app settings or the guide’s ADB command. Uninstalling deletes local preferences. APKs with different signatures cannot update each other; never automatically remove a working installation.",
  "licenseTitle": "Open source. Make it your own.",
  "licenseBody": "MIT is the suggested license included for the launcher and site. Fonts retain their OFL licenses. The separate AirPlay receiver has its own GPL-3.0 license. App order and hidden system packages can be changed in code; they do not have settings controls yet.",
  "airplayStoryTitle": "AirPlay wasn’t built in.<br> We added it.",
  "airplayStoryBody": "The Philips 65PUS7906/62 has no built-in AirPlay receiver. A separate open-source receiver lets you mirror an iPhone or Mac to the TV. The launcher opens the receiver when mirroring actually starts, then returns you home when it ends.",
  "airplayPath": "iPhone / Mac <span aria-hidden=\"true\">→</span> AirPlay receiver <span aria-hidden=\"true\">→</span> TV",
  "airplayStoryNote": "This is an optional extra setup. The receiver is not bundled with the launcher, and automatic handover needs additional permissions through ADB. See the setup guide for details.",
  "airplayGuide": "Open AirPlay setup"
};
const prompts = {"tr": "Bu klasördeki Makallete TV Launcher projesini Android TV'me kurmama yardım et.\n\nÖnce README.md, docs/KURULUM.md, docs/RELEASING.md ve kaynak kodu oku. Koddan öğrenebileceğin bilgileri tekrar sorma. Komutları sen çalıştır; TV'deki dokunuşları bana net ve numaralı anlat.\n\n1. Bilgisayarın araçlarını, TV modelini, Android sürümünü ve ADB bağlantısını salt okunur kontrollerle doğrula. Android 11'den eskiyse dur. Birden fazla cihaz varsa hedefi açıkça seç. Ağ hata ayıklamayı internete açma. TV'deki bağlantı onayını benim vermemi bekle.\n2. Resmi Platform-Tools'u ve gerekiyorsa JDK 17 / Android SDK 34 / build-tools 34.0.0 araçlarını hazırla. Hangi dosyayı indireceğimi, nereye çıkaracağımı ve nerede terminal açacağımı bilmediğimi varsay. Var olan araçları tekrar kurma.\n3. Hazır release APK varsa kaynağını ve SHA-256 değerini kontrol et. Yoksa bu kaynak koddan debug APK derle. Debug ve release imzalarının farklı olduğunu açıkla; çalışan kurulumu imza hatasında otomatik kaldırma. Anahtarları, şifreleri ve özel verileri Git'e veya sohbet çıktısına koyma.\n4. Önce mevcut HOME bileşenini kaydet. APK'yı kur ve uygulamayı aç; kumanda, OK, uzun OK, HDMI ve anteni benimle kontrol et. Sonra varsayılan ana ekranı seçmeme yardım et. Sistem uygulaması kaldırma veya devre dışı bırakma; bu proje debloat gerektirmez.\n5. İstersem Düzenle ekranından selamlama ve şehir ayarla. Şehir/hava isteklerinin Open-Meteo'ya gittiğini açıkla. Gerçek veri gelmiyorsa sahte değer gösterme.\n6. Temel launcher çalışınca AirPlay isteyip istemediğimi sor. İstersem ayrı io.github.jqssun.airplay alıcısını resmi kaynaktan kur, sürümü doğrula, log uyumluluğunun sürüme bağlı olduğunu anlat. READ_LOGS, kullanım erişimi ve overlay izinlerinin ne yaptığını açıklayıp yalnızca bu özellik için ver; Düzenle ekranındaki otomatik geçişi açtır. Yansıtma başlangıcı ve bitişini kontrol et.\n7. Home, uygulamalar, girişler ve yeniden açılışı doğrula. Ekran görüntüsü gerekiyorsa kişisel isim/konumu paylaşma. Bitirince geri alma komutlarını ver, ADB bağlantısını kapat ve TV'de hata ayıklamayı kapatmamı hatırlat. TV'ye erişemiyorsan başarı iddia etme.\n", "en": "Help me install Makallete TV Launcher from this folder on my Android TV.\n\nFirst read README.en.md, docs/SETUP.md, docs/RELEASING.md and the source. Do not ask me for facts you can learn from code. Run computer commands yourself and give me clear numbered instructions for actions on the TV.\n\n1. Inspect local tools, TV model, Android version and ADB connection without changing device state. Stop below Android 11. Explicitly select the TV if several devices exist. Never expose debugging ports to the internet. Wait for me to approve the TV's connection prompt.\n2. Prepare official Platform-Tools and, if needed, JDK 17, SDK platform 34 and build-tools 34.0.0. Explain exact downloads, extraction folders and where to open the terminal. Reuse existing tools.\n3. If a signed release APK exists, check its source and SHA-256. Otherwise build a debug APK from this source. Explain signature differences; never automatically uninstall a working version after an update signature error. Keep keys, passwords and private data out of Git and chat output.\n4. Record the current HOME component, install and launch the APK, then help me test the remote, OK, long OK, HDMI and tuner. Only then help select the default home. Never remove or disable system packages; debloating is not required.\n5. If I want, help me set the greeting and city in Düzenle (Edit). Explain that city/weather requests go to Open-Meteo. Never show invented readings as live data.\n6. Once the basic launcher works, ask whether I want AirPlay. If yes, obtain the separate io.github.jqssun.airplay receiver from its official source, verify its version and explain version-dependent log compatibility. Explain log, usage and overlay permissions before granting them only for this feature. Enable automatic handover in Edit and test mirroring start/stop.\n7. Verify Home, app launch, inputs and restart. Keep personal names/locations out of shared screenshots. Provide rollback commands, disconnect ADB and remind me to disable debugging. If you cannot reach the TV, do not claim device success.\n"};
const originals = new Map([...document.querySelectorAll('[data-i18n]')].map(node => [node.dataset.i18n, node.innerHTML]));
let language = 'tr';
function setLanguage(value) {
  language = value === 'en' ? 'en' : 'tr';
  document.documentElement.lang = language;
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
  updateTurkeyClock();
  try { localStorage.setItem('language', language); } catch (_) {}
}
document.querySelectorAll('[data-lang]').forEach(button => button.addEventListener('click', () => setLanguage(button.dataset.lang)));
document.getElementById('copy').addEventListener('click', async () => {
  const prompt = document.getElementById('setup-prompt');
  try {
    await navigator.clipboard.writeText(prompt.value);
    document.getElementById('copy-status').textContent = language === 'tr' ? 'Kopyalandı. Projeyi açtığın sohbete yapıştırabilirsin.' : 'Copied. Paste it into the chat where you opened the project.';
  } catch (_) {
    prompt.focus(); prompt.select();
    document.getElementById('copy-status').textContent = language === 'tr' ? 'Otomatik kopyalanamadı. Seçili metni Ctrl+C veya ⌘C ile kopyala.' : 'Automatic copying failed. Copy the selected text with Ctrl+C or ⌘C.';
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
