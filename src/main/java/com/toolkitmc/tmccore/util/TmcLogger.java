package com.toolkitmc.tmccore.util;

import com.toolkitmc.tmccore.TmcCore;

public class TmcLogger {
    public static void info(String msg) {
        TmcCore.LOGGER.info("[tmcCore] " + msg);
    }

    public static void warn(String msg) {
        TmcCore.LOGGER.warn("[tmcCore] " + msg);
    }

    public static void error(String msg) {
        TmcCore.LOGGER.error("[tmcCore] " + msg);
    }
}