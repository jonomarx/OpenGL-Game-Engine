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
    	outer:
        for(BoundingBoxPlane plane : other.faces) {
            for(BoundingBoxPlane lPlane : faces) {
            	if(testAgainstOneSide(plane, lPlane)) continue outer;
            }
            System.out.println("PLANE TEST FAILED.");
            return false;
        }
        return true;
    }
    
    private boolean testAgainstOneSide(BoundingBoxPlane side, BoundingBoxPlane side2) {
    	BoundingBox2D pSide = side.project();
    	Vec2[] projectedPoints = new Vec2[side2.getPoints().length];
    	for(int i = 0; i < side2.getPoints().length; i++) {
    		projectedPoints[i] = side.projectPoint(side2.getPoints()[i]);
    	}
    	BoundingBox2D pSide2 = new BoundingBox2D(projectedPoints);
    	
    	System.out.println(Arrays.toString(pSide.getPoints()) + " " + Arrays.toString(pSide2.getPoints()));
    	return pSide.testBox(pSide2) || pSide2.testBox(pSide);
    }
}
