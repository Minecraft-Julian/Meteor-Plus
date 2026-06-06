package com.meteorplus;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeteorPlusMod implements ModInitializer {
    public static final String MOD_ID = "meteorplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("MeteorPlus mod initialized");
    }
}
