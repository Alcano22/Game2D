package com.alcano.game.debug;

import com.alcano.game.util.Time;

public final class Debug {

    private Debug() {}

    public static void log(Object message, Level level) {
        String timestamp = Time.getCurrentTimestamp();

        System.out.println(level.color + timestamp + "[" + level + "] " + message);
    }

    public static void log(Object message) {
        log(message, Level.DEBUG);
    }

    public static void logInfo(Object message) {
        log(message, Level.INFO);
    }

    public static void logWarning(Object message) {
        log(message, Level.WARNING);
    }

    public static void logError(Object message) {
        log(message, Level.ERROR);
    }

    public enum Level {
        DEBUG(ConsoleColor.ANSI_GRAY),
        INFO(ConsoleColor.ANSI_WHITE),
        WARNING(ConsoleColor.ANSI_YELLOW),
        ERROR(ConsoleColor.ANSI_RED);

        public final String color;

        Level(String color) {
            this.color = color;
        }
    }

}
