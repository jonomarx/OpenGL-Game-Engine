package com.jonmarx.game.systems;

import static glm_.Java.glm;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import java.util.HashMap;
import java.util.List;

import com.jonmarx.core.Camera;
import com.jonmarx.core.Main;
import com.jonmarx.core.Renderer;
import com.jonmarx.game.ECSEntity;
import com.jonmarx.game.ECSEvent;
import com.jonmarx.game.ECSEventManager;
import com.jonmarx.game.ECSStorage;
import com.jonmarx.game.ECSSystem;
import com.jonmarx.game.components.CollisionComponent;
import com.jonmarx.game.components.ModelComponent;
import com.jonmarx.game.components.PositionComponent;
import com.jonmarx.game.entities.CollisionGameEntity;
import com.jonmarx.game.entities.GameEntity;

import glm_.vec2.Vec2;
import glm_.vec3.Vec3;

public class UserSystem extends ECSSystem {
	private static String[] messageFilter = new String[] {};
	private static String[] componentFilter = new String[] {};
	
	private ECSEntity amogus;
	private ECSEntity gun;
	
	private Camera camera = new Camera();

	public UserSystem() {
		super("userSystem", messageFilter, componentFilter);
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
		// none
	}
	
	@Override
	protected void updateO() {
		Vec3 rot = (Vec3) amogus.getField("rotation");
		Vec3 pos = (Vec3) amogus.getField("position");
		
		camera.setPos(pos);
        camera.setYaw(rot.getX());
        camera.setPitch(rot.getY());
        
        Main main = Main.getInstance();
        Vec2 delta = main.getMouseMovement();
        float deltaX = delta.getX();
        float deltaY = delta.getY();
        
        float rotate = 0.1f;
        camera.rotateX(deltaX * rotate);
        camera.rotateY(deltaY * rotate);
        
        float distance = 0.3f;
        // convert to an event later
        if(main.getKey(GLFW_KEY_W) == GLFW_PRESS) {
            camera.moveForward(distance);
        }
        if(main.getKey(GLFW_KEY_A) == GLFW_PRESS) {
        	camera.moveRight(-distance);
        }
        if(main.getKey(GLFW_KEY_S) == GLFW_PRESS) {
        	camera.moveForward(-distance);
        }
        if(main.getKey(GLFW_KEY_D) == GLFW_PRESS) {
        	camera.moveRight(distance);
        }
        
        amogus.setField("position", camera.getPos());
        amogus.setField("rotation", new Vec3(camera.getYaw(),0,0));
        
        Vec3 dir = new Vec3();
        dir.setX(glm.cos(glm.radians(camera.getYaw())));
        dir.setY(0);
        dir.setZ(glm.sin(glm.radians(camera.getYaw())));
        
        Renderer.getCamera().setYaw(camera.getYaw());
        Renderer.getCamera().setPitch(camera.getPitch());
        Renderer.getCamera().setPos(camera.getPos().plus(new Vec3(0f,1.05f,0f).plus(dir.times(0.6f))));
		
		gun.setField("rotation", rot);
		
		float yaw = rot.getX();
        float pitch = rot.getY();
        Vec3 direction = new Vec3();
        direction.setX(glm.cos(glm.radians(yaw)) * glm.cos(glm.radians(pitch)));
        direction.setY(glm.sin(glm.radians(pitch)));
        direction.setZ(glm.sin(glm.radians(yaw)) * glm.cos(glm.radians(pitch)));
        direction = direction.normalize();
        
        Vec3 rightDir = direction.cross(new Vec3(0,1,0)).normalize();
		
        gun.setField("position", pos.plus(0f,0.75f,0f).plus(rightDir.times(0.3f)).plus(direction.times(1.2f)));
	}
	
	@Override
	protected void init() {
		ECSStorage.registerComponent(new PositionComponent());
        ECSStorage.registerComponent(new ModelComponent());
        ECSStorage.registerComponent(new CollisionComponent());
        
        ECSEventManager.registerEvent(new ECSEvent("move", null, null));
        
        HashMap<String, Object> data = new HashMap<>();
        
        data.put("position", new Vec3(0,0,0));
        data.put("scale", new Vec3(1,1,1));
        data.put("rotation", new Vec3(0,0,0));
        data.put("velocity", new Vec3(0,0,0));
        data.put("model", "terrain");
        data.put("shader", "lightShader");
        
        ECSEntity terrain = new GameEntity("terrain");
        ECSStorage.createEntity(terrain, data);
        
        data.put("position", new Vec3(0,0,0));
        data.put("rotation", new Vec3(90f,0.1f,0f));
        data.put("scale", new Vec3(0.8f,0.8f,0.8f));
        data.put("model", "gun");
        gun = new GameEntity("gun");
        ECSStorage.createEntity(gun, data);
        
        data.put("position", new Vec3(0f,8f,0f));
        data.put("rotation", new Vec3(90f,0f,0f));
        data.put("scale", new Vec3(1,1,1));
        data.put("model", "amongus");
        data.put("hitbox", new Vec3(0.61f,1.06f,0.61f));
        amogus = new CollisionGameEntity("amongus");
        ECSStorage.createEntity(amogus, data);
	}
}
