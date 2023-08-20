package com.alcano.game.gameobject.component;

import com.alcano.game.gameobject.GameObject;
import com.alcano.game.gameobject.Transform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Component {

    public GameObject gameObject;

    public void start() {}

    public void update() {}

    public void imgui() {}

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SingleOnly {
    }

    public Transform getTransform() {
        return gameObject.transform;
    }
}
