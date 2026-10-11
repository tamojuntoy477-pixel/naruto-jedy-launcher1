# Minecraft Mods Hub (Android starter project)

A small Android catalog prototype with search and Bedrock/Java filters. Mod entries open their source pages; downloads are not bundled into this app.

The app source is kept in a subfolder so it does not replace the launcher project in the repository root.

## Build
A repository-level GitHub Actions workflow builds a debug APK and uploads it as an artifact. This is a catalog only; it does not install mods automatically or include Minecraft itself. Check compatibility and permissions on each mod page before downloading.