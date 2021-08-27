/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.animation;

import glm_.mat4x4.Mat4;

/**
 *
 * @author Jon
 */
public class BoneInfo {
    private int id;
    private String name;
    private Mat4 matrix;
    public BoneInfo(int id, String name, Mat4 matrix) {
        this.id = id;
        this.name = name;
        this.matrix = matrix;
    }
    
    public int getBoneId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Mat4 getMatrix() {
        return matrix;
    }
}
