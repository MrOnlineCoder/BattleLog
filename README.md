# BattleLog

CombatLog port for Sponge

This plugin prevents players on PVP servers from logging out with impunity.

When player is attacked, he and his attacker enter **battle or PVP** mode.

During this mode all commands are disabled.
If any of the fighters logs out - their items are dropped on the ground and their inventory is cleared.

Every hit replenishes each fighters battle time.

## Getting Started

Install BattleLog by putting latest JAR into your server's mods folder.

## Configuration

Default configuration (config/battlelog/battlelog.conf):

```
# Plugin locale (possible values: EN, RU, FR)
lang=RU
# Punish command to be executed. Leave empty to disable. Use % sign as placeholder for player name
punishCmd="tempban % 5m PVP Logout"
# Duration of the battle (in seconds)
time=10
```

* lang (EN,RU) - change plugin messages language

* punishCmd - this command will be executed when player, who logged out during the battle, joins the game. Use % sign as a placeholder for target player's name. For example: **tempban % 5m PVP logout** will ban player for 5 minutes with reason.

* time - time in seconds, duration of the battle.

## Authors
MrOnlineCoder

Kaeios

## License
MIT
