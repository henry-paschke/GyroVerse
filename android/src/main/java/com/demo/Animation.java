package com.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class Animation {
    public static ArrayList<Point> contactPoints = new ArrayList<>();
    public static float totalPressure = 1;
    public static Rectangle collisionSquareOne = new Rectangle(new Vector2(300, 250), 100, 100, 1, false);
    public static Rectangle collisionSquareTwo = new Rectangle(new Vector2(500, 200), 100, 100, 1, true);
    public static Circle staticCircle = new Circle(new Vector2(300, 250), 100, 20, 1, true);
    public static void pointLineDistanceAnimation(){
        Point p1 = new Point(new Vector2(100, 100), false);
        Point p2 = new Point(new Vector2(200, 250), false);

        float x = Gdx.input.getX();
        float y = Gdx.input.getY();
        y *= -1;
        y += Gdx.graphics.getHeight();

        Point p3 = new Point(new Vector2(x, y), true);

        Object[] data = CollisionDetection.pointLineDistance(p1, p2, p3);
        Vector2 cp = (Vector2) data[1];

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Engine.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Engine.shapeRenderer.setColor(Color.WHITE);
        Engine.shapeRenderer.circle(p1.getOrigin().x, p1.getOrigin().y, 10);
        Engine.shapeRenderer.circle(p2.getOrigin().x, p2.getOrigin().y, 10);
        Engine.shapeRenderer.line(p1.getOrigin(), p2.getOrigin());
        Engine.shapeRenderer.circle(p3.getOrigin().x, p3.getOrigin().y, 15);
        Engine.shapeRenderer.setColor(Color.RED);
        Engine.shapeRenderer.circle(cp.x, cp.y, 15);
        Engine.shapeRenderer.end();
    }

    public static void pressureAnimation(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        staticCircle.calculateSpringForce(1, 0.3f);
        staticCircle.calculatePressureForce(totalPressure);
        staticCircle.integrate(Gdx.graphics.getDeltaTime());
        staticCircle.draw(Engine.shapeRenderer);
        while (totalPressure < 60) {
            totalPressure += 0.5f;
        }
    }

    public static void collisionAnimation(){
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

        collisionSquareTwo.updateVelocity(new Vector2(dx, dy));

        CollisionDetection.checkCollision(collisionSquareOne, collisionSquareTwo);

        for (Point point : collisionSquareOne.getPoints()){
            if (point.getCollisionInfo().collide){
                contactPoints.add(point);
            }
        }

        for (Point point : collisionSquareTwo.getPoints()){
            if (point.getCollisionInfo().collide){
                contactPoints.add(point);
            }
        }

        collisionSquareTwo.integrate(Gdx.graphics.getDeltaTime());

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Engine.shapeRenderer.setColor(Color.WHITE);
        collisionSquareOne.draw(Engine.shapeRenderer);
        collisionSquareTwo.draw(Engine.shapeRenderer);

        Engine.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Engine.shapeRenderer.setColor(Color.GREEN);
        // Draw contact points
        for (Point point : contactPoints) {
            Engine.shapeRenderer.circle(point.getOrigin().x, point.getOrigin().y, 5);
        }
        Engine.shapeRenderer.end();

        Engine.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Engine.shapeRenderer.setColor(Color.RED);
        // Draw collision edges
        for (Point point : contactPoints) {
            Engine.shapeRenderer.line(point.getCollisionInfo().edgeVertexOne.getOrigin(), point.getCollisionInfo().edgeVertexTwo.getOrigin());
        }
        Engine.shapeRenderer.end();

        Engine.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Engine.shapeRenderer.setColor(Color.BLUE);
        // Draw collision edges
        for (Point point : contactPoints) {
            Engine.shapeRenderer.circle(point.getCollisionInfo().closestPoint.x, point.getCollisionInfo().closestPoint.y, 5);
        }
        Engine.shapeRenderer.end();

        Engine.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Engine.shapeRenderer.setColor(Color.GREEN);
        for (Point point : contactPoints) {
            System.out.println(point.getCollisionInfo().normal);
            if (point.getCollisionInfo().normal.x > 0) {
                Engine.shapeRenderer.line(new Vector2(point.getOrigin().x, point.getOrigin().y - 20), new Vector2(point.getOrigin().x - 120, point.getOrigin().y - 20));
                Engine.shapeRenderer.line(new Vector2(point.getOrigin().x - 120, point.getOrigin().y - 20), new Vector2(point.getOrigin().x - 100, point.getOrigin().y + 0));
                Engine.shapeRenderer.line(new Vector2(point.getOrigin().x - 120, point.getOrigin().y - 20), new Vector2(point.getOrigin().x - 100, point.getOrigin().y - 40));

            }
            else if (point.getCollisionInfo().normal.x < 0) {
                Engine.shapeRenderer.line(new Vector2(point.getOrigin().x, point.getOrigin().y + 20), new Vector2(point.getOrigin().x + 120, point.getOrigin().y + 20));
                Engine.shapeRenderer.line(new Vector2(point.getOrigin().x + 120, point.getOrigin().y + 20), new Vector2(point.getOrigin().x + 100, point.getOrigin().y - 0));
                Engine.shapeRenderer.line(new Vector2(point.getOrigin().x + 120, point.getOrigin().y + 20), new Vector2(point.getOrigin().x + 100, point.getOrigin().y + 40));
            }
        }
        Engine.shapeRenderer.end();


        contactPoints.clear();
    }

    public static void circleNoSpringAnimation(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Engine.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Engine.shapeRenderer.setColor(Color.WHITE);
        for (Point point : staticCircle.getPoints()){
            Engine.shapeRenderer.circle(point.getOrigin().x, point.getOrigin().y, 5);
        }
        Engine.shapeRenderer.end();
    }

    public static void circleWithSpringAnimation(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Engine.shapeRenderer.setColor(Color.WHITE);
        staticCircle.draw(Engine.shapeRenderer);
    }
}
