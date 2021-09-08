/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.gfx;

import com.jonmarx.core.Shader;

/**
 * This just binds a shader and inputs other uniforms into it
 * @author Jon
 */
public abstract class PostProcessingShader {
    protected Shader shader;
    
    public PostProcessingShader(Shader shader) {
        this.shader = shader;
    }
    
    /**
     * Puts the uniforms in the shader, the binding and rendering are done by the Renderer
     * You do not need to use glUseProgram()
     */
    public abstract void applyUniforms();
    
    /**
     * returns the Shader
     * @return The Shader to be used
     */
    public Shader getShader() {
        return shader;
    }
}
