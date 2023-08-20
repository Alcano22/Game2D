package com.alcano.game.input;

import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.*;

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

    public static float getAxis(Axis axis) {
        if (axis == Axis.HORIZONTAL) {
            if (Input.getKey(GLFW_KEY_A)) {
                return -1.0f;
            } else if (Input.getKey(GLFW_KEY_D)) {
                return 1.0f;
            }
        } else {
            if (Input.getKey(GLFW_KEY_W)) {
                return 1.0f;
            } else if (Input.getKey(GLFW_KEY_S)) {
                return -1.0f;
            }
        }

        return 0;
    }

    public static Vector2f getAxes() {
        return new Vector2f(getAxis(Axis.HORIZONTAL), getAxis(Axis.VERTICAL));
    }

    public enum Axis {
        HORIZONTAL,
        VERTICAL;
    }
}
