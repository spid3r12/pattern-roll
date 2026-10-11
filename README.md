# Pattern Roll for Android

A small Android app that shows the Pattern Roll page full screen. Everything is inside the app, so it works offline. Saving a picture or animation puts it in the phone gallery, in the folder Pictures/PatternRoll.

Needs Android 10 or newer (API 29).

This project has not been compiled yet by its author. The first build may need a small fix. See "If the build fails" below.

## Easiest way to get the APK: GitHub (no installs)

1. Make a free account at github.com and create a new empty repository.
2. Upload the contents of this folder to it. Make sure the hidden `.github` folder goes too. Uploading the unzipped folder by drag and drop works.
3. Open the repository, go to Actions, choose "Build APK", press "Run workflow".
4. After a few minutes open the finished run and download the "PatternRoll-apk" file at the bottom. Unzip it to get `app-debug.apk`.
5. Send `app-debug.apk` to the phone and open it. Android will ask you to allow installs from that source once.

## Other way: Android Studio

1. Install Android Studio on a computer.
2. Open this folder as a project and wait for it to finish syncing.
3. Build menu, Build APK(s). Or plug in a phone with USB debugging and press Run.
4. The APK ends up in `app/build/outputs/apk/debug/app-debug.apk`.

## Notes

- This is a debug-signed APK. It installs on your own phones. Putting it on Google Play needs a proper release key and a Play developer account.
- The page is in `app/src/main/assets/index.html`. The saving bridge is in `MainActivity.java`.

## If the build fails

Copy the red error text and send it to Claude. Version numbers (Android Gradle Plugin 8.5.2, Gradle 8.9, SDK 34) are the most likely place for a first-build problem.
