/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.util;

import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;
import static glm_.Java.glm;
import glm_.vec2.Vec2;

/**
 * Its a collision box. Must import a convex shape like a prism
 * Not in use atm.
 * @author Jon
 */
public abstract class ConvexCollisionBox {
    protected Vec3[] vertices;
    protected Mat4 transform;
    
    protected Vec3[] cachedVertices;
    protected Plane[] cachedEdges;
    protected int[] edgeIndices;
    
    /**
     * Hey, just like OpenGL! kindof...jk, indices help calculate the edges and crap
 
 indices are the lines between vertices...for instance (1,2,1,4) has lines between
 vertices 1+2 and 1+4
     * @param vertices
     * @param planes
     * @param transform
     * @param indices 
     */
    public ConvexCollisionBox(Vec3[] vertices, int planes, Mat4 transform) {
        this.vertices = vertices;
        this.transform = transform;
        this.cachedVertices = new Vec3[vertices.length];
        this.cachedEdges = new Plane[planes];
        
        recacheVertices();
    }
    
    public void updateTransform(Mat4 transform) {
        this.transform = transform;
        recacheVertices();
    }
    
    private void recacheVertices() {
        for(int i = 0; i < vertices.length; i++) {
            Vec3 vertex = vertices[i];
            Vec4 calc = transform.times(new Vec4((float)vertex.getX(), (float)vertex.getY(), (float)vertex.getZ(), 0f));
            cachedVertices[i] = new Vec3(calc.getX(), calc.getY(), calc.getZ());
        }
        recacheEdges();
    }
    
    /**
     * Edge calculation is dependent on the shape.
     * Just the normals i guess
     */
    protected abstract void recacheEdges();
    
    public Mat4 getTransform() {
        return transform;
    }
    
    /**
     * please do not edit.
     * @return 
     */
    public Plane[] getPlanes() {
        return cachedEdges;
    }
    
    /**
     * Moves all points to said plane, only includes 2: the min and the max
     * @param plane
     * @return 
     */
    public Vec3[] moveToPlane3(Plane plane) {
        Vec3[] out = new Vec3[vertices.length];
        for(int i = 0; i < vertices.length; i++) {
            Vec3 point = vertices[i];
            Vec3 movedPoint = point.minus(plane.getNormal().times(plane.distanceTo(point)));
            out[i] = movedPoint;
        }
        return out;
    }
    /**
     * Same as moveToPlane3 but returns the translated position on the plane
     * so its flat
     * @param plane
     * @return 
     */
    public Vec2[] moveToPlane2(Plane plane) {
        Vec2[] out = new Vec2[vertices.length];
        for(int i = 0; i < vertices.length; i++) {
            Vec3 point = vertices[i];
            Vec3 movedPoint = point.minus(plane.getNormal().times(plane.distanceTo(point)));
            
            // note: get the "forward" and "right" vectors from the up vector... somehow
            // this existing code is right from forward
            // this'll probably work ;) i'm interpreting the up vector as forward, so the "right" is the forward and the "up" is the right
            Vec3 right = plane.getNormal().cross(new Vec3(0,1,0)).normalize();
            Vec3 up = right.cross(plane.getNormal()).normalize();
            
            Vec3 a = new Vec3(0,0,0);
            Vec3 b = up.plus(right);
            Vec3 c = b;
            Vec3 ab = b.minus(a).normalize();
            Vec3 ac = c.minus(a).normalize();
            out[i] = new Vec2(ab.dot(movedPoint), ac.dot(movedPoint));
        }
        return out;
    }
    /**
     * Same as moveToPlane2, except its 1d
     * Z IS ASSUMED TO BE 0
     * @param plane
     * @return 
     */
    public float[] moveToPlane1(Plane plane) {
        float[] out = new float[vertices.length];
        // gets the perp. line and converts it to a float
        float slope = -(plane.getNormal().getX() / plane.getNormal().getY());
        float b = plane.getOrigin().getY() - slope * plane.getOrigin().getX();
        if(!Float.isFinite(b)) b = 0;
        for(int i = 0; i < vertices.length; i++) {
            Vec3 point = vertices[i];
            Vec3 movedPoint = point.minus(plane.getNormal().times(plane.distanceTo(point)));
            Vec2 actualPoint = new Vec2(movedPoint.getX(), movedPoint.getY());
            
            // gets the distance from the point to (0, b)
            out[i] = glm.length(actualPoint.minus(new Vec2(0,b)));
        }
        return out;
    }
    
    /**
     * returns only min and max
     * @param plane
     * @return 
     */
    public float[] moveToPlane1E(Plane plane) {
        Float min = null;
        Float max = null;
        float slope = -(plane.getNormal().getX() / plane.getNormal().getY());
        float b = plane.getOrigin().getY() - slope * plane.getOrigin().getX();
        if(!Float.isFinite(b)) b = 0;
        for(int i = 0; i < vertices.length; i++) {
            Vec3 point = vertices[i];
            Vec3 movedPoint = point.minus(plane.getNormal().times(plane.distanceTo(point)));
            Vec2 actualPoint = new Vec2(movedPoint.getX(), movedPoint.getY());
            
            // gets the distance from the point to (0, b)
            float length = glm.length(actualPoint.minus(new Vec2(0,b)));
            
            if(min == null || length < min) {
                min = length;
            }
            if(max == null || length > max) {
                max = length;
            }
        }
        return new float[] {min, max};
    }
}
