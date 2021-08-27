/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.gfx;

import com.jonmarx.core.Shader;
import static org.lwjgl.opengl.GL20C.glUseProgram;

/**
 * Simple Post-Processing Shader that requires a 3x3 kernel
 * Some effects include blur, sharpen, edge, etc.
 * @author Jon
 */
public class KernelPostProcessingShader extends PostProcessingShader {
    protected float[] kernel = new float[] {0,0,0,0,1,0,0,0,0};

    public KernelPostProcessingShader(Shader shader) {
        super(shader);
    }
    
    public KernelPostProcessingShader(Shader shader, float[] kernel) {
        this(shader);
        setKernel(kernel);
    }

    @Override
    public void applyUniforms() {
        glUseProgram(shader.getProgram());
        for(int i = 0; i < 9; i++) {
            shader.setUniform("kernel["+i+"]", kernel[i]);
        }
        glUseProgram(0);
    }
    
    public void setKernel(float[] kernel) {
        if(kernel.length != 9) {
            return;
        }
        this.kernel = kernel;
    }
}
