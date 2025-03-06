package elocindev.ysns.event;

import elocindev.ysns.YSNS;
import elocindev.ysns.api.YSNSAPI;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

//#if FORGE==1
//$$ import elocindev.ysns.YSNS;
//$$ import net.minecraft.server.level.ServerPlayer;
//$$ import net.minecraft.world.entity.player.Player;
//$$ import net.minecraftforge.event.entity.EntityJoinLevelEvent;
//$$ import net.minecraft.world.level.Level;

//#if MC>=12001
//$$ import net.minecraftforge.event.entity.living.MobSpawnEvent;
//#endif

//$$ import net.minecraftforge.eventbus.api.SubscribeEvent;
//$$ import net.minecraftforge.eventbus.api.Event.Result;
//$$ import net.minecraftforge.fml.common.Mod;

//$$@Mod.EventBusSubscriber(modid = YSNS.MODID)
//#else

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

//#endif
public class SpawnEventHandler {
    //#if FORGE==1
    //$$ @SubscribeEvent
    //$$ public static void fallbackSpawnEvent(EntityJoinLevelEvent event) {
    //$$    if (event.getLevel().isClientSide()) return;
    //$$    if (event.getEntity() instanceof LivingEntity && YSNSAPI.isEntityDisabled(event.getEntity(), event.getEntity().getLevel())) {
    //$$        event.setCanceled(true);
    //$$    }
    //$$}

    //#if MC>=12001
    //$$ @SubscribeEvent
    //$$ public static void fallbackFinalizeSpawnEvent(MobSpawnEvent.FinalizeSpawn event) {
    //$$    if (event.getLevel().isClientSide()) return;
    //$$    if (event.getEntity() instanceof LivingEntity && YSNSAPI.isEntityDisabled(event.getEntity(), event.getEntity().level())) {
    //$$        event.setSpawnCancelled(true);
    //$$    }
    //$$}
    //#endif

    //#endif
    
    //#if FABRIC==1
    public static void register() {
        YSNS.LOGGER.info("Registering YSNS spawn event handler");
        
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof LivingEntity livingEntity) {
                String entityId = entity.getType().getDescriptionId();
                
                // Special handling for zombies
                boolean isZombieVariant = entityId.contains("zombie");
                if (isZombieVariant) {
                    // Get biome information
                    String biomeId = null;
                    try {
                        BlockPos entityPos = entity.blockPosition();
                        ResourceLocation biomeResLoc = world.registryAccess()
                            .registryOrThrow(Registries.BIOME)
                            .getKey(world.getBiome(entityPos).value());
                        
                        if (biomeResLoc != null) {
                            biomeId = biomeResLoc.toString();
                        }
                    } catch (Exception e) {
                        YSNS.LOGGER.error("Error getting biome for zombie", e);
                    }
                    
                    // Check if this is a plains biome
                    if (biomeId != null && biomeId.equals("minecraft:plains")) {
                        livingEntity.discard();
                        return;
                    }
                }
                
                if (YSNSAPI.isEntityDisabled(entity, world)) {
                    livingEntity.discard();
                }
            }
        });
        
        YSNS.LOGGER.info("YSNS spawn event handler registered");
    }
    //#endif
}
