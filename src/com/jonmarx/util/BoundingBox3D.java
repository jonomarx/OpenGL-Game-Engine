/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.util;

import glm_.vec2.Vec2;
import glm_.vec3.Vec3;
import java.util.Arrays;

/**
 * 3D SAT implementation
 * @author Jon
 */
public class BoundingBox3D {
    private BoundingBoxPlane[] faces;
    
    public BoundingBox3D(BoundingBoxPlane[] faces) {
        this.faces = faces;
    }
    
    // or just calculate the dot product
    public boolean testPoint(Vec3 point) {
        for(BoundingBoxPlane face : faces) {
            Vec2 projected = face.projectPoint(point);
            BoundingBox2D plane = face.project();
            if(!plane.testPoint(projected)) return false;
        }
        return true;
    }
    
    public boolean testBox(BoundingBox3D other) {
        for(BoundingBoxPlane plane : other.faces) {
            for(BoundingBoxPlane lPlane : faces) {
                BoundingBox2D b1 = lPlane.project();
                
                Vec2[] projectedPoints = new Vec2[plane.getPoints().length];
                for(int i = 0; i < projectedPoints.length; i++) {
                    projectedPoints[i] = lPlane.projectPoint(plane.getPoints()[i]);
                }
                System.out.println(Arrays.toString(projectedPoints) + " " + Arrays.toString(lPlane.project().getPoints()));
                BoundingBox2D b2 = new BoundingBox2D(projectedPoints);
                
                if(!(b1.testBox(b2) || b2.testBox(b1))) {
                    return false;
                }
            }
        }
        return true;
    }
}
