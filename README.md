# You Shall Not Spawn (YSNS) - Extended Edition

This is an extended version of the original "You Shall Not Spawn" mod by ElocinDev, optimized for Minecraft 1.20.1 with Fabric.

## What is YSNS?

YSNS (You Shall Not Spawn) is a lightweight mod that gives you complete control over mob spawning in your Minecraft world. It allows you to disable specific mob spawns globally, per dimension, or per biome.

## What's New in This Extended Edition?

- **Biome-Specific Controls**: Control mob spawning based on biomes - keep certain mobs out of specific areas
- **Advanced Pattern Matching**: Use powerful regex patterns to target groups of mobs or biomes at once
- **Spawn Chance Control**: Set custom spawn chances from 0% to 100% for fine-tuned mob control
- **Optimized for 1.20.1**: Fully compatible with Minecraft 1.20.1 and the latest Fabric loader (0.15.11+)
- **Updated Dependencies**: Works with the latest Fabric API (0.92.2+) and Necronomicon (1.4.2)
- **Performance Optimized**: Minimal impact on game performance even with complex rules

## Requirements

- Minecraft 1.20.1
- Fabric Loader 0.15.11+
- Fabric API 0.92.2+
- Necronomicon 1.4.2

## Installation

1. Install Fabric for Minecraft 1.20.1
2. Download and install Fabric API
3. Download and install Necronomicon
4. Download and place the YSNS jar file in your mods folder

## Configuration

The mod creates configuration files in the `config/ysns` folder:
- `disabled_entities.json5` - Configure globally disabled entities
- `per_dimension_entities.json5` - Configure dimension-specific entity spawning rules
- `per_biome_entities.json5` - Configure biome-specific entity spawning rules (NEW!)

### Example Configuration

```json
// Disable hostile mobs in forest biomes
{
  "biomes": [
    {
      "entityId": "minecraft:zombie",
      "biome": "minecraft:forest",
      "spawn_chance": 0.0
    },
    {
      "entityId": "minecraft:skeleton",
      "biome": "minecraft:forest",
      "spawn_chance": 0.0
    },
    {
      "entityId": "minecraft:creeper",
      "biome": "minecraft:forest",
      "spawn_chance": 0.0
    }
  ]
}
```

## Credits

- Original mod by ElocinDev
- Extended and updated by Bradderz65

## License

This project is licensed under LGPL-3.0 - see the LICENSE file for details. 