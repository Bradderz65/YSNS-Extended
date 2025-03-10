package elocindev.ysns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.ysns.config.DisabledBiomeEntities;
import elocindev.ysns.config.DisabledEntities;
import elocindev.ysns.config.PerDimensionEntities;
import elocindev.ysns.event.SpawnEventHandler;

//#if FABRIC==1

import net.fabricmc.api.ModInitializer;

//#else

//$$ import net.minecraftforge.fml.common.Mod;
//$$ @Mod("ysns")
//#endif
public class YSNS
//#if FABRIC==1
    implements ModInitializer {
//#else
//$$ {
//#endif

    public static final String MODID = "ysns";
    public static final Logger LOGGER = LoggerFactory.getLogger("ysns");

    //#if FABRIC==1
    @Override
    public void onInitialize() {
    //#else
    //$$ public YSNS() {
    //#endif
        LOGGER.info("Initializing You Shall Not Spawn");

        try {
            NecConfigAPI.registerConfig(DisabledEntities.class);
            NecConfigAPI.registerConfig(PerDimensionEntities.class);
            NecConfigAPI.registerConfig(DisabledBiomeEntities.class);

            LOGGER.info("You Shall Not Spawn's Config initialized");
        } catch (Exception e) {
            LOGGER.error("Error initializing YSNS config", e);
        }

        //#if FABRIC==1
        SpawnEventHandler.register();
        LOGGER.info("You Shall Not Spawn initialized");
        //#endif
    }
}