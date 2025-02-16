package com.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BackgroundRenderer {
    Mesh mesh;
    ShaderProgram shader;
    float time = 0.0f;
    public BackgroundRenderer() {
        float[] vertices = {
                -1.0f, -1.0f,  // Bottom-left
                1.0f, -1.0f,  // Bottom-right
                1.0f,  1.0f,  // Top-right
                -1.0f,  1.0f   // Top-left
        };

        short[] indices = {
                0, 1, 2, 0, 2, 3
        };

        mesh = new Mesh(true, 4, 6,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position")
        );

        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        shader = new ShaderProgram(
                Gdx.files.internal("bgVert.glsl"),
                Gdx.files.internal("bgFrag.glsl")
        );

        if (!shader.isCompiled()) {
            Gdx.app.error("Shader", "Shader compilation failed: " + shader.getLog());
        }
    }

    public void render() {
        time += Gdx.graphics.getDeltaTime();
        shader.bind();
        shader.setUniformf("iTime", time);
        mesh.render(shader, GL20.GL_TRIANGLES);
    }
}
