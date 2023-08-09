package com.alcano.game.input;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {

    private static final int NUM_BUTTONS = 5;

    private static MouseListener instance;

    private double scrollX, scrollY;
    private double posX, posY, lastX, lastY;
    private boolean buttonsPressed[] = new boolean[NUM_BUTTONS];
    private boolean dragging;

    private MouseListener() {}

    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }

        return instance;
    }

    public static void mousePosCallback(long window, double posX, double posY) {
        get().lastX = get().posX;
        get().lastY = get().posY;
        get().posX = posX;
        get().posY = posY;
        get().dragging = get().buttonPressed(0) || get().buttonPressed(1) || get().buttonPressed(2);
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

    public static void endFrame() {
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        get().lastX = get().posX;
        get().lastY = get().posY;
    }

    public float getX() {
        return (float) posX;
    }

    public float getY() {
        return (float) posY;
    }

    public float getLastX() {
        return (float) lastX;
    }

    public float getLastY() {
        return (float) lastY;
    }

    public float getScrollX() {
        return (float) scrollX;
    }

    public float getScrollY() {
        return (float) scrollY;
    }

    public boolean buttonPressed(int button) {
        if (button >= NUM_BUTTONS) return false;

        return this.buttonsPressed[button];
    }
}
