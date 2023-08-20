package com.alcano.game.util;

import com.alcano.game.core.Application;
import com.alcano.game.renderer.Shader;
import com.alcano.game.renderer.Sprite;
import com.alcano.game.renderer.SpriteSheet;
import com.alcano.game.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class AssetPool {

    private static final Map<String, File> FILES = new HashMap<>();
    private static final Map<String, Shader> SHADERS = new HashMap<>();
    private static final Map<String, Texture> TEXTURES = new HashMap<>();
    private static final Map<String, SpriteSheet> SPRITE_SHEETS = new HashMap<>();

    private AssetPool() {}

    public static File loadFile(String filepath) {
        if (FILES.containsKey(filepath)) {
            return FILES.get(filepath);
        }

        File file = new File(filepath);
        if (!file.exists()) {
            throw new RuntimeException("Unable to find '" + filepath + "'");
        }

        FILES.put(filepath, file);
        return file;
    }

    public static Shader loadShader(String filepath) {
        if (SHADERS.containsKey(filepath)) {
            return SHADERS.get(filepath);
        }

        Shader shader = new Shader(filepath);
        shader.compile();
        SHADERS.put(filepath, shader);
        return shader;
    }

    public static Texture loadTexture(String filepath) {
        if (TEXTURES.containsKey(filepath)) {
            return TEXTURES.get(filepath);
        }

        Texture texture = new Texture(filepath);
        TEXTURES.put(filepath, texture);
        return texture;
    }

    public static Sprite loadSprite(String filepath) {
        return new Sprite(loadTexture(filepath));
    }

    public static void addSpriteSheet(String filepath, int spriteWidth, int spriteHeight, int spacing) {
        if (!SPRITE_SHEETS.containsKey(filepath)) {
            SpriteSheet sheet = new SpriteSheet(
                    AssetPool.loadTexture(filepath), spriteWidth, spriteHeight, spacing);
            SPRITE_SHEETS.put(filepath, sheet);
        }
    }

    public static SpriteSheet loadSpriteSheet(String filepath) {
        if (!SPRITE_SHEETS.containsKey(filepath)) {
            Application.exitError("Tried to access Sprite Sheet '" + filepath +
                    "' although it has not been added to the Asset Pool");
        }

        return SPRITE_SHEETS.get(filepath);
    }

}
