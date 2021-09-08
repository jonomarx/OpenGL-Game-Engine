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
    
    public boolean testBoxAgainstEdge(BoundingBox2D b, int edge) {
        if(edge > points.length-1 || edge < 0) return false;
        Vec2 p1 = points[edge];
        Vec2 p2 = points[(edge+1)%points.length];
        
        Vec2 planePoint = p1; // probably doesn't matter
        Vec2 planeNormal = getPerpVector(p2.minus(p1).normalize());
        
        /* hacked way to do it cuz i can't sort the points without penalty
        // if at least 1 point is inside - then they are colliding.
        //edge-length approach
        float distance = glm.length(p1.minus(p2));
        for(int i = 0; i < points.length; i++) {
            Vec2 projectedPoint = b.points[i].minus(planeNormal.times(distanceToEdge(planePoint,planeNormal,b.points[i])));
            if(glm.length(projectedPoint.minus(p1)) > distance) continue;
            if(glm.length(projectedPoint.minus(p2)) > distance) continue;
            return true;
        }
        return false;
        */
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
    
    public boolean testPointAgainstEdge(Vec2 p0, int edge) {
        if(edge > points.length-1 || edge < 0) return false;
        Vec2 p1 = points[edge];
        Vec2 p2 = points[(edge+1)%points.length];
        
        Vec2 planePoint = p1; // probably doesn't matter
        Vec2 planeNormal = getPerpVector(p2.minus(p1).normalize());
        
        Vec2 projectedPoint = p0.minus(planeNormal.times(distanceToEdge(planePoint,planeNormal,p0)));
        /* edge-length approach
        float distance = glm.length(p1.minus(p2));
        if(glm.length(projectedPoint.minus(p1)) > distance) return false;
        if(glm.length(projectedPoint.minus(p2)) > distance) return false;
        return true;
        */
        
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
