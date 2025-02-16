package com.demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private FitViewport viewport;
    private Shape shape;
    private Shape shape2;
    private int renderMode = 0;

    @Override
    public void create() {
        viewport = new FitViewport(1200, 1200);
        Engine.addCircle(new Vector2(50, 50),15, 32, 1, true);
        Engine.addCircle(new Vector2(100, 100),15, 32, 1, true);
        Engine.addWalls(650, 600);

        Engine.addCircle(new Vector2(150, 150),10, 32, 1, true);
        Engine.addCircle(new Vector2(200, 200),10, 32, 1, true);
        Engine.addCircle(new Vector2(250, 250),10, 32, 1, true);
//        Engine.addCircle(new Vector2(350, 350),10, 50, 1, true);
//        Engine.addCircle(new Vector2(400, 400),10, 50, 1, true);
//        Engine.addCircle(new Vector2(450, 450),10, 50, 1, true);
    }

    public void logic(){
        Vector2 gravity = new Vector2(0, -240);
        Engine.logic(gravity, 8);
    }

    @Override
    public void render() {
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            renderMode = 0;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            renderMode = 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            renderMode = 2;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            renderMode = 3;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            renderMode = 4;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            renderMode = 5;
        }

        if (renderMode == 0){
            Animation.circleNoSpringAnimation();
        }
        else if (renderMode == 1){
            Animation.circleWithSpringAnimation();
        }
        else if (renderMode == 2){
            Animation.pressureAnimation();
        }
        else if (renderMode == 3){
            Animation.pointLineDistanceAnimation();
        }
        else if (renderMode == 4){
            Animation.collisionAnimation();
        }
        else if (renderMode == 5){
            Engine.input();
            logic();
            Engine.drawWireframe();
        }
    }

    @Override
    public void dispose() {}
}
