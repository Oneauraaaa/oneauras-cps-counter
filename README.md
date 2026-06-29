# oneaura's CPS Counter

![oneaura's CPS Counter icon](src/main/resources/assets/oneauras-cps-counter/icon.png)

A lightweight client-side Fabric mod that adds a customizable CPS counter for modern Minecraft versions.

## Overview

This mod focuses on a simple on-screen CPS display with a few customization options for layout and styling. It is designed for Fabric on recent Minecraft versions and runs entirely client-side.

## Features

- Left click, right click, or both counters
- Moveable HUD position
- Custom label text
- Text color and background color options
- Optional text shadow
- Optional text styling
- Mod Menu config screen support
- Client-side only

## Requirements

- Minecraft `1.21.11`
- Java `21+`
- Fabric Loader
- Fabric API

## Optional

- Mod Menu
- Cloth Config for the in-game config screen

## Screenshots

Add screenshots here once you have a few clean in-game captures:

- HUD in normal gameplay
- Config screen open in Mod Menu
- Different color and style example
- Alternate HUD position example

Suggested filenames if you add them later:

- `docs/screenshots/hud-default.png`
- `docs/screenshots/config-screen.png`
- `docs/screenshots/hud-alt-style.png`
- `docs/screenshots/hud-alt-position.png`

## Installation

1. Install Fabric Loader for your Minecraft version.
2. Put Fabric API in your `mods` folder if it is not already included in your setup.
3. Place `oneauras-cps-counter` in your `mods` folder.
4. Launch the game with Fabric.

## Configuration

If Mod Menu is installed, the mod settings can be opened through the Mod Menu config screen. From there you can adjust:

- display mode
- label text
- text color
- background color
- text shadow
- text style
- corner radius
- HUD position

## Building

```powershell
.\gradlew build
```

Built files will be placed in `build/libs/`.

## FAQ

### Does this work on servers?

Yes. The mod is client-side only and does not need to be installed on the server.

### Do I need Mod Menu?

No. Mod Menu is optional, but it makes the config screen easier to access.

### Do I need Cloth Config?

Cloth Config is only needed for the in-game config screen.

### Which Minecraft version is supported?

This repository is currently set up for `1.21.11`.

## Modrinth

The Modrinth project link can be added here after approval.

- Project: `coming soon`
- Versions: `coming soon`

## Source And Issues

- Source: [https://github.com/Oneauraaaa/oneauras-cps-counter](https://github.com/Oneauraaaa/oneauras-cps-counter)
- Issues: [https://github.com/Oneauraaaa/oneauras-cps-counter/issues](https://github.com/Oneauraaaa/oneauras-cps-counter/issues)

## License

This project is licensed under the MIT License.
