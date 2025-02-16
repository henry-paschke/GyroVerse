package com.demo;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;


public class Rectangle extends Shape {
    public Rectangle(Vector2 origin, float width, float height, float mass, boolean moveable) {
        super(new ArrayList<>(), mass, moveable);

        ArrayList<Point> points = new ArrayList<>();

        Point topRight = new Point(new Vector2(origin.x + width / 2, origin.y + height / 2), moveable);
        Point topLeft = new Point(new Vector2(origin.x - width / 2, origin.y + height / 2), moveable);
        Point bottomLeft = new Point(new Vector2(origin.x - width / 2, origin.y - height / 2), moveable);
        Point bottomRight = new Point(new Vector2(origin.x + width / 2, origin.y - height / 2), moveable);

        points.add(topRight);
        points.add(topLeft);
        points.add(bottomLeft);
        points.add(bottomRight);

        super.points = points;
        super.addSprings();
    }
}
