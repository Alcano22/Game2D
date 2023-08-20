package com.alcano.game.util;

import org.lwjgl.glfw.GLFW;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Time {

    public static float deltaTime;

    public static String getCurrentTimestamp(char leftWrap, char rightWrap) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return leftWrap + LocalTime.now().format(formatter) + rightWrap;
    }

    public static String getCurrentTimestamp() {
        return getCurrentTimestamp('[', ']');
    }

    public static float getTime() {
        return (float) GLFW.glfwGetTime();
    }

}
