package com.alcano.game.gameobject;

import org.joml.Vector2f;

public class Transform {

    public Vector2f position;
    public float rotation;
    public Vector2f scale;

    public Transform(Vector2f position, float rotation, Vector2f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Transform(Vector2f position, float rotation) {
        this(position, rotation, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        this(position, 0.0f, scale);
    }

    public Transform(Vector2f position) {
        this(position, 0.0f, new Vector2f());
    }

    public Transform() {
        this(new Vector2f(), 0.0f, new Vector2f());
    }

    public Transform copy() {
        return new Transform(new Vector2f(position), rotation, new Vector2f(scale));
    }

    public void copyTo(Transform to) {
        to.position.set(this.position);
        to.rotation = this.rotation;
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Transform transform)) return false;

        return transform.position.equals(this.position) &&
                transform.rotation == this.rotation &&
                transform.scale.equals(this.scale);
    }
}
