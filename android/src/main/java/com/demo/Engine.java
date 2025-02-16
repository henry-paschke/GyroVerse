package com.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class Engine {
    public static ArrayList<Shape> shapes = new ArrayList<>();
    public static ShapeRenderer shapeRenderer = new ShapeRenderer();
    public static int playerShape = 0;

    public static void clear() {
        shapes.clear();
    }
    public static void addCircle(Vector2 origin, float radius, int numPoints, float mass, boolean moveable){
        shapes.add(new Circle(origin, radius, numPoints, mass, moveable));
    }

    public static void addCircle(Circle c){
        shapes.add(c);
    }


    public static void addRectangle(Vector2 origin, float width, float height, float mass, boolean moveable){
        shapes.add(new Rectangle(origin, width, height, mass, moveable));
    }

    public static void addWalls(float screenWidth, float screenHeight){
        float wallThickness = Math.max(screenHeight, screenWidth);
        shapes.add(new Rectangle(new Vector2(screenWidth / 2.0f + wallThickness / 2.0f, 0), wallThickness, wallThickness, 1, false));
        shapes.add(new Rectangle(new Vector2(-screenWidth / 2.0f - wallThickness / 2.0f, 0), wallThickness, wallThickness, 1, false));
        shapes.add(new Rectangle(new Vector2(0, screenHeight / 2.0f + wallThickness / 2.0f), wallThickness, wallThickness,1, false));
        shapes.add(new Rectangle(new Vector2(0, -screenHeight / 2.0f - wallThickness / 2.0f), wallThickness, wallThickness, 1, false));
    }

    public static void input(){
        float speed = 10;
        float dx = 0;
        float dy = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx += speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            dy += speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            dy -= speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx -= speed;
        }
        shapes.get(playerShape).updateVelocity(new Vector2(dx, dy));
    }

    public static void logic(Vector2 gravity, int iterations){
        float timeStep = Gdx.graphics.getDeltaTime() / iterations;

        for (Shape shape : shapes){
            shape.calculateResistance(0.99f);
        }

        for (int index = 0; index < iterations; index++){
            for (Shape shape : shapes) {
                if (shape.isMoveable()) {
                    shape.addGravity(gravity, timeStep * iterations);

                    shape.calculateSpringForce(5, 0.5f);
                    shape.calculatePressureForce(500);

                    shape.integrate(timeStep);
                }
            }

            for (int outer = 0; outer < shapes.size() - 1; outer++){
                for (int inner = outer + 1; inner < shapes.size(); inner++){
                    Shape shapeOne = shapes.get(outer);
                    Shape shapeTwo = shapes.get(inner);

                    CollisionDetection.checkCollision(shapeOne, shapeTwo);
                    CollisionResolution.displaceShapes(shapeOne, shapeTwo);
                    CollisionResolution.applyImpulse(shapeOne, shapeTwo);
                }
            }

        }
    }

    public static void drawWireframe(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        shapeRenderer.setColor(Color.WHITE);

        for (Shape shape : shapes){
            shape.draw(shapeRenderer);
        }
    }
}
