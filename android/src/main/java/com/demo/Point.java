package com.demo;

import com.badlogic.gdx.math.Vector2;

public class Point {
    private Vector2 origin;
    private boolean moveable;
    private Vector2 velocity = new Vector2(0,0);
    private CollisionInfo collisionInfo = new CollisionInfo();

    public Point(Vector2 origin, boolean moveable){
        this.origin = origin;
        this.moveable = moveable;
    }

    public boolean isMoveable(){
        return this.moveable;
    }

    public CollisionInfo getCollisionInfo(){
        return this.collisionInfo;
    }

    public Vector2 getOrigin(){
        return this.origin.cpy();
    }

    public void updateOrigin(Vector2 origin){
        this.origin.x += origin.x;
        this.origin.y += origin.y;
    }

    public void setOrigin(Vector2 origin){
        this.origin = origin;
    }

    public void calculateResistance(float resistance){
        this.velocity.scl(resistance);
    }

    public Vector2 getVelocity(){
        if (this.isMoveable()) {
            return this.velocity.cpy();
        }
        return new Vector2(0,0);
    }

    public void updateVelocity(Vector2 velocity){
        this.velocity.x += velocity.x;
        this.velocity.y += velocity.y;
    }

    public void integrate(float timeStep){
        this.origin.x += this.velocity.x * timeStep;
        this.origin.y += this.velocity.y * timeStep;
    }

    public void addGravity(Vector2 gravity, float timeStep){
        this.velocity.x += gravity.x * timeStep;
        this.velocity.y += gravity.y * timeStep;
    }
}
