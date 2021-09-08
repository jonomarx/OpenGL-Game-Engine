/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import com.jonmarx.animation.Animation;
import com.jonmarx.animation.Bone;
import com.jonmarx.animation.BoneInfo;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;
import glm_.vec4.Vec4i;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL33C.*;

/**
 * stores and manages info about how to draw an object
 * typically static
 * @author Jon
 */
public class Mesh {
    /**
     * Vertex data of model
     */
    protected Vertex[] verticies;
    /**
     * Index data of model
     */
    protected int[] indicies;
    
    protected int vbo;
    protected int ibo;
    protected Texture[] textures;
    
    protected int[] boneIds;
    protected float[] weights;
    
    public Mesh(Mesh model) {
        this.verticies = new Vertex[model.verticies.length];
        System.arraycopy(model.verticies, 0, verticies, 0, verticies.length);
        this.indicies = new int[model.indicies.length];
        System.arraycopy(model.indicies, 0, indicies, 0, indicies.length);
        
        // java thing
        this.vbo = model.vbo;
        this.ibo = model.ibo;
    }
    
    public Mesh(Vertex[] verticies, int[] indicies, Texture[] textures, int[] boneIds, float[] weights) {
        this.verticies = verticies;
        this.indicies = indicies;
        this.boneIds = boneIds;
        this.weights = weights;
        
        for(int i = 0; i < (int) Math.ceil(weights.length / 4.0); i++) {
            verticies[i].jointIndices = new Vec4i(boneIds[i+0],boneIds[i+1],boneIds[i+2],boneIds[i+3]);
            verticies[i].jointWeights = new Vec4(weights[i+0],weights[i+1],weights[i+2],weights[i+3]);
        }
        
        vbo = glGenBuffers();
        ibo = glGenBuffers();
        
        Cleanup.addVBO(vbo);
        Cleanup.addIBO(ibo);
        
        this.textures = textures;
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        ByteBuffer buffer = BufferUtils.createByteBuffer(verticies.length * 64);
        
        for(Vertex vertex : verticies) {
            buffer.putFloat(vertex.pos.getX());
            buffer.putFloat(vertex.pos.getY());
            buffer.putFloat(vertex.pos.getZ());
            
            buffer.putFloat(vertex.normal.getX());
            buffer.putFloat(vertex.normal.getY());
            buffer.putFloat(vertex.normal.getZ());
            
            buffer.putFloat(vertex.tX);
            buffer.putFloat(vertex.tY);
            
            buffer.putFloat(vertex.jointWeights.getX());
            buffer.putFloat(vertex.jointWeights.getY());
            buffer.putFloat(vertex.jointWeights.getZ());
            buffer.putFloat(vertex.jointWeights.getW());
            
            buffer.putInt(vertex.jointIndices.getX());
            buffer.putInt(vertex.jointIndices.getY());
            buffer.putInt(vertex.jointIndices.getZ());
            buffer.putInt(vertex.jointIndices.getW());
        }
        
        ((Buffer) buffer).flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        IntBuffer ibuffer = BufferUtils.createIntBuffer(indicies.length);
        ibuffer.put(indicies);
        ((Buffer) ibuffer).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    
    public Vertex[] getVertices() {
        return verticies;
    }
    
    public int[] getIndices() {
        return indicies;
    }
}
