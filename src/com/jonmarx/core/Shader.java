package com.jonmarx.core;

import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;
import static org.lwjgl.opengl.GL33C.*;

/**
 *
 * @author Jon
 */
 
 public class Shader {
    protected int program;
    
    public Shader(String vertexShader, String fragmentShader) {
        loadShaders(vertexShader, fragmentShader);
    }
	 
    private void loadShaders(String vertex, String fragment) {
        // shaders
        program = glCreateProgram();
        
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        String vData = readFile(vertex);
        glShaderSource(vertexShader, vData);
        glCompileShader(vertexShader);
        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(vertexShader, 1024));
            System.out.println("vertex compile failed");
            System.out.println("shader name: " + vertex);
            System.exit(1);
        }
        glAttachShader(program, vertexShader);
        
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        String fData = readFile(fragment);
        glShaderSource(fragmentShader, fData);
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(fragmentShader, 1024));
            System.out.println("frag compile failed");
            System.out.println("shader name: " + fragment);
            System.exit(1);
        }
        glAttachShader(program, fragmentShader);
        
        glLinkProgram(program);
        
        if(glGetProgrami(program, GL_LINK_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(program, 1024));
            System.out.println("link failed");
            System.exit(1);
        }
        
        glValidateProgram(program);
        
        if(glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(program, 1024));
            System.out.println("validation failed, this may be a problem, but if it works, it works");
        }
        
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        
        Cleanup.addShader(program);
    }
	
    private String readFile(String address) {
        Scanner sc = new Scanner(this.getClass().getResourceAsStream(address));
        String out = "";
        while(sc.hasNextLine()) {
            out += sc.nextLine() + "\n";
        }
        sc.close();
        return out;
    }
    
    public int getProgram() {
        return program;
    }
	
    public int getUniform(String name) {
        return glGetUniformLocation(program, name);
    }
	
    public void setUniform(String name, Mat4 matrix) {
        glUniformMatrix4fv(glGetUniformLocation(program, name), false, matrix.getArray());
    }
    
    public void setUniform(String name, Vec3 pos) {
        glUniform3fv(glGetUniformLocation(program, name), pos.getArray());
    }
    
    public void setUniform(String name, Vec4 pos) {
        glUniform4fv(glGetUniformLocation(program, name), pos.getArray());
    }
    
    public void setUniform(String name, float value) {
        glUniform1f(glGetUniformLocation(program, name), value);
    }
    
    public void setUniform(String name, int value) {
        glUniform1i(glGetUniformLocation(program, name), value);
    }
    
    public void setUniform(String name, Mat4[] values) {
        for(int i = 0; i < values.length; i++) {
            setUniform(name + "[" + i + "]", values[i]);
        }
    }
 }