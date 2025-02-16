package com.demo;

import com.badlogic.gdx.math.Vector2;

public class CollisionInfo {
    public boolean collide = false;
    public float depth = 0;
    public Point edgeVertexOne;
    public Point edgeVertexTwo;
    public Vector2 closestPoint;
    public Vector2 normal;
}
