package com.demo;

import com.badlogic.gdx.math.Vector2;

public class Spring {
    private Point pointOne;
    private Point pointTwo;
    private float length;
    private Vector2 normal;

    public Spring(Point p1, Point p2){
        this.pointOne = p1;
        this.pointTwo = p2;
        this.length = pointOne.getOrigin().dst(pointTwo.getOrigin());
        this.normal = new Vector2();
    }

    public void calculateSpringForce(float strength, float dampening){
        float distance = pointTwo.getOrigin().dst(pointOne.getOrigin());

        if (distance != 0) {
            Vector2 springNormal = pointTwo.getOrigin().sub(pointOne.getOrigin());
            springNormal.nor();

            float springMagnitude = (distance - this.length) * strength;

            Vector2 relativeVelocity = pointOne.getVelocity().sub(pointTwo.getVelocity());
            float velocityAlongSpring = relativeVelocity.dot(springNormal);

            float dampingForceMagnitude = velocityAlongSpring * dampening;

            float totalForceMagnitude = springMagnitude - dampingForceMagnitude;

            Vector2 velocityAdjustment = springNormal.scl(totalForceMagnitude);

            pointOne.updateVelocity(velocityAdjustment.cpy().scl(1));
            pointTwo.updateVelocity(velocityAdjustment.cpy().scl(-1));
        }
        this.normal.set((pointOne.getOrigin().y - pointTwo.getOrigin().y) / distance, -(pointOne.getOrigin().x - pointTwo.getOrigin().x) / distance);
    }

    public float calculateArea() {
        // Area using the cross product
        Vector2 edgeVector = pointTwo.getOrigin().sub(pointOne.getOrigin());
        return Math.abs(edgeVector.crs(this.normal)) / 2.0f;
    }

    public void calculatePressureForce(float area, float pressure){
        float distance = pointTwo.getOrigin().dst(pointOne.getOrigin());
        float pressureV = distance * pressure * (1 / area);
        pressureV *= -1;
        Vector2 finalPressure = this.normal.cpy().scl(pressureV);
        pointOne.updateVelocity(finalPressure);
        pointTwo.updateVelocity(finalPressure);
    }
}
