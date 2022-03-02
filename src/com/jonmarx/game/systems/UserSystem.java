package com.jonmarx.game.systems;

import static glm_.Java.glm;

import java.util.HashMap;
import java.util.List;

import com.jonmarx.core.Camera;
import com.jonmarx.core.Renderer;
import com.jonmarx.game.ECSEntity;
import com.jonmarx.game.ECSEvent;
import com.jonmarx.game.ECSEventManager;
import com.jonmarx.game.ECSStorage;
import com.jonmarx.game.ECSSystem;
import com.jonmarx.game.components.AudioComponent;
import com.jonmarx.game.components.CollisionComponent;
import com.jonmarx.game.components.LoopedAudioComponent;
import com.jonmarx.game.components.ModelComponent;
import com.jonmarx.game.components.PhysicsComponent;
import com.jonmarx.game.components.PositionComponent;
import com.jonmarx.game.components.AudioListenerComponent;
import com.jonmarx.game.entities.CollisionGameEntity;
import com.jonmarx.game.entities.GameEntity;
import com.jonmarx.sound.SoundListener;

import glm_.vec3.Vec3;

public class UserSystem extends ECSSystem {
	private static String[] messageFilter = new String[] {};
	private static String[] componentFilter = new String[] {};
	
	private ECSEntity amogus;
	private ECSEntity gun;

	public UserSystem() {
		super("userSystem", messageFilter, componentFilter);
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
		// none
	}
	
	@Override
	protected void updateO() {
		Camera camera = (Camera) ECSStorage.getVariable("amogusCamera");
		Vec3 rot = new Vec3(camera.getYaw(), camera.getPitch(), 0);
		camera.setPos((Vec3) amogus.getField("position"));
		Vec3 pos = camera.getPos();
		
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
        /*SoundListener.setPosition(pos);
        SoundListener.setOrientation(new float[] {camera.getFront().getX(), camera.getFront().getY(), camera.getFront().getZ(), camera.getUp().getX(), camera.getUp().getY(), camera.getUp().getZ()});
        SoundListener.setVelocity((Vec3) amogus.getField("velocity"));*/
	}
	
	@Override
	protected void init() {
		ECSStorage.registerComponent(new PositionComponent());
        ECSStorage.registerComponent(new ModelComponent());
        ECSStorage.registerComponent(new CollisionComponent());
        ECSStorage.registerComponent(new AudioComponent());
        ECSStorage.registerComponent(new LoopedAudioComponent());
        ECSStorage.registerComponent(new AudioListenerComponent());
        ECSStorage.registerComponent(new PhysicsComponent());
        
        ECSEventManager.registerEvent(new ECSEvent("move", null, null));
        
        /*HashMap<String, Object> data = new HashMap<>();
        
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
        
        ECSStorage.putVariable("amogus", amogus);*/
        
        ECSEntity[] entities = ECSStorage.createEntities("/res/objects/test.json");
        System.out.println(entities.length);
        ECSEntity terrain = entities[0];
        gun = entities[1];
        amogus = entities[2];
        
        ECSStorage.putVariable("amogus", amogus);
	}
}
