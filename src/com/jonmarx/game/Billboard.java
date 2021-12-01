package com.jonmarx.game;

import com.jonmarx.core.Entity;
import com.jonmarx.core.Model;

import glm_.mat4x4.Mat4;

public class Billboard extends Entity {
	
	public Billboard(Mat4 matrix, Model model, String name, String shader) {
		super(matrix, model, name, shader);
	}

	@Override
	public void update() {
		
	}
}
