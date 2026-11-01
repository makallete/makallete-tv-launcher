# TV'ye adım adım kurulum

Bu rehber Android 11 ve üzeri Android TV içindir. Özgün uygulama Philips 65PUS7906/62 üzerinde denenmiştir. Diğer modellerde önce uygulamayı açıp kumandayı ve kaynak geçişlerini kontrol edin. Kurulum bilgisayardan yapılır; kaynak ZIP'i TV'ye kopyalamayın. Root gerekmez.

## 1. TV bilgilerini ve mevcut ana ekranı not edin

TV'de Ayarlar → Cihaz tercihleri / Sistem → Hakkında bölümünden model ve Android sürümüne bakın. Android 11'den eskiyse bu APK'yı kullanmayın. Mevcut ana ekranı kaldırmayın veya devre dışı bırakmayın.

## 2. Bilgisayara bağlantı aracını indirin

[Google SDK Platform-Tools](https://developer.android.com/tools/releases/platform-tools) sayfasından kendi sisteminizin ZIP'ini indirin. Bilgisayarınızda `Masaüstü/tv-kurulum` adlı bir klasör açın ve ZIP içindeki `platform-tools` klasörünü buraya çıkarın. İçinde `adb` (Windows'ta `adb.exe`) bulunmalı.

Terminal'i, Windows'ta PowerShell'i bu `platform-tools` klasöründe açın. Aşağıdaki komutlarda macOS/Linux için `./adb`, PowerShell için `.\adb.exe` kullanın. (`adb` PATH'e ekliyse doğrudan `adb` yeterlidir.)

## 3. Uygulama dosyasını hazırlayın

Reponun Releases bölümünde imzalı `makallete-tv-launcher-0.1.0-alpha.apk` varsa indirin ve `platform-tools` klasörüne koyun. APK yoksa:

1. GitHub'da Code → Download ZIP ile kaynak kodu indirin. ZIP'i `Masaüstü/tv-kurulum` içine açın; `gradlew` ve `README.md` dosyalarının olduğu kökü kullanın.
2. [Android Studio](https://developer.android.com/studio) indirin. Open ile bu proje kökünü açın. SDK Manager'dan Android SDK Platform 34 ve Build-Tools 34.0.0 kurun. Gradle JDK olarak JDK 17 seçin; gerekirse IDE'deki Download JDK seçeneğini kullanın.
3. Android Studio Terminal'inde macOS/Linux için `./gradlew assembleDebug`, Windows için `.\gradlew.bat assembleDebug` çalıştırın.
4. `app/build/outputs/apk/debug/app-debug.apk` dosyasını `platform-tools` klasörüne kopyalayın. Sonraki kurulum komutunda bu dosya adını kullanın. Bu debug sürümüdür; resmi release ile imzası farklıdır.

## 4. TV'de hata ayıklamayı açın

Hakkında ekranında Derleme / Build satırına yedi kez basın. Geliştirici seçeneklerinden USB / Ağ hata ayıklamayı açın. Menü isimleri modele göre değişir. Bilgisayar ve TV aynı güvendiğiniz yerel ağda olsun. TV'nin Ağ → IP adresi bilgisini not edin; internete port açmayın.

Ağ hata ayıklama `IP:5555` sunuyorsa, `TV_IP` yerine kendi TV adresinizi yazarak çalıştırın:

```sh
./adb connect TV_IP:5555
./adb devices
```

TV'de bilgisayarın RSA bağlantı isteğini onaylayın; listede `device` görünmeli. `unauthorized` varsa TV'deki onayı kontrol edin. Yeni eşleştirmeli kablosuz hata ayıklama menüsü varsa TV'nin verdiği eşleştirme portuyla `./adb pair TV_IP:PAIR_PORT`, ardından bağlantı portuyla `./adb connect TV_IP:CONNECT_PORT` kullanın. Ağ bağlantısı desteklenmiyorsa cihazın resmi USB geliştirme bağlantısını kullanın; rastgele bir porta kablo takmayın.

Birden fazla cihaz görünüyorsa tüm komutlarda `./adb -s TV_IP:PORT ...` kullanın. Bağlantı olmadan sonraki adıma geçmeyin.

## 5. Kurun ve ilk kez açın

```sh
./adb install -r makallete-tv-launcher-0.1.0-alpha.apk
./adb shell am start -n tv.makallete.launcher/.MainActivity
```

Kaynak koddan derlediyseniz ilk komuttaki dosya adını `app-debug.apk` yapın. `INSTALL_FAILED_UPDATE_INCOMPATIBLE` farklı imza demektir. Çalışan sürümü otomatik kaldırmayın; kaldırmak yerel ayarları siler. Önce mevcut ana ekranın hâlâ çalıştığını doğrulayın ve imza geçişine karar verin.

Uygulamada ok tuşları, OK, uzun OK ve HDMI/Anten girişlerini deneyin. **Düzenle** düğmesinden isterseniz ad ve şehir seçin. Boş şehir hava durumunu kapatır. Kaydet dediğinizde bulunan şehirlerden doğru olanı seçin. Şehir araması internet ister. Hava durumu ilk açılışta boşsa varsayılan olarak kapalıdır.

## 6. Ana ekran olarak seçin

Önce mevcut ana ekranın paket adını kaydedin:

```sh
./adb shell cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME
```

Ardından Home'a basıp Makallete TV Launcher'ı ve varsa Her zaman seçeneğini seçin. Seçici çıkmıyorsa:

```sh
./adb shell cmd package set-home-activity tv.makallete.launcher/.MainActivity
```

Üretici izin vermiyorsa burada durup modele uygun yöntemi araştırın. Bazı Philips modelleri kendi launcher'ını zorla açar. Bu genel rehber hiçbir sistem paketini kapatmaz; `tv-debloat` gerekmez ve projeye dahil değildir.

## 7. AirPlay alıcısını ve otomatik geçişi kurun

Kurulum prompt’u bu adımı launcher kurulumu ile birlikte tamamlar. Launcher tek başına AirPlay alıcısı değildir. [F-Droid](https://f-droid.org/packages/io.github.jqssun.airplay/) üzerinden `io.github.jqssun.airplay` alıcısını indirin. Özgün projede 0.0.36 kullanıldı; daha yeni sürümlerin log uyumluluğu doğrulanmadı. APK'yı `platform-tools` içine `airplay.apk` adıyla koyun ve kurun:

```sh
./adb install -r airplay.apk
./adb shell appops set io.github.jqssun.airplay SYSTEM_ALERT_WINDOW allow
./adb shell am broadcast -a io.github.jqssun.airplay.SET_SETTING -n io.github.jqssun.airplay/.SettingsReceiver --es key launch_on_connect --es value false
./adb shell am broadcast -a io.github.jqssun.airplay.SET_SETTING -n io.github.jqssun.airplay/.SettingsReceiver --es key server_name --es value 'Living Room TV'
./adb shell pm grant tv.makallete.launcher android.permission.READ_LOGS
./adb shell appops set tv.makallete.launcher GET_USAGE_STATS allow
./adb shell appops set tv.makallete.launcher SYSTEM_ALERT_WINDOW allow
```

`Living Room TV` örnek addır; istediğiniz adı kullanın. Alıcıyı bir kez açıp kendi kurulumunu tamamlayın. Launcher → **Düzenle** → AirPlay otomatik geçişini işaretleyip Kaydet seçin. Bekçi ancak alıcı ve gerekli üç izin mevcutsa başlar. Yansıtmanın başlayıp bitmesini deneyin. iPhone/Mac ve TV aynı ağda olmalı. Alıcının çalışması TV açıldıktan sonra gecikebilir.

READ_LOGS cihaz kayıtlarına, kullanım izni öndeki uygulama bilgisine, overlay izni geçiş perdesine erişim sağlar. Bu izinler temel launcher için gerekli değildir. Açmak istemiyorsanız AirPlay düğmesinden alıcıyı elle kullanabilirsiniz. Otomatik geçişi kapatmak için Düzenle'deki işareti kaldırın. İzinleri geri almak için:

```sh
./adb shell pm revoke tv.makallete.launcher android.permission.READ_LOGS
./adb shell appops set tv.makallete.launcher GET_USAGE_STATS default
./adb shell appops set tv.makallete.launcher SYSTEM_ALERT_WINDOW default
```

## 8. Bitirirken ve geri alma

Home, uygulama açma, uzun OK, girişler, yeniden açılış ve AirPlay'i kontrol edin. Hız testi yaklaşık 25 MB indirebilir. Bellek temizleme sonucu TV'nin Android sürümüne bağlıdır.

Eski ana ekrana dönmek için TV'nin varsayılan uygulamalar menüsünden eski launcher'ı seçin veya kaydettiğiniz bileşen adıyla `./adb shell cmd package set-home-activity ESKI_PAKET/ESKI_ACTIVITY` çalıştırın. Önceki Philips ana ekranı daha önce kapatılmışsa `./adb shell pm enable com.google.android.tvlauncher` yeniden etkinleştirir; bu yalnızca o paketi kullanan TV'ler içindir. Eski Home çalıştıktan sonra isterseniz `./adb uninstall tv.makallete.launcher` ile launcher'ı kaldırın; yerel ayarlar silinir.

İşiniz bitince `./adb disconnect` çalıştırın ve TV'deki hata ayıklamayı kapatın. AirPlay alıcısı ayrı uygulama olduğu için launcher kaldırılınca otomatik silinmez.

Resmi başvuru: [ADB rehberi](https://developer.android.com/tools/adb). Cihaz menülerinde üreticinizin kılavuzunu esas alın.
