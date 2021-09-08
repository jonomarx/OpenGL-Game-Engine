/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.gfx;

import com.jonmarx.core.Shader;
import static org.lwjgl.opengl.GL20C.glUseProgram;

/**
 * Simple Post Processing Shader that affects Gamma
 * @author Jon
 */
public class GammaPostProcessingShader extends PostProcessingShader {
    protected float gamma;
    
    public GammaPostProcessingShader(Shader shader) {
        super(shader);
    }
    
    public GammaPostProcessingShader(Shader shader, float kernel) {
        this(shader);
        setGamma(kernel);
    }

    @Override
    public void applyUniforms() {
        glUseProgram(shader.getProgram());
        shader.setUniform("gamma", gamma);
        glUseProgram(0);
    }
    
    public void setGamma(float kernel) {
        this.gamma = kernel;
    }
}
