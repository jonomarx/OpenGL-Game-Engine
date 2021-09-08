/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.util;

import glm_.vec2.Vec2;
import glm_.vec3.Vec3;

/**
 * just a BoundingBox2D, except in 3D
 * can be converted to a BoundingBox2D, and
 * can project points onto it.
 * 
 * BoundingBox3D also uses this class to store info.
 * @author Jon
 */
public class BoundingBoxPlane {
    private Vec3[] points;
    private Plane plane;
    
    /**
     * Edge structure:
     * p1->p2
     * p2->p3
     * p3->p4
     * etc...
     * p4->p1
     * ALL POINTS MUST BE ON THE SAME PLANE
     * @param points nah i'm just lazy, input as many points as you want
     */
    public BoundingBoxPlane(Vec3... points) {
        this.points = points;
        this.plane = new Plane(points[0],points[1],points[2]);
    }
    
    /**
     * Converts the coords of this object into flat 2d coords.
     * @return 
     */
    public BoundingBox2D project() {
        Vec2[] ps = new Vec2[points.length];
        for(int i = 0; i < ps.length; i++) {
            ps[i] = projectPoint(points[i]);
        }
        return new BoundingBox2D(ps);
    }
    
    /**
     * Converts the coords of said point into flat 2d coords.
     * @param point
     * @return 
     */
    public Vec2 projectPoint(Vec3 point) {
        Vec3 projected3D = point.minus(plane.getNormal().times(plane.distanceTo(point)));
        
        // faces towrad the x plane
        Plane yAxis = new Plane(points[0], points[0].minus(points[1]).normalize()); // completely arbitrary. still works
        Plane xAxis = new Plane(points[0], yAxis.getNormal().cross(plane.getNormal()).normalize());
        
        float x = yAxis.distanceTo(projected3D);
        float y = xAxis.distanceTo(projected3D);
        return new Vec2(x, y);
    }
    
    public Vec3[] getPoints() {
        return points;
    }
}
