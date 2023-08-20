package com.alcano.game.level;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera {

    public Vector4f backgroundColor;
    public Vector2f position;

    private final Matrix4f projectionMatrix, viewMatrix;

    public Camera(Vector2f position) {
        this.backgroundColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();

        this.adjustProjection();
    }

    public Camera() {
        this(new Vector2f());
    }

    public void adjustProjection() {
        this.projectionMatrix.setOrtho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

        this.viewMatrix.setLookAt(
                new Vector3f(this.position.x, this.position.y, 20.0f),
                cameraFront.add(this.position.x, this.position.y, 0.0f),
                cameraUp
        );

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
