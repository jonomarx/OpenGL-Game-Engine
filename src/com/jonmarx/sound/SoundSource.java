package com.jonmarx.sound;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.openal.AL10.AL_LOOPING;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.openal.AL11.*;

import glm_.vec3.Vec3;

public class SoundSource {
	private int source;
	
	public SoundSource() {
		source = alGenSources();
		alSourcei(source, AL_SOURCE_RELATIVE, AL_FALSE);
		alSourcei(source, AL_LOOPING, AL_TRUE); // SET TO FALSE you idiot
		
		alSourcef(source, AL_REFERENCE_DISTANCE, 5);
		alSourcei(source, AL_ROLLOFF_FACTOR, 2);
	}
	
	public void setFloat(int field, float val) {
		alSourcef(source, field, val);
	}
	
	public void setInt(int field, int val) {
		alSourcei(source, field, val);
	}
	
	public void setVec3(int field, Vec3 val) {
		alSource3f(source, field, val.getX(), val.getY(), val.getZ());
	}
	
	public void bindBuffer(int buffer) {
		alSourcei(source, AL_BUFFER, buffer);
	}
	
	public void play() {
		alSourcePlay(source);
	}
}
