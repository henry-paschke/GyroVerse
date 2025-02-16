package com.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class WallpaperScene {
    BackgroundRenderer BG;
    PlanetContainer PC;

    Vector2 g = new Vector2(0.0f, -90.0f);

    float timer = 0.0f;
    public WallpaperScene() {

        BG = new BackgroundRenderer();
        PC = new PlanetContainer();

        setup();
    }

    public void setup() {
        float[] planetSizes = {
                20.0f,   // Mercury
                24.68f,  // Venus
                25.19f,  // Earth
                20.95f,  // Mars
                110.0f,  // Jupiter
                93.18f,  // Saturn
                51.29f,  // Uranus
                50.74f   // Neptune
        };

        for (int i = 0; i <= 7; i++) {
            spawnShape(i, planetSizes[i], new Vector2(-500 + 1000 * (i / 8.0f), -1000 + 2000 * (i / 8.0f)), 1.0f);
        }

        Engine.addWalls(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void spawnShape(int ID, float radius, Vector2 position, float mass) {
        Circle newShape = new Circle(position, radius, 24, mass, true);

        PlanetRenderer r = new PlanetRenderer(24, radius, ID, newShape);
        PC.add(r);

        Engine.addCircle(newShape);
    }

    public void render() {


        Engine.logic(new Vector2(Gdx.input.getRoll(), Gdx.input.getPitch()), 8);
        BG.render();
        PC.render();
    }

    public void reset() {
        Engine.clear();
        PC.clear();

        setup();
    }
}
