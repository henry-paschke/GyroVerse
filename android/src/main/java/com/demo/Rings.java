package com.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Rings {
    Mesh mesh;
    float[] vertices;
    PlanetRenderer planet;

    static ShaderProgram shader = null;

    public Rings (PlanetRenderer planet) {
        float radius;
        if (planet.radius < 80.0f) {
            radius = planet.radius *  12.0f;
        } else  {
            radius = planet.radius *  8.0f;
        }

        this.planet = planet;
        vertices = new float[]{
                -radius * 0.5f, -radius * 0.5f, 0.0f, 0.0f,
                -radius * 0.5f, radius* 0.5f, 0.0f, 1.0f,
                radius* 0.5f, radius * 0.5f, 1.0f, 1.0f,
                radius* 0.5f, -radius * 0.5f, 1.0f, 0.0f,
        };

        short[] indices = {
                0, 1, 2, 0, 2, 3
        };

        mesh = new Mesh(true, 4, 6,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_coord"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord")
        );

        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        if (shader == null) {
            shader = new ShaderProgram(
                    Gdx.files.internal("ringVert.glsl"),
                    Gdx.files.internal("ringFrag.glsl")
            );
        }
    }

    public Matrix4 createModelMatrix() {
        Matrix4 mat = new Matrix4();
        mat.setToRotation(new Vector3(0, 0, 1), planet.shape.getRotation());
        mat.trn(planet.shape.getOrigin().x, planet.shape.getOrigin().y, 0.0f); // Apply translation
        return mat;
    }
    public void postRender(Camera camera) {
        mesh.setVertices(vertices);
        shader.bind();
        shader.setUniformi("planetID", planet.planetID);
        shader.setUniformMatrix("u_proj", camera.combined);
        shader.setUniformMatrix("u_model", createModelMatrix());
        shader.setUniformi("flip", 0);
        mesh.render(shader, GL20.GL_TRIANGLES);
    }

    public void preRender(Camera camera) {
        mesh.setVertices(vertices);
        shader.bind();
        shader.setUniformi("planetID", planet.planetID);
        shader.setUniformMatrix("u_proj", camera.combined);
        shader.setUniformMatrix("u_model", createModelMatrix());
        shader.setUniformi("flip", 1);
        mesh.render(shader, GL20.GL_TRIANGLES);
    }
}
