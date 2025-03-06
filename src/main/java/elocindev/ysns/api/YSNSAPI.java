package elocindev.ysns.api;

import elocindev.necronomicon.api.NecUtilsAPI;
import elocindev.ysns.YSNS;
import elocindev.ysns.config.DisabledBiomeEntities;
import elocindev.ysns.config.DisabledEntities;
import elocindev.ysns.config.PerDimensionEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class YSNSAPI {
    public static boolean isEntityDisabled(Entity entity, Level world) {
        if (entity == null || world == null) return false;

        String entity_id = NecUtilsAPI.getEntityId(entity);
        String dimension_id = world.dimension().location().toString();
        
        // Check global disabled entities
        for (String id : DisabledEntities.INSTANCE.disabled) {
            if (parseEntry(id, entity_id)) {
                return true;
            }
        }

        // Check dimension-specific rules
        for (DimensionSpawnHolder holder : PerDimensionEntities.INSTANCE.dimensions) {
            if (parseEntry(holder.getEntityIdentifier(), entity_id) && parseEntry(holder.getDimension(), dimension_id)) {
                boolean disabled = world.getRandom().nextFloat() > holder.getSpawnChance();
                return disabled;
            }
        }
        
        // Check biome-specific rules
        BlockPos entityPos = entity.blockPosition();
        String biome_id = getBiomeId(world, entityPos);
        
        if (biome_id != null) {
            // Special handling for zombies to ensure they're properly checked
            boolean isZombieVariant = entity_id.contains("zombie");
            
            for (BiomeSpawnHolder holder : DisabledBiomeEntities.INSTANCE.biomes) {
                boolean entityMatches = parseEntry(holder.getEntityIdentifier(), entity_id);
                boolean biomeMatches = parseEntry(holder.getBiome(), biome_id);
                
                // Special handling for zombies
                if (isZombieVariant && holder.getEntityIdentifier().contains("zombie")) {
                    if (biomeMatches) {
                        entityMatches = true;
                    }
                }
                
                if (entityMatches && biomeMatches) {
                    // For zombies, always disable if the rule matches
                    if (isZombieVariant && holder.getSpawnChance() == 0.0f) {
                        return true;
                    }
                    
                    boolean disabled = world.getRandom().nextFloat() > holder.getSpawnChance();
                    return disabled;
                }
            }
        } else {
            YSNS.LOGGER.warn("Could not determine biome for entity at " + entityPos);
        }
        
        return false;
    }

    private static String getBiomeId(Level world, BlockPos pos) {
        try {
            // For Minecraft 1.20.1
            ResourceLocation biomeId = world.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getKey(world.getBiome(pos).value());
            
            return biomeId != null ? biomeId.toString() : null;
        } catch (Exception e) {
            YSNS.LOGGER.error("Error getting biome ID", e);
            return null;
        }
    }

    public static boolean parseEntry(String entry, String compareTo) {
        if (entry == null || compareTo == null) {
            return false;
        }
        
        // Special handling for zombie variants
        if (entry.contains("zombie") && compareTo.contains("zombie")) {
            if (entry.equals("minecraft:zombie") && compareTo.startsWith("minecraft:zombie")) {
                return true;
            }
        }
        
        if (entry.startsWith("!")) {
            try {
                boolean matches = compareTo.matches(entry.substring(1));
                return matches;
            } catch (Exception e) {
                YSNS.LOGGER.error("Error in regex pattern: " + entry, e);
                return false;
            }
        } else {
            return compareTo.equals(entry);
        }
    }
}
