# tmCore

Core API library for [ToolkitMC](https://github.com/ToolkitMC) Fabric mods.

Provides six subsystems accessible via static accessors on `TmCore`:

| Accessor | Purpose |
|---|---|
| `TmCore.registry()` | Cross-mod object registry |
| `TmCore.events()` | Custom event bus |
| `TmCore.config()` | JSON config management |
| `TmCore.networking()` | S2C / C2S packet abstraction |
| `TmCore.commands()` | Brigadier command wrapper |
| `TmCore.data()` | Persistent data attachments |

---

## Requirements

- Minecraft 1.21.8
- Fabric Loader ≥ 0.16
- Fabric API
- Java 21

---

## Installation

Add tmCore to your mod's `fabric.mod.json`:

```json
"depends": {
  "tmcore": "*"
}
```

Then add the dependency to your `build.gradle` (once published to a Maven repository):

```groovy
dependencies {
    modImplementation "com.toolkitmc:tmcore:${tmcore_version}"
}
```

---

## Subsystems

### Registry

Cross-mod object registry. Frozen after server start — late registration throws.

```java
// Register
TmCore.registry().register("mymod", "my_feature", myFeatureInstance);

// Retrieve
Optional<MyFeature> f = TmCore.registry().get("mymod", "my_feature", MyFeature.class);

// Identifier overloads also available
TmCore.registry().register(Identifier.of("mymod", "my_feature"), myFeatureInstance);
```

---

### Event Bus

Dynamic event bus keyed by event class. Any object can be an event. Subscribers fire in priority order (higher = first); exceptions in one subscriber do not stop others.

```java
// Define an event
public record PlayerJoinTmEvent(ServerPlayerEntity player, MinecraftServer server) {}

// Subscribe — returns a handle for later unsubscription
TmEventHandle handle = TmCore.events().subscribe(PlayerJoinTmEvent.class, event -> {
    TmCore.LOGGER.info("{} joined!", event.player().getName().getString());
});

// Subscribe with explicit priority (default: 0)
TmCore.events().subscribe(PlayerJoinTmEvent.class, event -> { ... }, 10);

// Fire
TmCore.events().fire(new PlayerJoinTmEvent(player, server));

// Cancellable events
public record BlockBreakTmEvent(BlockPos pos) implements Cancellable { ... }
boolean allowed = TmCore.events().fireAndCheck(new BlockBreakTmEvent(pos));

// Unsubscribe
handle.unsubscribe();
```

---

### Config

JSON config management backed by GSON. Config files live at `config/<namespace>.json`. Missing fields are filled from defaults on load; the merged result is written back to disk.

```java
// Define a config POJO
public class MyModConfig {
    public boolean enableFeature = true;
    public int maxCooldownTicks = 20;
    public String prefix = "[MyMod]";
}

// Register during onInitialize — returns the loaded (or default) config immediately
MyModConfig cfg = TmCore.config().register("mymod", MyModConfig.class, new MyModConfig());

// Access later
boolean active = TmCore.config().get("mymod", MyModConfig.class).enableFeature;

// Reload from disk (fires ConfigReloadEvent)
TmCore.config().reload("mymod");

// Save current in-memory state to disk
TmCore.config().save("mymod");

// Reset to defaults and save
TmCore.config().reset("mymod");
```

Listen for reloads via the event bus:

```java
TmCore.events().subscribe(ConfigReloadEvent.class, event -> {
    if ("mymod".equals(event.namespace())) {
        cfg = TmCore.config().get("mymod", MyModConfig.class);
    }
});
```

---

### Networking

S2C / C2S packet abstraction wrapping Fabric's `CustomPayload` API. Callers work with `PacketByteBuf` and `Identifier` channel IDs; the underlying payload registration is handled internally.

```java
// Define a channel
public static final Identifier MY_CHANNEL = Identifier.of("mymod", "my_packet");

// Register C2S handler (server, during onInitialize)
TmCore.networking().registerServerReceiver(MY_CHANNEL, (player, buf, responseSender) -> {
    int value = buf.readInt();
    // handle on server
});

// Register S2C handler (client, during onInitializeClient)
TmCore.networking().registerClientReceiver(MY_CHANNEL, (buf, responseSender) -> {
    String message = buf.readString();
    // handle on client
});

// Send S2C
TmCore.networking().sendToPlayer(player, MY_CHANNEL, buf -> buf.writeString("hello"));
TmCore.networking().sendToAll(server, MY_CHANNEL, buf -> buf.writeString("broadcast"));
TmCore.networking().sendToTracking(entity, MY_CHANNEL, buf -> buf.writeInt(42));

// Send C2S (client side)
TmCore.networking().sendToServer(MY_CHANNEL, buf -> buf.writeInt(42));
```

---

### Commands

Brigadier command wrapper that defers registration to Fabric's `CommandRegistrationCallback`, avoiding ordering issues.

```java
// Register during onInitialize
TmCore.commands().register(
    Identifier.of("mymod", "mycommand"),
    CommandManager.literal("mycommand")
        .requires(src -> src.hasPermissionLevel(2))
        .then(CommandManager.literal("reload")
            .executes(ctx -> {
                TmCore.config().reload("mymod");
                ctx.getSource().sendFeedback(() -> Text.literal("Config reloaded."), false);
                return Command.SINGLE_SUCCESS;
            }))
);
```

---

### Data Attachments

Persistent typed data storage for players, entities, and worlds. Player and entity data is serialized via the `WriteView`/`ReadView` API introduced in 1.21.8. World data uses `PersistentState` with a Codec-based serializer.

```java
// Define keys (static constants in your mod class)
public static final TmDataKey<Integer> KILLS =
    TmDataKey.of(Identifier.of("mymod", "kills"), Integer.class, 0);

public static final TmDataKey<Boolean> FLAG =
    TmDataKey.of(Identifier.of("mymod", "flag"), Boolean.class, false);

// Lazy default (fresh instance each call)
public static final TmDataKey<List<String>> HISTORY =
    TmDataKey.lazy(Identifier.of("mymod", "history"), List.class, ArrayList::new);

// Player data
TmCore.data().setPlayer(player, KILLS, 5);
int kills = TmCore.data().getPlayer(player, KILLS);           // 5
Optional<Integer> opt = TmCore.data().getPlayerOpt(player, KILLS);
TmCore.data().removePlayer(player, KILLS);

// Entity data (any entity)
TmCore.data().setEntity(entity, FLAG, true);
boolean flag = TmCore.data().getEntity(entity, FLAG);

// World data (per-dimension, persists across restarts)
TmCore.data().setWorld(world, KILLS, 100);
int worldKills = TmCore.data().getWorld(world, KILLS);

// Keep player data across death
TmCore.data().keepOnDeath(KILLS);
```

---

## License

[MIT](LICENSE)