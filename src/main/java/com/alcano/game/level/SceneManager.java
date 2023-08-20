package com.alcano.game.level;

import com.alcano.game.debug.Debug;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private static final Map<Integer, Class<? extends Scene>> SCENE_INDEX_MAP = Collections.unmodifiableMap(
            new HashMap<>() {{
                this.put(0, LevelEditorScene.class);
            }});

    private Scene currentScene;

    public void loadLevel(int levelIndex) {
        try {
            Scene scene = (Scene) SCENE_INDEX_MAP.get(levelIndex).getDeclaredConstructors()[0].newInstance();
            scene.init();
            scene.start();
            this.currentScene = scene;

            Debug.logInfo("Loaded scene '" + scene.name + "'");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        if (this.currentScene == null) return;

        this.currentScene.update();
    }

    public Scene getCurrentScene() {
        return currentScene;
    }
}
