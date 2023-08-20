package com.alcano.game.core;

import com.alcano.game.debug.Debug;
import com.alcano.game.imgui.ImGuiLayer;
import com.alcano.game.input.Input;
import com.alcano.game.input.KeyListener;
import com.alcano.game.input.MouseListener;
import com.alcano.game.util.Time;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static Window instance;

    private final Game game;

    private int width, height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imGuiLayer;

    private Window() {
        this.game = Game.get();
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
        Debug.logInfo("Hello LWJGL " + Version.getVersion() + "!");

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
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
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
        glfwSetWindowSizeCallback(this.glfwWindow, (window, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
        });

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

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.imGuiLayer = new ImGuiLayer(this.glfwWindow);
        this.imGuiLayer.initImGui();

        this.game.init();
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime;

        while (!glfwWindowShouldClose(this.glfwWindow)) {
            endTime = Time.getTime();
            Time.deltaTime = endTime - beginTime;
            beginTime = endTime;

            glfwPollEvents();

            Vector4f backgroundColor = this.game.sceneManager.getCurrentScene().camera.backgroundColor;
            glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
            glClear(GL_COLOR_BUFFER_BIT);

            if (Input.getKey(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(this.glfwWindow, true);
            }

            this.game.update();
            this.imGuiLayer.update(this.game.sceneManager.getCurrentScene());

            glfwSwapBuffers(this.glfwWindow);
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

    public static float getWidth() {
        return get().width;
    }

    public static void setWidth(int width) {
        get().width = width;
    }

    public static float getHeight() {
        return get().height;
    }

    public static void setHeight(int height) {
        get().height = height;
    }

}
