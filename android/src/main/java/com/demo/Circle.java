package com.demo;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Circle extends Shape {
    public Circle(Vector2 origin, float radius, int numPoints, float mass, boolean moveable){
        super(new ArrayList<>(), mass, moveable);
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Point> originalPoints = new ArrayList<>();
        float angle = 0;
        float step = 360f / numPoints;
        for (int index = 0; index < numPoints; index++){        float x = origin.x + (float) (radius * Math.cos(Math.toRadians(angle)));        float y = origin.y + (float) (radius * Math.sin(Math.toRadians(angle)));        points.add(new Point(new Vector2(x, y), moveable));        originalPoints.add(new Point(new Vector2(x, y), moveable));        angle += step;    }    super.points = points;    super.originalPoints = originalPoints;    this.addSprings();}
}
