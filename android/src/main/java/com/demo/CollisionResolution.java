package com.demo;

import com.badlogic.gdx.math.Vector2;

public class CollisionResolution {
    public static void displaceShapes(Shape shapeOne, Shape shapeTwo){
        for (Point point : shapeOne.getPoints()){
            if (point.getCollisionInfo().collide){
                float v1Dst = point.getCollisionInfo().edgeVertexOne.getOrigin().dst(point.getCollisionInfo().closestPoint);
                float v2Dst = point.getCollisionInfo().edgeVertexTwo.getOrigin().dst(point.getCollisionInfo().closestPoint);

                float v1Pct = v2Dst / (v1Dst + v2Dst);
                float v2Pct = v1Dst / (v1Dst + v2Dst);

                if (point.isMoveable() && point.getCollisionInfo().edgeVertexOne.isMoveable()) {
                    point.getCollisionInfo().edgeVertexOne.updateOrigin(point.getCollisionInfo().normal.cpy().scl((point.getCollisionInfo().depth / 2) * v1Pct));
                    point.getCollisionInfo().edgeVertexTwo.updateOrigin(point.getCollisionInfo().normal.cpy().scl((point.getCollisionInfo().depth / 2) * v2Pct));
                    point.updateOrigin(point.getCollisionInfo().normal.cpy().scl(-point.getCollisionInfo().depth / 2));
                }
                else if (point.isMoveable() && !point.getCollisionInfo().edgeVertexOne.isMoveable()){
                    point.updateOrigin(point.getCollisionInfo().normal.cpy().scl(-point.getCollisionInfo().depth));
                }
                else{
                    point.getCollisionInfo().edgeVertexOne.updateOrigin(point.getCollisionInfo().normal.cpy().scl(point.getCollisionInfo().depth * v1Pct));
                    point.getCollisionInfo().edgeVertexTwo.updateOrigin(point.getCollisionInfo().normal.cpy().scl(point.getCollisionInfo().depth * v2Pct));
                }
            }
        }
        for (Point point : shapeTwo.getPoints()){
            if (point.getCollisionInfo().collide){
                float v1Dst = point.getCollisionInfo().edgeVertexOne.getOrigin().dst(point.getCollisionInfo().closestPoint);
                float v2Dst = point.getCollisionInfo().edgeVertexTwo.getOrigin().dst(point.getCollisionInfo().closestPoint);

                float v1Pct = v2Dst / (v1Dst + v2Dst);
                float v2Pct = v1Dst / (v1Dst + v2Dst);

                if (point.isMoveable() && point.getCollisionInfo().edgeVertexOne.isMoveable()) {
                    point.getCollisionInfo().edgeVertexOne.updateOrigin(point.getCollisionInfo().normal.cpy().scl((point.getCollisionInfo().depth / 2) * v1Pct));
                    point.getCollisionInfo().edgeVertexTwo.updateOrigin(point.getCollisionInfo().normal.cpy().scl((point.getCollisionInfo().depth / 2) * v2Pct));
                    point.updateOrigin(point.getCollisionInfo().normal.cpy().scl(-point.getCollisionInfo().depth / 2));
                }
                else if (point.isMoveable() && !point.getCollisionInfo().edgeVertexOne.isMoveable()){
                    point.updateOrigin(point.getCollisionInfo().normal.cpy().scl(-point.getCollisionInfo().depth));
                }
                else{
                    point.getCollisionInfo().edgeVertexOne.updateOrigin(point.getCollisionInfo().normal.cpy().scl(point.getCollisionInfo().depth * v1Pct));
                    point.getCollisionInfo().edgeVertexTwo.updateOrigin(point.getCollisionInfo().normal.cpy().scl(point.getCollisionInfo().depth * v2Pct));
                }
            }
        }
    }

    public static void applyImpulse(Shape shapeOne, Shape shapeTwo){
        for (Point point : shapeTwo.getPoints()){
            if (point.getCollisionInfo().collide){
                Vector2 relativeVelocity;
                if (shapeOne.isMoveable() && shapeTwo.isMoveable()) {
                    Vector2 edgeVelocity = (point.getCollisionInfo().edgeVertexOne.getVelocity().add(point.getCollisionInfo().edgeVertexTwo.getVelocity())).scl(0.5f);
                    relativeVelocity = point.getVelocity().sub(edgeVelocity);
                }
                else if (shapeOne.isMoveable() && !shapeTwo.isMoveable()){
                    relativeVelocity = (point.getCollisionInfo().edgeVertexOne.getVelocity().add(point.getCollisionInfo().edgeVertexTwo.getVelocity())).scl(0.5f);
                }
                else{
                    relativeVelocity = point.getVelocity();
                }

                if (relativeVelocity.dot(point.getCollisionInfo().normal) <= 0) {
                    return;
                }

                float j = -2 * relativeVelocity.dot(point.getCollisionInfo().normal);
                j /= (shapeTwo.getInverseMass() + shapeOne.getInverseMass());

                Vector2 impulse = point.getCollisionInfo().normal.cpy().scl(j);

                float v1Dst = point.getCollisionInfo().edgeVertexOne.getOrigin().dst(point.getCollisionInfo().closestPoint);
                float v2Dst = point.getCollisionInfo().edgeVertexTwo.getOrigin().dst(point.getCollisionInfo().closestPoint);

                float v1Pct = v2Dst / (v1Dst + v2Dst);
                float v2Pct = v1Dst / (v1Dst + v2Dst);

                point.updateVelocity(impulse.cpy().scl(0.5f));
                point.getCollisionInfo().edgeVertexOne.updateVelocity(impulse.cpy().scl(-0.5f * v1Pct));
                point.getCollisionInfo().edgeVertexTwo.updateVelocity(impulse.cpy().scl(-0.5f * v2Pct));
            }
        }

        for (Point point : shapeOne.getPoints()){
            if (point.getCollisionInfo().collide){
                Vector2 relativeVelocity;
                if (shapeOne.isMoveable() && shapeTwo.isMoveable()) {
                    Vector2 edgeVelocity = (point.getCollisionInfo().edgeVertexOne.getVelocity().add(point.getCollisionInfo().edgeVertexTwo.getVelocity())).scl(0.5f);
                    relativeVelocity = edgeVelocity.sub(point.getVelocity());
                }
                else if (shapeOne.isMoveable() && !shapeTwo.isMoveable()){
                    relativeVelocity = point.getVelocity();
                }
                else{
                    relativeVelocity = (point.getCollisionInfo().edgeVertexOne.getVelocity().add(point.getCollisionInfo().edgeVertexTwo.getVelocity())).scl(0.5f);
                }

                if (relativeVelocity.dot(point.getCollisionInfo().normal) <= 0) {
                    return;
                }

                float j = -2 * relativeVelocity.dot(point.getCollisionInfo().normal);
                j /= (shapeTwo.getInverseMass() + shapeOne.getInverseMass());

                Vector2 impulse = point.getCollisionInfo().normal.cpy().scl(j);

                float v1Dst = point.getCollisionInfo().edgeVertexOne.getOrigin().dst(point.getCollisionInfo().closestPoint);
                float v2Dst = point.getCollisionInfo().edgeVertexTwo.getOrigin().dst(point.getCollisionInfo().closestPoint);

                float v1Pct = v2Dst / (v1Dst + v2Dst);
                float v2Pct = v1Dst / (v1Dst + v2Dst);

                point.updateVelocity(impulse.cpy().scl(0.5f));
                point.getCollisionInfo().edgeVertexOne.updateVelocity(impulse.cpy().scl(-.5f * v1Pct));
                point.getCollisionInfo().edgeVertexTwo.updateVelocity(impulse.cpy().scl(-.5f * v2Pct));
            }
        }
    }
}
