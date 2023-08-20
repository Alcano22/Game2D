package com.alcano.game.level;

import com.alcano.game.gameobject.GameObject;
import com.alcano.game.renderer.SceneRenderer;
import com.alcano.game.util.AssetPool;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    public final String name;
    public final Camera camera;

    protected final SceneRenderer renderer;
    protected final List<GameObject> gameObjects;

    protected GameObject activeGameObject;

    private boolean running = false;

    public Scene(String name) {
        this.name = name;
        this.camera = new Camera();
        this.renderer = new SceneRenderer();
        this.gameObjects = new ArrayList<>();
    }

    public void addGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);

        if (!this.running) return;

        gameObject.start();
        this.renderer.addGameObject(gameObject);
    }

    public void init() {
        AssetPool.loadShader("assets/shaders/default.glsl");
    }

    private void loadResources() {

    }

    public void start() {
        this.gameObjects.forEach(gameObject -> {
            gameObject.start();
            this.renderer.addGameObject(gameObject);
        });
        this.running = true;
    }

    public void update() {
        this.gameObjects.forEach(GameObject::update);
        this.renderer.render();
    }

    public void sceneImgui() {
        if (this.activeGameObject != null) {
            ImGui.begin("Inspector");
            this.activeGameObject.imgui();
            ImGui.end();
        }

        this.imgui();
    }

    public void imgui() {

    }

}
