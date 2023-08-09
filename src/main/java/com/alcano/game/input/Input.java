package com.alcano.game.input;

import org.joml.Vector2f;

public final class Input {

    private static final MouseListener MOUSE = MouseListener.get();
    private static final KeyListener KEYBOARD = KeyListener.get();

    public static boolean getMouseButton(int button) {
        return MOUSE.buttonPressed(button);
    }

    public static Vector2f position() {
        return new Vector2f(MOUSE.getX(), MOUSE.getY());
    }

    public static Vector2f last() {
        return new Vector2f(MOUSE.getLastX(), MOUSE.getLastY());
    }

    public static Vector2f scroll() {
        return new Vector2f(MOUSE.getScrollX(), MOUSE.getScrollY());
    }

    public static boolean getKey(int keyCode) {
        return KEYBOARD.keyPressed(keyCode);
    }

}
