package com.alcano.game.renderer;

import org.joml.Vector2f;

public class Sprite {

    private Texture texture;
    private Vector2f[] texCoords = {
            new Vector2f(1.0f, 1.0f),
            new Vector2f(1.0f, 0.0f),
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.0f, 1.0f)
    };

    public static Sprite init(Texture texture) {
        Sprite sprite = new Sprite();
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    public boolean isEmpty() {
        return texture.isEmpty();
    }
}
