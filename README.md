# Makallete TV Launcher

[English](README.en.md) · [Kurulum](docs/KURULUM.md) · [Installation](docs/SETUP.md)

Android TV için hafif, reklamsız bir ana ekran. Uygulamaların, HDMI girişlerin ve ihtiyacın olan birkaç araç aynı yerde. Kotlin ve Android'in kendi View sistemiyle yazıldı; uygulamada harici kütüphane yok.

**Alpha:** Özgün sürüm Philips 65PUS7906/62 / Android 11 üzerinde kullanıldı. Bu paylaşım sürümünün derleme ve statik kontrolleri yayın notunda belirtilir; diğer televizyonlarla uyumluluk garantisi yok. Uygulamanın arayüzü şu an Türkçe. Site ve kurulum rehberi iki dilli.

## Neler yapıyor?

- Kurulu TV uygulamalarını afişleriyle otomatik listeler. Kumandayla gezin, OK ile açın; uzun OK uygulama bilgisini açar.
- Seçili uygulamanın rengini arka plana taşır. Saat, tarih ve isteğe bağlı kişisel selamlama gösterir.
- HDMI/CEC cihazlarını ve varsa anten girişini gösterir. Destekleyen Philips TV'lerde Ambilight paneline ulaşır.
- Seçtiğiniz şehir için Open-Meteo hava durumu, elle başlatılan Cloudflare hız testi ve bellek araçları sunar.
- İsteğe bağlı, **ayrı kurulan** AirPlay alıcısıyla yansıtma başlayınca alıcıya, bitince ana ekrana geçebilir.

İlk açılışta isim yoktur, hava durumu ve AirPlay bekçisi kapalıdır. Üstteki **Düzenle** düğmesinden adınızı ve şehrinizi seçebilirsiniz. Bu bilgiler yalnızca TV'de saklanır. Hava durumu için konum izni veya hesap gerekmez; şehir araması ve seçilen koordinatlar Open-Meteo'ya gönderilir.

## İlk kez kuruyorsanız

1. [Kurulum rehberini](docs/KURULUM.md) açın. İndirilecek araçlar, klasörler ve TV'de yapılacaklar numaralı anlatılır.
2. Hazır imzalı APK yayımlanmışsa bu reponun **Releases** bölümünden indirin. Yoksa kaynak kodu Android Studio ile derleyin. Kaynak ZIP bir APK değildir ve doğrudan TV'ye kurulmaz.
3. Codex veya Claude Code kullanıyorsanız [hazır promptu](docs/INSTALL_PROMPT.tr.txt) proje klasörüyle birlikte verin.

## Geliştirme

JDK 17, Android SDK platform 34 / build-tools 34.0.0 gerekir. Android Studio'da bu kök klasörü açın. SDK Manager'dan ilgili paketleri yükleyin. SDK yolu IDE tarafından `local.properties` dosyasına yazılır; bu dosyayı paylaşmayın.

```sh
./gradlew assembleDebug lintDebug
```

Windows: `gradlew.bat assembleDebug lintDebug`. Çıktı: `app/build/outputs/apk/debug/app-debug.apk`. Debug APK yalnızca geliştirme içindir. [Release imzalama](docs/RELEASING.md) anahtarı repo dışında tutulur; release artık debug anahtarıyla imzalanmaz.

## Sınırlar

Android 11 (API 30) altı desteklenmez. HDMI, anten ve Ambilight üreticiye bağlıdır. Anten için Philips DTV kimliği tercih edilir, yoksa ilk tuner kullanılır. Uygulama sırası ve bazı gizlenen sistem paketleri hâlâ koddaki listelerle belirlenir. Bellek temizleme sonucu Android sürümüne bağlıdır; güncel Android sürümlerinde başka uygulamaları kapatma kısıtlıdır. AirPlay alıcısının log satırları değişirse otomatik geçiş bozulabilir. Açılışta alıcının hazır olması gecikebilir. Uygulama güncel Play Store hedef API şartları için hazırlanmış değildir; ADB ile kurulum içindir.

## Site

`site/` yalnızca statik dosyalardan oluşur; `netlify.toml` yayın klasörünü tanımlar; `python3 scripts/package-source.py` indirilebilir kaynak ZIP’ini üretir. Yerelde `python3 -m http.server 8080 --directory site` çalıştırıp `http://localhost:8080` açın. Önizleme gerçek uygulama markalarını ve İstanbul örnek şehrini kullanır. Saat ve tarih canlı Türkiye saatidir; hava ve cihaz ölçümleri örnektir. Philips 65PUS7906/62 referanslı oda görseli AI ile hazırlanmıştır; gerçek TV ekran görüntüsü değildir.

## Lisans

Önerilen ve bu pakete eklenen lisans **MIT**: launcher ve site kodunun yeniden kullanılmasını kolaylaştırır. Fontlar kendi OFL lisanslarıyla gelir; isteğe bağlı AirPlay alıcısı ayrı GPL-3.0 projesidir ve pakete dahil değildir. Ayrıntılar: [LICENSE](LICENSE), [üçüncü taraf bildirimleri](THIRD_PARTY_NOTICES.md).

Developed by makallete
