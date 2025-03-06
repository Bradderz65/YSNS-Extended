package elocindev.ysns.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elocindev.ysns.YSNS;
import elocindev.ysns.api.YSNSAPI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {
    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void ysns$removeDisabledEntitiesOnSpawn(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (entity instanceof LivingEntity living) {
            String entityId = entity.getType().getDescriptionId();
            YSNS.LOGGER.info("ServerWorldMixin checking entity: " + entityId);
            
            // Special handling for zombies
            boolean isZombieVariant = entityId.contains("zombie");
            if (isZombieVariant) {
                YSNS.LOGGER.info("ServerWorldMixin detected zombie variant: " + entityId);
                
                // Get biome information
                try {
                    BlockPos entityPos = entity.blockPosition();
                    String biomeId = entity.level().registryAccess()
                        .registryOrThrow(Registries.BIOME)
                        .getKey(entity.level().getBiome(entityPos).value())
                        .toString();
                    
                    YSNS.LOGGER.info("Zombie in biome: " + biomeId);
                    
                    // Check if this is a plains biome
                    if (biomeId.equals("minecraft:plains")) {
                        YSNS.LOGGER.info("Zombie in plains biome - disabling");
                        living.discard();
                        ci.setReturnValue(false);
                        return;
                    }
                } catch (Exception e) {
                    YSNS.LOGGER.error("Error getting biome for zombie", e);
                }
            }
            
            if (YSNSAPI.isEntityDisabled(living, living.level())) {
                YSNS.LOGGER.info("ServerWorldMixin discarding entity: " + entityId);
                living.discard();
                ci.setReturnValue(false);
            }
        }
    }
}
