package com.jonmarx.game;

import com.jonmarx.core.TextureModel;
import com.jonmarx.core.Entity;
import com.jonmarx.core.Main;
import com.jonmarx.core.Model;

import glm_.mat4x4.Mat4;
import glm_.vec2.Vec2;
import glm_.vec3.Vec3;

public class Cursor extends Entity {
	protected Vec2 position = new Vec2();

	public Cursor(Mat4 locrot, TextureModel model, String id) {
		super(locrot, model, id);
	}

	@Override
	public void update() {
		Vec2 mouseMovement = Main.getInstance().getMouseMovement();
		position = position.plus(mouseMovement);
		
		if(position.getX() < -1) position.setX(-1);
		if(position.getX() > 1) position.setX(1);
		if(position.getY() < -1) position.setY(-1);
		if(position.getY() > 1) position.setY(1);
		
		locrot = new Mat4().translate(new Vec3(position.getX(), position.getY(), (Float)0f));
	}

}
