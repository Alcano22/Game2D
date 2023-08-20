package com.alcano.game.text;

import com.alcano.game.util.AssetPool;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Translator {

    private final Map<Language, Map<String, String>> translations;

    private Language currentLanguage;

    public Translator() {
        this.translations = new HashMap<>();
        this.loadTranslation(Language.ENGLISH);
    }

    public void loadTranslation(Language language) {
        this.currentLanguage = language;

        try {
            JsonObject root = JsonParser.parseReader(new FileReader(
                    this.getTranslationFile(language.key))).getAsJsonObject();

            Map<String, JsonElement> rootMap = root.asMap();
            Map<String, String> translation = new HashMap<>();
            for (String key : rootMap.keySet()) {
                String value = rootMap.get(key).getAsString();

                translation.put(key, value);
            }

            this.translations.put(language, translation);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String translate(String translationKey) {
        String languageKey = this.currentLanguage.key;

        Map<String, String> translation = this.translations.get(this.currentLanguage);

        if (!translation.containsKey(translationKey)) {
            throw new RuntimeException(
                    "Unable to find '" + translationKey + "' in '" + languageKey + "'");
        }

        return translation.get(translationKey);
    }

    public File getTranslationFile(String languageKey) {
        return AssetPool.loadFile("assets/lang/" + languageKey + ".json");
    }

}
