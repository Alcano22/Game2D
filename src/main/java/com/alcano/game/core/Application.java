package com.alcano.game.core;

import com.alcano.game.debug.Debug;

public final class Application {

    private Application() {}

    public static void exit(int code) {
        System.exit(code);
    }

    public static void exitError(String error) {
        Debug.logError(error);
        exit(1);
    }

    public static void exitError(String error, String log) {
        exitError(error + "\n\t" + log);
    }

}
