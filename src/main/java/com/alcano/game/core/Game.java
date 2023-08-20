package com.alcano.game.core;

import com.alcano.game.level.SceneManager;
import com.alcano.game.text.Translator;

public class Game {

    private static Game instance;

    public final Window window;
    public final SceneManager sceneManager;
    public final Translator translator;

    private Game() {
        instance = this;

        this.window = Window.get();
        this.sceneManager = new SceneManager();
        this.translator = new Translator();

        window.run();
    }

    public void init() {
        this.sceneManager.loadLevel(0);
    }

    public static Game get() {
        return instance;
    }

    public void update() {
        this.sceneManager.update();
    }

    public static void main(String[] args) {
        new Game();
    }

}
