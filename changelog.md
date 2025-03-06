## 2.0.3 - Extended Edition
- Added biome-specific mob spawn control with new `per_biome_entities.json5` configuration
- Implemented special handling for zombie variants to ensure proper spawn control
- Enhanced regex support for more flexible entity and biome pattern matching
- Optimized for Minecraft 1.20.1 with Fabric Loader 0.15.11+
- Updated dependencies to latest versions (Fabric API 0.92.2+)
- Improved error handling and logging for better troubleshooting
- Fixed compatibility issues with other popular Fabric mods
- Optimized spawn check algorithm for better performance

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