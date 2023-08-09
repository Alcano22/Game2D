package com.alcano.game.text;

import com.alcano.game.core.Game;

public class Component {

    private static final Translator TRANSLATOR = Game.get().translator;

    private final String text;

    private Component(String text) {
        this.text = text;
    }

    public static Component literal(String text) {
        return new Component(text);
    }

    public static Component translatable(String translationKey) {
        return new Component(TRANSLATOR.translate(translationKey));
    }

    public String getString() {
        return this.text;
    }

}
