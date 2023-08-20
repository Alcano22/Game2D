package com.alcano.game.gameobject;

import com.alcano.game.debug.Debug;
import com.alcano.game.gameobject.component.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    public final int zIndex;

    private final List<Component> components;

    public Transform transform;

    private String name;

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.transform = transform;
        this.zIndex = zIndex;
        this.components = new ArrayList<>();
    }

    public GameObject(String name, Transform transform) {
        this(name, transform, 0);
    }

    public GameObject(String name) {
        this(name, new Transform(), 0);
    }

    public void start() {
        this.components.forEach(Component::start);
    }

    public void update() {
        this.components.forEach(Component::update);
    }

    public void imgui() {
        this.components.forEach(Component::imgui);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : this.components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                return componentClass.cast(component);
            }
        }

        return null;
    }

    public void addComponent(Component component) {
        if (this.getComponent(component.getClass()) != null &&
                component.getClass().isAnnotationPresent(Component.SingleOnly.class)) {
            Debug.logError("Cannot add '" + component.getClass().getSimpleName() +
                    "' multiple times because it is a Single-Only component!");
            return;
        }

        this.components.add(component);
        component.gameObject = this;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < this.components.size(); i++) {
            Component component = this.components.get(i);
            if (componentClass.isAssignableFrom(component.getClass())) {
                this.components.remove(i);
                return;
            }
        }
    }
}
