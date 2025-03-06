## 2.0.3 - Extended Edition
- Added biome-specific mob spawn control - keep certain mobs out of specific biomes
- Implemented custom spawn chance settings from 0% to 100% for fine-tuned control
- Enhanced regex pattern matching for targeting multiple entities or biomes at once
- Optimized for Minecraft 1.20.1 with Fabric Loader 0.15.11+
- Updated to latest dependencies (Fabric API 0.92.2+)
- Improved performance with minimal impact on gameplay
- Fixed compatibility issues with other popular Fabric mods

## 2.0.2
- Fixed crash when using regex on per-dimension bans

## 2.0.1
- Added fallback calls to prevent possible cases where entities would still spawn
- Fixed crash on servers

# 2.0.0
Initial release of You Shall Not Spawn v2
- You can now disable entities globally, per-dimension, or reduce spawn chances. 
- Config moved to JSON5
- Added Regex support (Can be used for example to ban all mobs except 1)
- Fixed many issues encountered when interacting with other mods