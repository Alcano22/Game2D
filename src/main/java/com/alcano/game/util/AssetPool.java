package com.alcano.game.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public final class AssetPool {

    private static final Map<String, File> FILES = new HashMap<>();

    private AssetPool() {}

    public static File loadFile(String path) {
        if (FILES.containsKey(path)) {
            return FILES.get(path);
        }

        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("Unable to find '" + path + "'");
        }

        FILES.put(path, file);
        return file;
    }

}
