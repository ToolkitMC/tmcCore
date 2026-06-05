# tmcCore

**tmcCore** is a powerful, lightweight **sandbox-style core library** for Minecraft 1.21.1 (Fabric). It works **without Fabric API** and provides a wide range of utilities, systems, and secure APIs that other mods can depend on.

## Features

tmcCore includes many ready-to-use modules:

### Core Systems
- **Config System** — Load JSON configs from both external files and inside mod JARs (`tmcCore/*.json`)
- **Event Bus** — Custom event system with priority support
- **Registry Helper** — Easy Item, Block, and Entity registration
- **Persistent Data** — PlayerData & WorldData storage

### Sandbox & Security
- **Rate Limiter** — Prevent abuse with action limits
- **Audit Logger** — Log all important actions
- **Safe Command Executor** — Secure command handling
- **Player Isolation** — Temporarily isolate players
- **Resource Limiter** — Limit resource usage
- **Event Filter** — Whitelist/blacklist events
- **Secure Storage** — Encrypted-style data storage
- **Anti-Abuse System** — Automatic violation detection
- **Ban / Mute / Kick Manager**
- **Whitelist & Blacklist**
- **Error Handler** — Centralized error management
- **Scheduler** — Tick-based task scheduler (no Fabric API)

### Utility Systems
- **Permission API** — Simple permission levels
- **Region System** — Define and check protected areas
- **Quest System** — Quest tracking with progress
- **Economy System** — Balance management
- **Cooldown Manager** — Per-action cooldowns
- **Math Utilities** — Vector, lerp, clamp, distance
- **Effects / Sound / Particle** — Easy visual and audio helpers
- **Chat Helper** — Colored messages and broadcasts
- **GUI Builder** — Simple screen creation
- **Loot & Recipe Helper**
- **Version Checker**
- **Metrics** — Usage statistics
- **Notification System**
- **Backup System**

## Requirements

- Minecraft **1.21.1**
- Fabric Loader **0.16.10+**
- **No Fabric API required**

## How to Use (as a dependency)

Add the following to your `build.gradle`:

```groovy
repositories {
    maven { url "https://maven.fabricmc.net/" }
}

dependencies {
    modImplementation "com.toolkitmc.tmcCore:tmcCore:1.0"
}
```

## Building from Source

```bash
./gradlew build
```

## License

This project is licensed under the **MIT License**.

## Repository

https://github.com/ToolkitMC/tmcCore

---

Made with ❤️ by ToolkitMC
