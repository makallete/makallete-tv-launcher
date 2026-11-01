# Install step by step

Requires Android TV running Android 11 / API 30 or newer. The original app was tested on Philips 65PUS7906/62. Other models are unverified. No root is needed. Do not copy the source ZIP to your TV.

## 1. Check the TV

Open Settings → Device Preferences / System → About. Note the model and Android version. Keep the original home app installed and enabled throughout setup.

## 2. Download the computer tools

Download [Google SDK Platform-Tools](https://developer.android.com/tools/releases/platform-tools) for your operating system. Create `Desktop/tv-setup`, extract the ZIP there, and open a terminal inside its `platform-tools` folder. You should see `adb` or `adb.exe`.

Commands below use macOS/Linux `./adb`. In Windows PowerShell use `.\adb.exe` instead. If adb is already on PATH, `adb` works directly.

## 3. Get the APK

If the repository's Releases page contains the signed `makallete-tv-launcher-0.1.0-alpha.apk`, download it into `platform-tools`. If there is no APK:

1. Choose Code → Download ZIP on GitHub and extract it under `Desktop/tv-setup`. Find the project root containing `gradlew` and `README.md`.
2. Install [Android Studio](https://developer.android.com/studio), Open that root folder, then use SDK Manager to install SDK Platform 34 and Build-Tools 34.0.0. Select JDK 17 as the Gradle JDK; use the IDE's Download JDK option if necessary.
3. In the IDE terminal run `./gradlew assembleDebug` (Windows: `.\gradlew.bat assembleDebug`).
4. Copy `app/build/outputs/apk/debug/app-debug.apk` into `platform-tools`. Use that filename in the install command below. Its debug signature differs from a signed release.

## 4. Connect the TV

In About, press Build seven times. Enable USB / network debugging in Developer options. Menus vary by vendor. Put the computer and TV on the same trusted local network and find the TV's IP under Network settings. Do not forward debugging ports to the internet.

For network debugging that advertises port 5555, replace `TV_IP` with your TV address:

```sh
./adb connect TV_IP:5555
./adb devices
```

Accept the RSA authorization prompt on the TV. The device must appear as `device`, not `unauthorized`. If the TV offers paired Wireless debugging, use `./adb pair TV_IP:PAIR_PORT`, then `./adb connect TV_IP:CONNECT_PORT` with the ports displayed on the TV. If network debugging is unavailable, follow the vendor's supported USB development connection instructions.

If several devices are listed, select the intended TV explicitly using `./adb -s TV_IP:PORT ...` in every command. Resolve connection problems before continuing.

## 5. Install and try the app

```sh
./adb install -r makallete-tv-launcher-0.1.0-alpha.apk
./adb shell am start -n tv.makallete.launcher/.MainActivity
```

If you built from source, replace the filename with `app-debug.apk`. `INSTALL_FAILED_UPDATE_INCOMPATIBLE` means a signature mismatch: do not automatically uninstall the working version, as that deletes preferences. Check that the original home app works before deciding how to migrate.

Test arrows, OK, long OK, HDMI and tuner inputs. **Düzenle** means Edit. There you can enter an optional greeting and city. A blank city disables weather. Saving a new city searches Open-Meteo and lets you pick the correct result; this requires internet access. The UI is currently Turkish.

## 6. Choose the default home

Record the current home component first:

```sh
./adb shell cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME
```

Press Home and choose Makallete TV Launcher / Always if offered. If no chooser appears:

```sh
./adb shell cmd package set-home-activity tv.makallete.launcher/.MainActivity
```

If your vendor blocks this, stop and investigate your model. Some Philips TVs force the vendor launcher back to the foreground. This general guide never disables system packages; no debloat tools are needed or included.

## 7. Install the AirPlay receiver and automatic handover

The setup prompt completes this step alongside the launcher installation. The launcher is not an AirPlay receiver. Download `io.github.jqssun.airplay` from [F-Droid](https://f-droid.org/packages/io.github.jqssun.airplay/). The original integration used 0.0.36; later log formats are unverified. Save the APK as `airplay.apk` inside `platform-tools`:

```sh
./adb install -r airplay.apk
./adb shell appops set io.github.jqssun.airplay SYSTEM_ALERT_WINDOW allow
./adb shell am broadcast -a io.github.jqssun.airplay.SET_SETTING -n io.github.jqssun.airplay/.SettingsReceiver --es key launch_on_connect --es value false
./adb shell am broadcast -a io.github.jqssun.airplay.SET_SETTING -n io.github.jqssun.airplay/.SettingsReceiver --es key server_name --es value 'Living Room TV'
./adb shell pm grant tv.makallete.launcher android.permission.READ_LOGS
./adb shell appops set tv.makallete.launcher GET_USAGE_STATS allow
./adb shell appops set tv.makallete.launcher SYSTEM_ALERT_WINDOW allow
```

`Living Room TV` is an example; choose your own name. Launch the receiver once to finish its setup. Open launcher → **Düzenle**, enable the AirPlay checkbox and save. The watcher only starts if the receiver, log access, usage access and overlay permissions are available. Test mirroring start and stop with the sender and TV on the same network. Readiness after boot can be delayed.

Log access reads device logs; usage access identifies the foreground app; overlay access draws the transition cover. These permissions are not needed for the basic launcher. You can open the receiver manually without automatic handover. Uncheck the option to stop handover; to revoke launcher permissions:

```sh
./adb shell pm revoke tv.makallete.launcher android.permission.READ_LOGS
./adb shell appops set tv.makallete.launcher GET_USAGE_STATS default
./adb shell appops set tv.makallete.launcher SYSTEM_ALERT_WINDOW default
```

## 8. Verify and recover

Check Home, app launch, long OK, inputs, restart and AirPlay. The speed test can download about 25 MB. Memory cleanup depends on Android restrictions.

To restore the old home, select it in the TV's default apps settings, or run `./adb shell cmd package set-home-activity OLD_PACKAGE/OLD_ACTIVITY` with the component you recorded. If the original Philips launcher had previously been disabled, `./adb shell pm enable com.google.android.tvlauncher` re-enables it on models using that package. Once the old home works, `./adb uninstall tv.makallete.launcher` removes this launcher and its preferences. The separate AirPlay receiver stays installed.

Finish with `./adb disconnect` and disable TV debugging. See the official [ADB guide](https://developer.android.com/tools/adb) and your vendor's documentation for device-specific details.
