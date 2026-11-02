# 0.1.0-alpha

Android TV için reklamsız bir ana ekran. Android 11 ve üzeri için ilk alpha sürümü.

- Afişli uygulama listesi, kumandayla gezinme ve uygulamaya göre değişen arka plan rengi.
- HDMI/CEC ve anten girişleri; desteklenen Philips modellerinde Ambilight ayarları.
- Saat, tarih, kişisel selamlama ve şehir seçimiyle hava durumu.
- Hız testi ve bellek araçları.
- Ayrı AirPlay alıcısıyla otomatik geçiş ve yansıtma sonunda launcher’a dönüş.
- Türkçe uygulama arayüzü; Türkçe ve İngilizce kurulum rehberleri.

## Uyumluluk

Proje Philips 65PUS7906/62 ve Android 11 için geliştirildi. Bu APK sürümü fiziksel TV’de doğrulanmadı; diğer modellerde HDMI, anten, Ambilight ve AirPlay davranışı değişebilir. Kurulum ve eski ana ekrana dönüş adımları kurulum rehberinde bulunur.

## Derleme doğrulaması

JDK 17 ve Android SDK 34 ile `assembleDebug lintDebug assembleRelease lintRelease` kontrolleri geçti. Lint uyarıları View constructor’ları, Türkçe arayüz metinleri, çizim sırasında nesne oluşturma, TV ekran yönü ve banner boyutunu kapsar. Minimum ve hedef API 30’dur. ADB ile kurulum için dağıtılır; Play Store yayını için hazırlanmadığından `ExpiredTargetSdkVersion` kontrolü devre dışıdır.

İmzalı APK, Android SDK apksigner ile doğrulandı. İmza sertifikasının SHA-256 değeri:

`fa4a021c1069a04bcb48a3dd424de07e7ccc4f4bd5b7d480e31cc82f525ac051`
