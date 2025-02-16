package com.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;

public class PlanetContainer {
    Array<PlanetRenderer> planets;
    OrthographicCamera camera;

    public PlanetContainer() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        planets = new Array<>();
    }

    public void add(PlanetRenderer r) {
        planets.add(r);
        if (r.planetID == 4 || r.planetID == 6) {
            r.rings = new Rings(r);
        }
    }

    public void render() {
        for (PlanetRenderer p : planets) {
            p.render(camera);
        }
    }

    public void clear() {
        planets.clear();
    }
}
