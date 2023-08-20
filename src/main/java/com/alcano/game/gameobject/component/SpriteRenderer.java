package com.alcano.game.gameobject.component;

import com.alcano.game.gameobject.Transform;
import com.alcano.game.renderer.Sprite;
import com.alcano.game.renderer.Texture;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Sprite sprite = ;
    private Vector4f color = ;

    private transient Transform lastTransform;
    private transient boolean isDirty;

    public static SpriteRenderer init(Sprite sprite, Vector4f color) {
        SpriteRenderer instance = new SpriteRenderer();
        instance.sprite
    }

    @Override
    public void start() {
        this.lastTransform = this.getTransform().copy();
    }

    @Override
    public void update() {
        if (!this.lastTransform.equals(this.getTransform())) {
            this.getTransform().copyTo(this.lastTransform);
            this.isDirty = true;
        }
    }

    @Override
    public void imgui() {
        float[] imColor = {this.color.x, this.color.y, this.color.z, this.color.w};
        if (ImGui.colorPicker4("Color Picker:", imColor)) {
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            this.isDirty = true;
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.color.set(color);
            this.isDirty = true;
        }
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setClean() {
        this.isDirty = false;
    }

    public boolean isDirty() {
        return isDirty;
    }
}
