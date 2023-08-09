package com.alcano.game.core;

import com.alcano.game.level.LevelManager;
import com.alcano.game.text.Component;
import com.alcano.game.text.Language;
import com.alcano.game.text.Translator;

public class Game {

    private static Game instance;

    public final Window window;
    public final LevelManager levelManager;
    public final Translator translator;

    private Game() {
        instance = this;

        this.window = Window.get();
        this.levelManager = new LevelManager();
        this.translator = new Translator();

        window.run();
    }

    public static Game get() {
        return instance;
    }

    public static void main(String[] args) {
        new Game();
    }

}
