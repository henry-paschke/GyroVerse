package com.demo;

import com.badlogic.gdx.math.Vector2;

public class CollisionDetection {
    public static void checkCollision(Shape shapeOne, Shape shapeTwo){
        for (Point point : shapeOne.getPoints()){
            boolean collide = raycastPoint(shapeTwo, point);
            point.getCollisionInfo().collide = collide;
            if (collide) getCollisionInfo(shapeTwo, point);
        }
        for (Point point : shapeTwo.getPoints()){
            boolean collide = raycastPoint(shapeOne, point);
            point.getCollisionInfo().collide = collide;
            if (collide) getCollisionInfo(shapeOne, point);
        }
    }

    private static boolean raycastPoint(Shape shape, Point point) {
        Vector2[] vertices = shape.getVertices();
        Vector2 vertex = point.getOrigin();

        int i, j;
        boolean collide = false;

        for (i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            if (((vertices[i].y >= vertex.y) != (vertices[j].y >= vertex.y)) &&
                (vertex.x <= (vertices[j].x - vertices[i].x) * (vertex.y - vertices[i].y) / (vertices[j].y - vertices[i].y) + vertices[i].x)) {
                collide = !collide;
            }
        }
        return collide;
    }

    private static void getCollisionInfo(Shape shape, Point point){
        float minDistance = Float.MAX_VALUE;
        Vector2 closestPoint = new Vector2();
        int edgeVertexOneIndex = -1;
        int edgeVertexTwoIndex = -1;

        for (int index = 0; index < shape.getPoints().size() - 1; index++){
            Object[] data = pointLineDistance(shape.getPoints().get(index), shape.getPoints().get(index + 1), point);
            float distance = (float) data[0];
            Vector2 relativePoint = (Vector2) data[1];

            if (distance < minDistance){
                minDistance = distance;
                closestPoint = relativePoint;
                edgeVertexOneIndex = index;
                edgeVertexTwoIndex = index + 1;
            }
        }

        Object[] data = pointLineDistance(shape.getPoints().get(0), shape.getPoints().get(shape.getPoints().size() - 1), point);
        float distance = (float) data[0];
        Vector2 relativePoint = (Vector2) data[1];

        if (distance < minDistance){
            minDistance = distance;
            closestPoint = relativePoint;
            edgeVertexOneIndex = 0;
            edgeVertexTwoIndex = shape.getPoints().size() - 1;
        }

        point.getCollisionInfo().depth = minDistance;
        point.getCollisionInfo().edgeVertexOne = shape.getPoints().get(edgeVertexOneIndex);
        point.getCollisionInfo().edgeVertexTwo = shape.getPoints().get(edgeVertexTwoIndex);
        point.getCollisionInfo().closestPoint = closestPoint;
        point.getCollisionInfo().normal = point.getOrigin().sub(closestPoint).nor();
    }

    public static Object[] pointLineDistance(Point edgeVertexOne, Point edgeVertexTwo, Point point){
        Vector2 edgeVector = edgeVertexTwo.getOrigin().sub(edgeVertexOne.getOrigin());
        Vector2 pointVector = point.getOrigin().sub(edgeVertexOne.getOrigin());
        float projection = edgeVector.dot(pointVector);
        float distance = projection / edgeVector.len2();

        Vector2 closestPoint;

        if (distance <= 0){
            closestPoint = edgeVertexOne.getOrigin();
        }
        else if (distance >= 1){
            closestPoint = edgeVertexTwo.getOrigin();
        }
        else{
            closestPoint = edgeVector.scl(distance).add(edgeVertexOne.getOrigin());
        }

        distance = closestPoint.dst(point.getOrigin());

        return new Object[] {distance, closestPoint};
    }
}
