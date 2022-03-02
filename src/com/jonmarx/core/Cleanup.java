/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.ALC11.*;

/**
 * holds a giant list of objects to cleanup
 * @author Jon
 */
public class Cleanup {
    private static List<Integer> textures = new ArrayList<>();
    private static List<Integer> shaders = new ArrayList<>();
    private static List<Integer> framebuffers = new ArrayList<>();
    private static List<Integer> renderbuffers = new ArrayList<>();
    private static List<Integer> arraybuffers = new ArrayList<>();
    private static List<Integer> drawingbuffers = new ArrayList<>();
    private static List<Integer> audiobuffers = new ArrayList<>();
    
    public static void addTexture(int texture) {
        textures.add(texture);
    }
    
    public static void addShader(int shader) {
        shaders.add(shader);
    }
    
    public static void addFBO(int fbo) {
        framebuffers.add(fbo);
    }
    
    public static void addRBO(int rbo) {
        renderbuffers.add(rbo);
    }
    
    public static void addVAO(int vao) {
        arraybuffers.add(vao);
    }
    
    public static void addVBO(int vbo) {
        drawingbuffers.add(vbo);
    }
    
    public static void addIBO(int ibo) {
        drawingbuffers.add(ibo);
    }
    
    public static void addAudio(int audio) {
    	audiobuffers.add(audio);
    }
    
    public static void cleanup() {
        for(int texture : textures) {
            glDeleteTextures(texture);
        }
        System.out.println("cleaned up " + textures.size() + " textures");
        for(int shader : shaders) {
            glDeleteProgram(shader);
        }
        System.out.println("cleaned up " + shaders.size() + " programs");
        for(int framebuffer : framebuffers) {
            glDeleteFramebuffers(framebuffer);
        }
        System.out.println("cleaned up " + framebuffers.size() + " FBOs");
        for(int renderbuffer : renderbuffers) {
            glDeleteRenderbuffers(renderbuffer);
        }
        System.out.println("cleaned up " + renderbuffers.size() + " RBOs");
        for(int arraybuffer : arraybuffers) {
            glDeleteVertexArrays(arraybuffer);
        }
        System.out.println("cleaned up " + arraybuffers.size() + " VAOs");
        for(int drawingbuffer : drawingbuffers) {
            glDeleteBuffers(drawingbuffer);
        }
        System.out.println("cleaned up " + drawingbuffers.size() + " VBOs and IBOs");
        for(int audiobuffer : audiobuffers) {
        	alDeleteBuffers(audiobuffer);
        }
        System.out.println("cleaned up " + audiobuffers.size() + " audio buffers");
        
        long context = alcGetCurrentContext();
        long device = alcGetContextsDevice(context);
        alcMakeContextCurrent(0);
        alcDestroyContext(context);
        alcCloseDevice(device);
        System.out.println("shutdown openal");
        
        System.out.println("cleanup finished");
    }
}
