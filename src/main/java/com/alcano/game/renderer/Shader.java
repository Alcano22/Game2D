package com.alcano.game.renderer;

import com.alcano.game.core.Application;
import com.alcano.game.debug.Debug;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private static final String TYPE_PREFIX = "#type";

    private final String filepath;

    private int programId;
    private String vertexSource;
    private String fragmentSource;
    private boolean used;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitSource = source.split("(" + TYPE_PREFIX + ")( )+([a-zA-Z]+)");

            // Find the first pattern after #type 'pattern'
            int index = source.indexOf(TYPE_PREFIX) + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // Find the second pattern after #type 'pattern'
            index = source.indexOf(TYPE_PREFIX, eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                this.vertexSource = splitSource[1];
            } else if (firstPattern.equals("fragment")) {
                this.fragmentSource = splitSource[1];
            } else {
                Application.exitError("Unexpected token '" + firstPattern + "' in '" + filepath + "'");
            }

            if (secondPattern.equals("vertex")) {
                this.vertexSource = splitSource[2];
            } else if (secondPattern.equals("fragment")) {
                this.fragmentSource = splitSource[2];
            } else {
                Application.exitError("Unexpected token '" + secondPattern + "' in '" + filepath + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Debug.logError("Could not open file for shader '" + filepath + "'");
        }
    }

    public void compile() {
        int vertexId, fragmentId;

        // First load and compile the vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(vertexId, this.vertexSource);
        glCompileShader(vertexId);

        // Check for errors in compilation
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            Application.exitError("Shader: '" + this.filepath + "': Vertex shader compilation failed.",
                    glGetShaderInfoLog(vertexId, len));
        }

        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentId, this.fragmentSource);
        glCompileShader(fragmentId);

        // Check for errors in compilation
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            Application.exitError("Shader: '" + this.filepath + "': Fragment shader compilation failed.",
                    glGetShaderInfoLog(fragmentId, len));
        }

        // Link shaders and check for errors
        this.programId = glCreateProgram();
        glAttachShader(this.programId, vertexId);
        glAttachShader(this.programId, fragmentId);
        glLinkProgram(this.programId);

        success = glGetProgrami(this.programId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(this.programId, GL_INFO_LOG_LENGTH);
            Application.exitError("Shader: '" + this.filepath + "': Linking of shaders failed.",
                    glGetProgramInfoLog(this.programId, len));
        }
    }

    public void use() {
        if (!this.used) {
            // Bind shader program
            glUseProgram(this.programId);
            this.used = true;
        }
    }

    public void detach() {
        if (this.used) {
            glUseProgram(0);
            this.used = false;
        }
    }

    public void uploadMat4f(String varName, Matrix4f mat4f) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4f.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3f) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3f.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadMat2f(String varName, Matrix2f mat2f) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4);
        mat2f.get(matBuffer);
        glUniformMatrix2fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec4f) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        glUniform4f(varLocation, vec4f.x, vec4f.y, vec4f.z, vec4f.w);
    }

    public void uploadVec3f(String varName, Vector3f vec3f) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        glUniform3f(varLocation, vec3f.x, vec3f.y, vec3f.z);
    }

    public void uploadVec2f(String varName, Vector2f vec2f) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        glUniform2f(varLocation, vec2f.x, vec2f.y);
    }

    public void uploadVec4i(String varName, Vector4i vec4i) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        glUniform4i(varLocation, vec4i.x, vec4i.y, vec4i.z, vec4i.w);
    }

    public void uploadVec3i(String varName, Vector3i vec3i) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        glUniform3i(varLocation, vec3i.x, vec3i.y, vec3i.z);
    }

    public void uploadVec2i(String varName, Vector2i vec2i) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        glUniform2i(varLocation, vec2i.x, vec2i.y);
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot) {
        this.uploadInt(varName, slot);
    }

    public void uploadIntArray(String varName, int[] array) {
        int varLocation = glGetUniformLocation(this.programId, varName);

        this.use();

        glUniform1iv(varLocation, array);
    }

}
