/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.util;

import static glm_.Java.glm;
import glm_.vec2.Vec2;

/**
 * 2D SAT implementation
 * @author Jon
 */
public class BoundingBox2D {
    private Vec2[] points;
    
    /**
     * Edge structure:
     * p1->p2
     * p2->p3
     * p3->p4
     * etc...
     * p4->p1
     * @param points nah i'm just lazy, input as many points as you want
     */
    public BoundingBox2D(Vec2... points) {
        this.points = points;
    }
    
    public boolean testPoint(Vec2 p) {
        for(int i = 0; i < points.length; i++) {
            if(!testPointAgainstEdge(p,i)) return false;
        }
        return true;
    }
    
    public boolean testBox(BoundingBox2D b) {
        for(int i = 0; i < points.length; i++) {
            if(!testBoxAgainstEdge(b,i)) return false;
        }
        return true;
    }
    
    // funny result from this function
    // 0.9999999 when facing away lmaoooo
    // rounding error
    public float sweepBox(BoundingBox2D b, Vec2 direction) {
    	Float min = null;
    	for(int i = 0; i < points.length; i++) {
    		float dist = this.sweepBoxAgainstEdge(b, direction, i);
    		if(min == null || dist < min) min = dist;
    		System.out.println("dist: " + dist);
    	}
    	for(int i = 0; i < b.points.length; i++) {
    		float dist = b.sweepBoxAgainstEdge(this, direction.times(-1f), i);
    		if(min == null || dist < min) min = dist;
    		System.out.println("dist: " + dist);
    	}
    	return min;
    }
    
    public boolean testBoxAgainstEdge(BoundingBox2D b, int edge) {
        if(edge > points.length-1 || edge < 0) return false;
        Vec2 p1 = points[edge];
        Vec2 p2 = points[(edge+1)%points.length];
        
        Vec2 planePoint = p1; // probably doesn't matter
        Vec2 planeNormal = getPerpVector(p2.minus(p1).normalize());
        
        // signed distance approach
        Vec2 lineVector = p1.minus(p2);
        float edge2 = glm.length(lineVector);
        
        Float min = null;
        Float max = null;
        
        for(int i = 0; i < points.length; i++) {
            Vec2 projectedPoint = b.points[i].minus(planeNormal.times(distanceToEdge(planePoint,planeNormal,b.points[i])));
            float signedDist = signedDistance(planePoint, lineVector.normalize(), projectedPoint);
            if(min == null || signedDist < min) min = signedDist;
            if(max == null || signedDist > max) max = signedDist;
        }
        if(min >= 0 && min <= edge2) return true;
        if(max >= 0 && max <= edge2) return true;
        return false;
    }
    
    public float sweepBoxAgainstEdge(BoundingBox2D b, Vec2 direction, int edge) {
        if(edge > points.length-1 || edge < 0) return 0;
        Vec2 p1 = points[edge];
        Vec2 p2 = points[(edge+1)%points.length];
        
        Vec2 planePoint = p1; // probably doesn't matter
        Vec2 planeNormal = getPerpVector(p2.minus(p1).normalize());
        
        // signed distance approach
        Vec2 lineVector = p1.minus(p2);
        float edge2 = glm.length(lineVector);
        
        Float min = null;
        Float max = null;
        
        for(int i = 0; i < points.length; i++) {
            float signedDist = projectPoint(planePoint, planeNormal, lineVector, b.points[i]);
            if(min == null || signedDist < min) min = signedDist;
            if(max == null || signedDist > max) max = signedDist;
        }
        if(min >= 0 && min <= edge2) return 0;
        if(max >= 0 && max <= edge2) return 0;
        
        Vec2 pointt = b.points[0].plus(direction);
        Vec2 aPoint = b.points[0].minus(planeNormal.times(distanceToEdge(planePoint, planeNormal, b.points[0])));
        Vec2 aaPoint = pointt.minus(planeNormal.times(distanceToEdge(planePoint, planeNormal, pointt)));
        
        float APoint = projectPoint(planePoint, planeNormal, lineVector, aPoint);
        float AAPoint = projectPoint(planePoint, planeNormal, lineVector, aaPoint);
        //float pDirection = projectPoint(planePoint, planeNormal, lineVector, aPoint.minus(aaPoint));
        float pDirection = APoint - AAPoint;
        

        if(glm.abs(pDirection) < 0.01f) {
        	return 1; // movement is almost 0, it will probably never touch
        }
        
        // not colliding, actually have to sweep
        
        float distance;
        int dir1d;
        if(max < 0) { // box 1 is aligned first on the line
            distance = 0 - max;
            dir1d = max < 0 ? 1 : -1;
        } else { // box 2 is aligned first on the line
            distance = min - edge2;
            dir1d = edge2 < min ? 1 : -1;
        }
        if(distance < 0.001f) return 0; // close enough to touching
        //if(distance > 0 && dir1d == 1) return 1; // will NEVER touch
        //if(distance < 0 && dir1d == -1) return 1; // will NEVER touch
        // probably lol
        
        float time = distance / pDirection;
        System.out.println(time);
        if(time > 1f) {
        	time = 1; // round it.
        	System.out.println("1!");
        }
        if(time < 0f) {
        	time = 1; // they will never collide
        	System.out.println("<0 means 1!");
        }
        return time;
    }
    
    private float projectPoint(Vec2 planeOrigin, Vec2 planeNormal, Vec2 lineVector, Vec2 point) {
    	Vec2 projectedPoint = point.minus(planeNormal.times(distanceToEdge(planeOrigin, planeNormal, point)));
        float pPoint = signedDistance(planeOrigin, lineVector.normalize(), projectedPoint);
        return pPoint;
    }
    
    public boolean testPointAgainstEdge(Vec2 p0, int edge) {
        if(edge > points.length-1 || edge < 0) return false;
        Vec2 p1 = points[edge];
        Vec2 p2 = points[(edge+1)%points.length];
        
        Vec2 planePoint = p1; // probably doesn't matter
        Vec2 planeNormal = getPerpVector(p2.minus(p1).normalize());
        
        Vec2 projectedPoint = p0.minus(planeNormal.times(distanceToEdge(planePoint,planeNormal,p0)));
        
        // signed distance approach
        Vec2 lineVector = p1.minus(p2);
        float point = signedDistance(planePoint, lineVector.normalize(), projectedPoint);
        float edge2 = glm.length(lineVector);
        
        if(point < 0 && point < edge2) return false;
        if(point > 0 && point > edge2) return false;
        return true;
    }
    
    // keep the point the same for all tests.
    private float signedDistance(Vec2 lineOrigin, Vec2 lineVector, Vec2 p2) {
        Vec2 originToPoint = lineOrigin.minus(p2);
        if(lineVector.dot(originToPoint.normalize()) < 0) {
            return -glm.length(originToPoint);
        } else {
            return glm.length(originToPoint);
        }
    }
    
    // signed distance
    private float distanceToEdge(Vec2 pPoint, Vec2 pNormal, Vec2 point) {
        return pNormal.dot(point.minus(pPoint));
    }
    
    // it doesn't actually matter which direction it faces
    private Vec2 getPerpVector(Vec2 vec) {
        return new Vec2((float) vec.getY(), (float) -vec.getX()).normalize();
    }
    
    public Vec2[] getPoints() {
        return points;
    }
}
