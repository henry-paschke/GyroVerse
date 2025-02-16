package com.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

public class PlanetRenderer {

    Mesh mesh;
    float[] vertices;
    int vertexCount;
    public int planetID;
    float time = 0.0f;
    public Shape shape;
    public float radius;

    public Rings rings = null;

    static ShaderProgram shader = null;

    public PlanetRenderer(int vertexCount, float radius, int planetID, Shape shape) {
        this.radius = radius;
        this.vertexCount = vertexCount;
        this.planetID = planetID;
        this.shape = shape;
        vertices = new float[(vertexCount + 2) * 4];

        prepVertices(radius);

        mesh = new Mesh(false, vertexCount + 2, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_coord"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord")
                );

        if (shader == null) {
            shader = new ShaderProgram(
                    Gdx.files.internal("planetVert.glsl"),
                    Gdx.files.internal("planetFrag.glsl")
            );

            Gdx.gl.glEnable(GL20.GL_BLEND);   // Enable blending
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); // Set the blend function

        }
    }

    public void prepVertices(float radius) {
        if (vertexCount < 3) {
            throw new IllegalArgumentException("n must be at least 3 for an n-gon.");
        }

        float angleStep = 360.0f / vertexCount; // Angle between each vertex

        vertices[0] = 0.0f;
        vertices[1] = 0.0f;
        vertices[2] = 0.5f;
        vertices[3] = 0.5f;

        // Generate vertices
        for (int i = 0; i < vertexCount; i++) {
            float angle = i * angleStep; // Angle for the current vertex

            // Convert to radians (since trig functions expect radians)
            float rad = MathUtils.degreesToRadians * angle;

            // Calculate the position of the vertex
            float x = radius * MathUtils.cos(rad);
            float y = radius * MathUtils.sin(rad);

            float x_tx = 0.5f + 0.5f * MathUtils.cos(rad);  // x from -1 to 1 mapped to 0 to 1
            float y_tx = 0.5f + 0.5f * MathUtils.sin(rad);  // y from -1 to 1 mapped to 0 to 1

            // Set the vertex position in the mesh (assuming a mesh with n vertices)
            setVertex(i, x, y);
            setTexcoord(i, x_tx, y_tx);
        }
        Gdx.app.log("VERTS", Arrays.toString(vertices));
    }

    public void copyVertices(){
        Vector2[] vertices = shape.getVertices();

        for (int i = 0;  i < vertices.length; i ++) {
            setVertex(i, vertices[i].x, vertices[i].y);
        }

        this.vertices[0] = shape.getOrigin().x;
        this.vertices[1] = shape.getOrigin().y;

    }

    public void setVertex(int index, float x, float y) {
        if (index == 0) {
            setVertex(this.vertexCount, x, y);
        } else if (index > this.vertexCount) {
            throw new IndexOutOfBoundsException("out of bounds vertex");
        }
        vertices[(index+1) * 4] = x;
        vertices[((index+1) * 4) + 1] = y;
    }

    public void setTexcoord(int index, float x, float y) {
        if (index == 0) {
            setTexcoord(this.vertexCount, x, y);
        } else if (index > this.vertexCount) {
            throw new IndexOutOfBoundsException("out of bounds vertex");
        }
        vertices[((index+1) * 4) + 2] = x;
        vertices[((index+1) * 4) + 3] = y;
    }

    public void render(Camera camera) {
        if (rings != null) {
            rings.preRender(camera);
        }
        time += Gdx.graphics.getDeltaTime();
        copyVertices();
        mesh.setVertices(vertices);
        shader.bind();
        shader.setUniformf("iTime", time);
        shader.setUniformi("planetID", planetID);
        shader.setUniformMatrix("u_proj", camera.combined);
        mesh.render(shader, GL20.GL_TRIANGLE_FAN);
        if (rings != null) {
            rings.postRender(camera);
        }
    }
}
