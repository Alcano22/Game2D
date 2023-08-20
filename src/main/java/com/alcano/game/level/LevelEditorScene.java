package com.alcano.game.level;

import com.alcano.game.gameobject.GameObject;
import com.alcano.game.gameobject.Transform;
import com.alcano.game.gameobject.component.SpriteRenderer;
import com.alcano.game.util.AssetPool;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {

    private GameObject obj1;

    public LevelEditorScene() {
        super("Overworld");
    }

    @Override
    public void init() {
        this.loadResources();

        this.camera.backgroundColor = new Vector4f(
                0.043f, 0.22f, 0.41f, 1.0f);

        obj1 = new GameObject("Object 1",
                new Transform(new Vector2f(100.0f, 100.0f), new Vector2f(256.0f, 256.0f)), 4);
        obj1.addComponent(new SpriteRenderer(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)));
        this.addGameObject(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(300.0f, 100.0f), new Vector2f(256.0f, 256.0f)), 3);
        obj2.addComponent(new SpriteRenderer(AssetPool.loadSprite("assets/textures/blend_image_2.png")));
        this.addGameObject(obj2);
    }

    private void loadResources() {
        AssetPool.addSpriteSheet("assets/textures/stardew_items.png", 16, 16, 0);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void imgui() {
        ImGui.begin("Test window");
        ImGui.text("Some random text");
        if (ImGui.button("Move Object")) {
            this.obj1.transform.position.x += 10;
        }
        ImGui.end();
    }
}
