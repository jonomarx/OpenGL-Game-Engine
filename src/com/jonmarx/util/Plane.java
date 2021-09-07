/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.util;

import glm_.vec3.Vec3;

/**
 * does math
 * @author Jon
 */
public class Plane {
    private float equation[] = new float[4];
    private Vec3 normal;
    private Vec3 origin;
    
    public Plane(Vec3 origin, Vec3 normal) {
        this.origin = origin;
        this.normal = normal;
        equation[0] = normal.getX();
        equation[1] = normal.getY();
        equation[2] = normal.getZ();
        equation[3] = -(normal.getX()*origin.getX() + normal.getY()*origin.getY() + normal.getZ()*origin.getZ());
    }
    
    public Plane(Vec3 p1, Vec3 p2, Vec3 p3) {
        this.normal = (p2.minus(p1)).cross(p3.minus(p1)).normalize();
        this.origin = p1;
        
        equation[0] = normal.getX();
        equation[1] = normal.getY();
        equation[2] = normal.getZ();
        equation[3] = -(normal.getX()*origin.getX() + normal.getY()*origin.getY() + normal.getZ()*origin.getZ());
    }
    
    public boolean isFrontFacing(Vec3 direction) {
        float dot = normal.dot(direction);
        return dot <= 0;
    }
    
    public float distanceTo(Vec3 point) {
        return (point.dot(normal)) + equation[3];
    }
    
    public Vec3 getNormal() {
        return normal;
    }
    public Vec3 getOrigin() {
        return origin;
}   }
