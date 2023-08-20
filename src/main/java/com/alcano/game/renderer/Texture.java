package com.alcano.game.renderer;

import com.alcano.game.core.Application;
import com.alcano.game.debug.Debug;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private final String filepath;
    private final int texId;

    private int width;
    private int height;
    private boolean empty;

    public Texture(String filepath) {
        this.filepath = filepath;

        // Generate texture on GPU
        this.texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.texId);

        // Set texture parameters
        // Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // Pixelate the image when stretching and shrinking
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(this.filepath, width, height, channels, 0);

        int format = switch (channels.get(0)) {
            case 4 -> GL_RGBA;
            case 3 -> GL_RGB;
            default -> {
                Application.exitError("Unknown number of channels '" + channels.get(0) + "'");
                yield -1;
            }
        };

        if (image != null) {
            this.width = width.get(0);
            this.height = height.get(0);
            this.empty = this.isEmpty(image, this.width, this.height, channels.get(0));

            glTexImage2D(GL_TEXTURE_2D, 0, format,
                    width.get(0), height.get(0), 0, format, GL_UNSIGNED_BYTE, image);
        } else {
            Application.exitError("Could not load image '" + this.filepath + "'");
        }

        stbi_image_free(image);
    }

    private boolean isEmpty(ByteBuffer image, int width, int height, int channels) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelIndex = (y * width + x) * channels;

                if (channels >= 4) { // Assuming RGBA format (4 bytes per pixel)
                    int alpha = image.get(pixelIndex + 3) & 0xFF; // Extract alpha value
                    if (alpha > 0) {
                        return false; // Found a non-transparent pixel
                    }
                } else {
                    // For RGB format (3 bytes per pixel), assume no alpha channel
                    int red = image.get(pixelIndex) & 0xFF; // Extract red value
                    int green = image.get(pixelIndex + 1) & 0xFF; // Extract green value
                    int blue = image.get(pixelIndex + 2) & 0xFF; // Extract blue value
                    if (red > 0 || green > 0 || blue > 0) {
                        return false; // Found a non-black pixel
                    }
                }
            }
        }

        return true; // All pixels are transparent or black
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.texId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isEmpty() {
        return empty;
    }
}
