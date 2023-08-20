package com.alcano.game.text;

import com.alcano.game.core.Game;

public class TextComponent {

    private static final Translator TRANSLATOR = Game.get().translator;

    private final String text;

    private TextComponent(String text) {
        this.text = text;
    }

    public static TextComponent literal(String text) {
        return new TextComponent(text);
    }

    public static TextComponent translatable(String translationKey) {
        return new TextComponent(TRANSLATOR.translate(translationKey));
    }

    public String getString() {
        return this.text;
    }

}
