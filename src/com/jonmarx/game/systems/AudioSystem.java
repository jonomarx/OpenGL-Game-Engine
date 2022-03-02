package com.jonmarx.game.systems;

import static glm_.Java.glm;

import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.system.MemoryUtil;

import com.jonmarx.core.Entity;
import com.jonmarx.core.MemoryCache;
import com.jonmarx.game.ECSEntity;
import com.jonmarx.game.ECSEvent;
import com.jonmarx.game.ECSStorage;
import com.jonmarx.game.ECSSystem;

import static org.lwjgl.openal.AL11.*;

import glm_.vec3.Vec3;

public class AudioSystem extends ECSSystem {

	private static String[] messageFilter = new String[] {"audio"};
	private static String[] componentFilter = new String[] {"+audioComponent"};

	public AudioSystem() {
		super("audioSystem", messageFilter, componentFilter);
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
		for(ECSEvent event : events) {
			
		}
	}

	@Override
	protected void updateO() {
		List<ECSEntity>entities = ECSStorage.getEntities();
		ECSEntity[] e = entities.toArray(new ECSEntity[0]);
		for(ECSEntity entity : e) {
			if(entity.containsComponent("audioComponent")) {
				if(entity.containsComponent("collisionComponent")) {
					entity.setField("AL_POSITION", entity.getField("position"));
					entity.setField("AL_VELOCITY", entity.getField("velocity"));
					
					Vec3 rotation = (Vec3) entity.getField("rotation");
					Vec3 direction = new Vec3();
					direction.setX(glm.cos(glm.radians(rotation.getX())) * glm.cos(glm.radians(rotation.getY())));
		        	direction.setY(glm.sin(glm.radians(rotation.getY())));
		        	direction.setZ(glm.sin(glm.radians(rotation.getX())) * glm.cos(glm.radians(rotation.getY())));
		        	Vec3 dir = direction.normalize();
		        	
					entity.setField("AL_DIRECTION", dir);
				}
				if(entity.containsComponent("loopedAudioComponent")) {
					List<String> buffers = (List<String>) entity.getField("buffers");
					int id = (Integer) entity.getField("audio_id");
					int sourcesLeft = (Integer) entity.getField("AL_BUFFERS_QUEUED");
					int sourcesDone = (Integer) entity.getField("AL_BUFFERS_PROCESSED");
					
					IntBuffer ibuffer = MemoryUtil.memAllocInt(sourcesDone);
					//alSourceUnqueueBuffers(id, ibuffer);
					MemoryUtil.memFree(ibuffer);
					
					// default amount of slack
					if(sourcesLeft < 2) {
						for(int i = 0; i < 2-sourcesLeft; i++) {
							int spot = (Integer) entity.getField("audio_spot");
							IntBuffer data = MemoryUtil.memAllocInt(1);
							data.put(MemoryCache.getAudio(buffers.get(spot)).getId());
							//alSourceQueueBuffers(id, data);
							MemoryUtil.memFree(data);
							
							spot++;
							if(spot > buffers.size()-1) {
								spot = 0;
							}
							entity.setField("audio_spot", spot);
						}
					}
					int state = (Integer) entity.getField("AL_SOURCE_STATE");
					if(state != AL_PLAYING) {
						entity.setField("AL_BUFFER", 1);
						alSourcePlay(id);
					}
				}
				if(entity.containsComponent("unloopedAudioComponent")) {
					// do code
				}
			}
			if(entity.containsComponent("audioListenerComponent")) {
				if(entity.containsComponent("collisionComponent")) {
					entity.setField("AL_POSITION", (Vec3) entity.getField("position"));
					entity.setField("AL_VELOCITY", (Vec3) entity.getField("velocity"));
					
					float[] orientation = new float[6];
					Vec3 rotation = (Vec3) entity.getField("rotation");
					Vec3 direction = new Vec3();
					direction.setX(glm.cos(glm.radians(rotation.getX())) * glm.cos(glm.radians(rotation.getY())));
		        	direction.setY(glm.sin(glm.radians(rotation.getY())));
		        	direction.setZ(glm.sin(glm.radians(rotation.getX())) * glm.cos(glm.radians(rotation.getY())));
		        	Vec3 dir = direction.normalize();
		        	orientation[0] = dir.getX();
		        	orientation[1] = dir.getY();
		        	orientation[2] = dir.getZ();
		        	
		        	Vec3 up = dir.cross(new Vec3(0,1,0)).normalize().cross(dir).normalize();
		        	orientation[3] = up.getX();
		        	orientation[4] = up.getY();
		        	orientation[5] = up.getZ();
		        	
		        	entity.setField("AL_ORIENTATION", orientation);
				}
			}
		}
	}
	
	@Override
	protected void init() {
		
	}

}
