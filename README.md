# Namma Mistri Android App

This workspace contains a native Android Studio project for **Namma Mistri**, built with Kotlin and Java.

## What is included

- Kotlin Android UI in `app/src/main/java/com/nammamistri/app/MainActivity.kt`
- Java material calculation logic in `app/src/main/java/com/nammamistri/app/calculation/`
- Dashboard, wall calculator, labour diary, and local material rates screens
- English and Kannada app labels
- No third-party runtime libraries, so the project is easy to open in Android Studio

## Open in Android Studio

1. Open Android Studio.
2. Select **Open**.
3. Choose this folder: `C:\Users\Shaik Waheed\OneDrive\Desktop\new intern`
4. Let Gradle sync.
5. Run the `app` configuration on an emulator or Android phone.

## Build

From Android Studio, use **Build > Make Project** or **Build > Build Bundle(s) / APK(s) > Build APK(s)**.

If you have Java and Gradle available on your PATH, you can also run:

```bash
gradle assembleDebug
```

The debug APK will be generated under `app/build/outputs/apk/debug/`.
