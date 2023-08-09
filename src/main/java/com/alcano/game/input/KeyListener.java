package com.alcano.game.input;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {

    private static final int NUM_KEYS = 350;

    private static KeyListener instance;

    private boolean keysPressed[] = new boolean[NUM_KEYS];

    private KeyListener() {}

    public static KeyListener get() {
        if (instance == null) {
            instance = new KeyListener();
        }

        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keysPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keysPressed[key] = false;
        }
    }

    public boolean keyPressed(int keyCode) {
        if (keyCode >= NUM_KEYS) return false;

        return this.keysPressed[keyCode];
    }

}
