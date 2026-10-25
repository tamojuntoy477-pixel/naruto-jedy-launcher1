# Ninja Shinobi: Legacy Launcher

Android companion launcher for Minecraft Java Edition on mobile.

## Features in this build
- New dark ninja-themed interface with purple and chakra-cyan accents
- Detects whether MJ Launcher is installed (where Android package visibility allows)
- Opens the installed MJ Launcher or its Play Store page
- Stores a Minecraft Java server address locally and can copy it
- Stores a local display nickname (does not change Microsoft account identity)
- Includes a practical guide for installing Java mods

## Important limits
This is a companion app, not a replacement Minecraft runtime. It does not bundle Minecraft, authenticate Microsoft accounts, automatically download/install mods, or guarantee Naruto Jedy compatibility. Use a Java mod that matches the Minecraft version and mod loader configured in MJ Launcher. Bedrock add-ons do not become Java mods by renaming files.

## Build APK
GitHub Actions runs the Android build workflow after a push. Open the repository's **Actions** tab, select the latest successful run, and download its APK artifact. A successful CI run is required before calling the APK tested.

## Project
- Android application ID: `com.narutojedy.launcher` (kept to preserve update compatibility with the existing app)
- Version: 0.2.0
- minSdk: 23
- compileSdk / targetSdk: 35
