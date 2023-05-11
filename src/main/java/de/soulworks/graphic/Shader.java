package de.soulworks.graphic;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int id;
    private int vertexId;
    private int fragmentId;

    public Shader() {
        this.id = glCreateProgram();
        if(this.id == 0) {
            throw new RuntimeException("Could not create shader");
        }
    }

    public void createVertexShader(String shaderCode) {
        vertexId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) {
        fragmentId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if(shaderId == 0) {
            throw new RuntimeException("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(id, shaderId);

        return shaderId;
    }

    public void link() {
        glLinkProgram(id);
        if(glGetProgrami(id, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking shader code: " + glGetProgramInfoLog(id, 1024));
        }

        if(vertexId != 0) {
            glDetachShader(id, vertexId);
        }
        if(fragmentId != 0) {
            glDetachShader(id, fragmentId);
        }

        glValidateProgram(id);
        if(glGetProgrami(id, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader code: " + glGetProgramInfoLog(id, 1024));
        }
    }

    public void bind() {
        glUseProgram(id);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if(id != 0) {
            glDeleteProgram(id);
        }
    }
}
