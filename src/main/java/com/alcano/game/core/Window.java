package com.alcano.game;

import com.alcano.game.input.Input;
import com.alcano.game.input.KeyListener;
import com.alcano.game.input.MouseListener;
import com.alcano.game.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static Window instance;

    private int width, height;
    private String title;
    private long glfwWindow;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Game 2D";
    }

    public static Window get() {
        if (instance == null) {
            instance = new Window();
        }

        return instance;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        this.init();
        this.loop();
        this.cleanup();
    }

    private void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Unable to create GLFW window");
        }

        // Setup input callbacks
        glfwSetCursorPosCallback(this.glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(this.glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(this.glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(this.glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(this.glfwWindow);

        /*
         * This line is critical for LWJGL's interoperation with GLFW's
         * OpenGL context, or any context that is managed externally.
         * LWJGL detects the context that is current in the current thread,
         * creates the GLCapabilities instance and makes OpenGL
         * bindings available for use
         */
        GL.createCapabilities();
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime;

        while (!glfwWindowShouldClose(this.glfwWindow)) {
            glfwPollEvents();

            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (Input.getKey(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(this.glfwWindow, true);
            }

            glfwSwapBuffers(this.glfwWindow);

            endTime = Time.getTime();
            Time.deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }

    private void cleanup() {
        // Free the memory
        glfwFreeCallbacks(this.glfwWindow);
        glfwDestroyWindow(this.glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

}
