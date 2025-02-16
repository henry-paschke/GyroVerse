package com.demo;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Vector;

public class Shape {
    protected ArrayList<Point> points;
    private ArrayList<Spring> springs;
    private float mass;
    private boolean moveable;

    protected ArrayList<Point> originalPoints;

    public Shape(ArrayList<Vector2> vertices, float mass, boolean moveable){    points = new ArrayList<>();    originalPoints = new ArrayList<>();    springs = new ArrayList<>();    this.mass = mass;    this.moveable = moveable;    for (Vector2 vertex : vertices){        points.add(new Point(vertex, moveable));        originalPoints.add(new Point(vertex, moveable));    }    this.addSprings();}

    public float getRotation() {
        Vector2 origin = this.getOrigin();
//        float total = 0.0f;
//        for (int i = 0; i < points.size(); i++) {
//            total += origin.sub(getVertices()[i]).angleDeg();
//        }
//
//        return -(total / points.size());
        return origin.sub(getVertices()[0]).angleDeg();
    }


    public void addSprings(){
        for (int index = 0; index < this.points.size(); index++){
            springs.add(new Spring(this.points.get(index), this.points.get((index + 1) % this.points.size())));
        }
    }

    public boolean isMoveable(){
        return this.moveable;
    }

    public float getMass(){
        if (this.moveable){
            return this.mass;
        }
        return 0;
    }

    public float getInverseMass(){
        if (this.moveable){
            return 1 / this.mass;
        }
        return 0;
    }

    public void updateVelocity(Vector2 velocity){
        for (Point point : this.points){
            point.updateVelocity(velocity);
        }
    }

    public void calculateSpringForce(float strength, float dampening){
        for (Spring spring : this.springs){
            spring.calculateSpringForce(strength, dampening);
        }
    }

    public void calculatePressureForce(float pressure){
        float area = 0;
        for (Spring spring : this.springs){
            area += spring.calculateArea();
        }

        for (Spring spring : this.springs){
            spring.calculatePressureForce(area, pressure);
        }
    }

    public void calculateResistance(float resistance){
        for (Point point : this.points){
            point.calculateResistance(resistance);
        }
    }

    public void setAll(Vector2 update){
        for (Point point : this.points){
            point.setOrigin(update);
        }
    }

    public Vector2[] getVertices(){
        Vector2[] vertices = new Vector2[this.points.size()];
        int index = 0;
        for (Point point : this.points){
            vertices[index] = point.getOrigin();
            index++;
        }
        return vertices;
    }

    public ArrayList<Point> getPoints(){
        return this.points;
    }

    public Vector2 getOrigin(){
        float totalX = 0;
        float totalY = 0;

        for (Point point : this.points){
            totalX += point.getOrigin().x;
            totalY += point.getOrigin().y;
        }
        return new Vector2(totalX / this.points.size(), totalY / this.points.size());
    }

    public void addGravity(Vector2 gravity, float timeStep){
        for (Point point: this.points){
            point.addGravity(gravity, timeStep);
        }
    }

    public void integrate(float timeStep){
        for (Point point: this.points){
            point.integrate(timeStep);
        }
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Point point : this.points){
            shapeRenderer.circle(point.getOrigin().x, point.getOrigin().y, 5);
        }

        for (int index = 0; index < this.points.size() - 1; index++){
            shapeRenderer.line(this.points.get(index).getOrigin(), this.points.get(index + 1).getOrigin());
        }
        shapeRenderer.line(this.points.get(0).getOrigin(), this.points.get(this.points.size() - 1).getOrigin());
        shapeRenderer.end();
    }
}
