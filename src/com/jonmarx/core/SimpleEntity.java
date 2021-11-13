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

    public SimpleEntity(Mat4 locrot, Model model, String shader) {
        super(locrot, model, UUID.randomUUID().toString(), shader);
    }
    
    public SimpleEntity(Mat4 locrot, Model model, String name, String shader) {
        super(locrot, model, name, shader);
    }

    @Override
    public void update() {
        
    }
}
