package com.toolkitmc.tmccore.sandbox;

import com.toolkitmc.tmccore.TmcCore;

public class TmcErrorHandler {

    public static void handle(Throwable e, String context) {
        TmcCore.LOGGER.error("[ErrorHandler] {} - {}", context, e.getMessage());
        e.printStackTrace();
    }

    public static void handle(String message) {
        TmcCore.LOGGER.error("[ErrorHandler] {}", message);
    }

    public static void safeRun(Runnable runnable, String context) {
        try {
            runnable.run();
        } catch (Exception e) {
            handle(e, context);
        }
    }
}