package com.alcano.game.renderer;

import com.alcano.game.debug.Debug;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {

    private final Texture texture;
    private final List<Sprite> sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int spacing) {
        this.texture = texture;
        this.sprites = new ArrayList<>();

        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        int numSprites = (texture.getWidth() / (spriteWidth + spacing)) *
                (texture.getHeight() / (spriteHeight + spacing));
        Debug.log(numSprites);
        for (int i = 0; i < numSprites; i++) {
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            this.sprites.add(sprite);

            currentX += spriteWidth;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }

}
