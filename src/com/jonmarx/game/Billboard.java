package com.jonmarx.game;

import com.jonmarx.core.Entity;
import com.jonmarx.core.Model;

import glm_.mat4x4.Mat4;

public class Billboard extends Entity {
	
	public Billboard(Mat4 matrix, Model model, String name) {
		super(matrix, model, name);
	}

	@Override
	public void update() {
		
	}
}
