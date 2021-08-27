package com.jonmarx.core;

import glm_.vec3.Vec3;
import glm_.vec4.Vec4;
import glm_.vec4.Vec4i;

/**
 * a vertex for OpenGL to draw
 * contains a vec3 and a vec2, (pos and texture)
 * @author Jon
 */
public class Vertex {
    protected Vec3 pos;
    protected Vec3 normal;
    protected float tX;
    protected float tY;
    protected Vec4 jointWeights;
    protected Vec4i jointIndices;
    
    public Vertex(Vec3 pos, Vec3 normal, float tX, float tY, Vec4 jointWeights, Vec4i jointIndices) {
        this.pos = pos;
        this.normal = normal;
        this.tX = tX;
        this.tY = tY;
        this.jointWeights = jointWeights;
        this.jointIndices = jointIndices;
    }
    
    public Vertex(Vec3 pos, Vec3 normal, float tX, float tY) {
        this(pos, normal, tX, tY, new Vec4(0f), new Vec4i(0));
    }
    
    public Vec3 getPos() {
        return pos;
    }
    
    public Vec3 getNormal() {
        return normal;
    }
    
    public float getTx() {
        return tX;
    }
    
    public float getTy() {
        return tY;
    }
    
    public void setTx(float tX) {
        this.tX = tX;
    }
    
    public void setTy(float tY) {
        this.tY = tY;
    }
    
    public void setPos(Vec3 pos) {
        this.pos = pos;
    }
    
    public void setNormal(Vec3 normal) {
        this.normal = normal;
    }
}
