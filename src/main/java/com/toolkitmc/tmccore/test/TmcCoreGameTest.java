package com.toolkitmc.tmccore.test;

import com.toolkitmc.tmccore.TmcCore;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

public class TmcCoreGameTest implements FabricGameTest {

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testCoreLoaded(TestContext context) {
        TmcCore.LOGGER.info("tmcCore GameTest: Core loaded successfully!");
        context.complete();
    }
}