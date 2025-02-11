# Minecraft Backpack Plugin

A feature-rich Minecraft plugin for Paper servers that adds customizable backpacks with upgradeable storage, commands, and permissions.

---

## Features

- **Backpack Inventory**: Each player gets a personal backpack accessible via `/bp` or by right-clicking a custom backpack item.
- **Upgrade System**: Players can upgrade their backpack size using experience levels.
- **Admin Commands**: Reset backpacks, give backpack items, and manage player backpacks.
- **Permissions**: Fine-grained control over who can use commands and features.
- **Customizable**: Configure default backpack size, upgrade costs, and more.

---

## Commands

| Command               | Description                                                                 | Permission               |
|-----------------------|-----------------------------------------------------------------------------|--------------------------|
| `/bp`                 | Open your backpack                                                          | `backpacks.use`          |
| `/bp give [player]`   | Give a backpack item to yourself or another player                          | `backpacks.give`         |
| `/bp open [player]`   | Open your backpack or another player's backpack (admin)                     | `backpacks.open.others`  |
| `/bp upgrade`         | Upgrade your backpack size (costs 5 experience levels)                      | `backpacks.use`          |
| `/bp reset [player]`  | Reset your backpack or another player's backpack to default size            | `backpacks.reset.others` |
| `/bp help`            | Show help menu                                                              | `backpacks.use`          |

---

## Permissions

| Permission                  | Description                                           | Default |
|-----------------------------|-------------------------------------------------------|---------|
| `backpacks.use`             | Allows use of basic backpack commands                 | `true`  |
| `backpacks.give`            | Allows giving backpack items to others                | `op`    |
| `backpacks.open.others`     | Allows opening other players' backpacks               | `op`    |
| `backpacks.reset.others`    | Allows resetting other players' backpacks             | `op`    |
| `backpacks.admin`           | Grants all admin permissions (parent permission)      | `op`    |
| `backpacks.bypass`          | Bypass cooldowns and restrictions                     | `op`    |

---

## Installation

1. Download the latest `.jar` file from the [Releases](https://github.com/claibornevv/backpacks/releases) page.
2. Place the `.jar` file in your server's `plugins` folder.
3. Restart the server.
4. Configure the plugin using the generated `config.yml` file (if applicable).

---

## Configuration

The plugin generates a `config.yml` file with the following options:

```yaml
# Default backpack size (in slots)
default-size: 9

# Cost to upgrade backpack (in experience levels)
upgrade-cost: 5

# Maximum backpack size (in slots)
max-size: 54