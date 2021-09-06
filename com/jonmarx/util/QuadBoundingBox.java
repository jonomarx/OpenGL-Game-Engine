/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.util;

import static glm_.Java.glm;
import glm_.vec2.Vec2;

/**
 * temporary thing to test 2d rectangles
 * @author Jon
 */
public class QuadBoundingBox {
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
    public QuadBoundingBox(Vec2... points) {
        this.points = points;
    }
    
    public boolean testPoint(Vec2 p) {
        for(int i = 0; i < points.length; i++) {
            if(!testPointAgainstEdge(p,i)) return false;
        }
        return true;
    }
    
    public boolean testBox(QuadBoundingBox b) {
        for(int i = 0; i < points.length; i++) {
            if(!testBoxAgainstEdge(b,0)) return false;
        }
        return true;
    }
    
    public boolean testBoxAgainstEdge(QuadBoundingBox b, int edge) {
        if(edge > points.length-1 || edge < 0) return false;
        Vec2 p1 = points[edge];
        Vec2 p2 = points[(edge+1)%points.length];
        
        float distance = glm.length(p1.minus(p2));
        
        Vec2 planePoint = p1; // probably doesn't matter
        Vec2 planeNormal = getPerpVector(p2.minus(p1).normalize());
        
        // hacked way to do it cuz i can't sort the points without penalty
        // if at least 1 point is inside - then they are colliding.
        System.out.println("loop.");
        for(int i = 0; i < points.length; i++) {
            Vec2 projectedPoint = b.points[i].minus(planeNormal.times(distanceToEdge(planePoint,planeNormal,b.points[i])));
            System.out.println(projectedPoint);
            if(glm.length(projectedPoint.minus(p1)) > distance) continue;
            if(glm.length(projectedPoint.minus(p2)) > distance) continue;
            return true;
        }
        return false;
    }
    
    public boolean testPointAgainstEdge(Vec2 p0, int edge) {
        if(edge > points.length-1 || edge < 0) return false;
        Vec2 p1 = points[edge];
        Vec2 p2 = points[(edge+1)%points.length];
        
        float distance = glm.length(p1.minus(p2));
        
        Vec2 planePoint = p1; // probably doesn't matter
        Vec2 planeNormal = getPerpVector(p2.minus(p1).normalize());
        
        Vec2 projectedPoint = p0.minus(planeNormal.times(distanceToEdge(planePoint,planeNormal,p0)));
        if(glm.length(projectedPoint.minus(p1)) > distance) return false;
        if(glm.length(projectedPoint.minus(p2)) > distance) return false;
        return true;
    }
    
    public Vec2 projectPoint(Vec2 p0, int edge) {
        Vec2 p1 = points[edge];
        Vec2 p2 = points[(edge+1)%points.length];
        Vec2 planePoint = p1; // probably doesn't matter
        Vec2 planeNormal = getPerpVector(p2.minus(p1).normalize());
        
        return p0.minus(planeNormal.times(distanceToEdge(planePoint,planeNormal,p0)));
    }
    
    // if its not projected... undefined results
    public boolean testProjectedPoint(Vec2 projectedPoint, int edge) {
        Vec2 p1 = points[edge];
        Vec2 p2 = points[(edge+1)%points.length];
        float distance = glm.length(p1.minus(p2));
        if(glm.length(projectedPoint.minus(p1)) > distance) return false;
        if(glm.length(projectedPoint.minus(p2)) > distance) return false;
        return true;
    }
    
    // signed distance
    private float distanceToEdge(Vec2 pPoint, Vec2 pNormal, Vec2 point) {
        return pNormal.dot(point.minus(pPoint));
    }
    
    // it doesn't actually matter which direction it faces
    private Vec2 getPerpVector(Vec2 vec) {
        return new Vec2((float) vec.getY(), (float) -vec.getX()).normalize();
    }
}
