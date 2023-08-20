package com.alcano.game.renderer;

import com.alcano.game.core.Game;
import com.alcano.game.gameobject.component.SpriteRenderer;
import com.alcano.game.level.Camera;
import com.alcano.game.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class RenderBatch implements Comparable<RenderBatch> {

    // Vertex
    // ======
    // Pos                  Color                           Tex Coords          Tex Id
    // float, float,        float, float, float, float,     float, float,       float
    private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 4;
    private static final int TEX_COORDS_SIZE = 2;
    private static final int TEX_ID_SIZE = 1;
    private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private static final int POS_OFFSET = 0;
    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private static final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private static final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;

    private static final int[] TEX_SLOTS = { 0, 1, 2, 3, 4, 5, 6, 7 };

    public final int zIndex;

    private final int maxBatchSize;
    private final SpriteRenderer[] sprites;
    private final List<Texture> textures;
    private final Shader shader;
    private final float[] vertices;

    private int numSprites;
    private boolean hasRoom;

    private int vaoId, vboId;

    public RenderBatch(int maxBatchSize, int zIndex) {
        this.maxBatchSize = maxBatchSize;
        this.zIndex = zIndex;
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.textures = new ArrayList<>();

        this.shader = AssetPool.loadShader("assets/shaders/default.glsl");
        this.shader.compile();

        // 4 vertices quads
        this.vertices = new float[this.maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
    }

    public void start() {
        // Generate and bind Vertex Array Object
        this.vaoId = glGenVertexArrays();
        glBindVertexArray(this.vaoId);

        // Allocate space for vertices
        this.vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) this.vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboId = glGenBuffers();
        int[] indices = this.generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void render() {
        boolean rebufferData = false;
        for (int i = 0; i < this.numSprites; i++) {
            SpriteRenderer sprite = this.sprites[i];
            if (!sprite.isDirty()) continue;

            this.loadVertexProperties(i);
            sprite.setClean();
            rebufferData = true;
        }

        if (rebufferData) {
            glBindBuffer(GL_ARRAY_BUFFER, this.vboId);
            glBufferSubData(GL_ARRAY_BUFFER, 0, this.vertices);
        }

        Camera camera = Game.get().sceneManager.getCurrentScene().camera;

        // Use shader
        this.shader.use();
        this.shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        this.shader.uploadMat4f("uView", camera.getViewMatrix());

        for (int i = 0; i < this.textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            this.textures.get(i).bind();
        }
        this.shader.uploadIntArray("uTextures", TEX_SLOTS);

        glBindVertexArray(this.vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        this.textures.forEach(Texture::unbind);
        this.shader.detach();
    }

    public void addSprite(SpriteRenderer sprite) {
        // Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = sprite;
        this.numSprites++;

        if (sprite.getTexture() != null && !this.textures.contains(sprite.getTexture())) {
            this.textures.add(sprite.getTexture());
        }

        // Add properties to local vertices array
        this.loadVertexProperties(index);

        if (this.numSprites < this.maxBatchSize) return;

        this.hasRoom = false;
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        int texId = 0;
        if (sprite.getTexture() != null) {
            for (int i = 0; i < this.textures.size(); i++) {
                if (this.textures.get(i) != sprite.getTexture()) continue;

                texId = i + 1;
                break;
            }
        }

        // Add vertex with the appropriate properties
        float addX = 1.0f;
        float addY = 1.0f;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1 -> addY = 0.0f;
                case 2 -> addX = 0.0f;
                case 3 -> addY = 1.0f;
            }

            // Load position
            vertices[offset] = sprite.gameObject.transform.position.x +
                    (addX * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y +
                    (addY * sprite.gameObject.transform.scale.y);

            // Load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // Load tex coords
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            // Load tex id
            vertices[offset + 8] = texId;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 indices per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            this.loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return hasRoom;
    }

    public boolean hasTextureRoom() {
        return this.textures.size() < 8;
    }

    public boolean hasTexture(Texture tex) {
        return this.textures.contains(tex);
    }

    @Override
    public int compareTo(RenderBatch other) {
        return Integer.compare(this.zIndex, other.zIndex);
    }
}
