package com.alcano.game.input;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {

    private static final int NUM_BUTTONS = 5;

    private static Mouse instance;

    private double scrollX, scrollY;
    private double posX, posY, lastX, lastY;
    private boolean buttonsPressed[] = new boolean[NUM_BUTTONS];
    private boolean dragging;

    private Mouse() {
        System.out.println("Scroll: (" + this.scrollX + ", " + this.scrollY + ")");
        System.out.println("Position: (" + this.posX + ", " + this.posY + ")");
        System.out.println("Last Position: (" + this.lastX + ", " + this.lastY + ")");
        System.out.println(this.dragging);
    }

    public static Mouse get() {
        if (instance == null) {
            instance = new Mouse();
        }

        return instance;
    }

    public static void mousePosCallback(long window, double posX, double posY) {
        get().lastX = get().posX;
        get().lastY = get().posY;
        get().posX = posX;
        get().posY = posY;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button >= NUM_BUTTONS) return;

        if (action == GLFW_PRESS) {
            get().buttonsPressed[button] = true;
        } else if (action == GLFW_RELEASE) {
            get().buttonsPressed[button] = false;
            get().dragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double offsetX, double offsetY) {
        get().scrollX = offsetX;
        get().scrollY = offsetY;
    }

    public 

}
