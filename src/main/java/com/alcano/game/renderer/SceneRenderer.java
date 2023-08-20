package com.alcano.game.renderer;

import com.alcano.game.gameobject.GameObject;
import com.alcano.game.gameobject.component.SpriteRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SceneRenderer {

    private static final int MAX_BATCH_SIZE = 1000;

    private final List<RenderBatch> batches;

    public SceneRenderer() {
        this.batches = new ArrayList<>();
    }

    public void render() {
        this.batches.forEach(RenderBatch::render);
    }

    public void addGameObject(GameObject gameObject) {
        SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
        if (sprite == null) return;

        this.addSprite(sprite);
    }

    private void addSprite(SpriteRenderer sprite) {
        for (RenderBatch batch : this.batches) {
            if (!batch.hasRoom() || batch.zIndex != sprite.gameObject.zIndex) continue;

            Texture tex = sprite.getTexture();
            if (tex == null || batch.hasTexture(tex) || batch.hasTextureRoom()) {
                batch.addSprite(sprite);
                return;
            }
        }

        RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.zIndex);
        newBatch.start();
        this.batches.add(newBatch);
        newBatch.addSprite(sprite);
        Collections.sort(this.batches);
    }

}
