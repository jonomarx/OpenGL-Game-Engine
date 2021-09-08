/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import glm_.mat4x4.Mat4;
import java.util.UUID;

/**
 * Just an entity without <code>update()</code> implementation
 * @author Jon
 */
public class SimpleEntity extends Entity {

    public SimpleEntity(Mat4 locrot, Model model) {
        super(locrot, model, UUID.randomUUID().toString());
    }
    
    public SimpleEntity(Mat4 locrot, Model model, String name) {
        super(locrot, model, name);
    }

    @Override
    public void update() {
        
    }
}
