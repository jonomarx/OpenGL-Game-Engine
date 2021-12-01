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
		alSourcei(source, AL_LOOPING, AL_FALSE); // SET TO FALSE you idiot
		
		alSourcef(source, AL_REFERENCE_DISTANCE, 5);
		alSourcei(source, AL_ROLLOFF_FACTOR, 2);
	}
	
	public void setPosition(Vec3 pos) {
		alSource3f(source, AL_POSITION, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public void setVelocity(Vec3 vel) {
		alSource3f(source, AL_VELOCITY, vel.getX(), vel.getY(), vel.getZ());
	}
	
	public void setDirection(Vec3 dir) {
		alSource3f(source, AL_DIRECTION, dir.getX(), dir.getY(), dir.getZ());
	}
	
	public void setPitch(float pitch) {
		alSourcef(source, AL_PITCH, pitch);
	}
	
	public void setGain(float gain) {
		alSourcef(source, AL_GAIN, gain);
	}
	
	public void bindBuffer(int buffer) {
		alSourcei(source, AL_BUFFER, buffer);
	}
	
	public void play() {
		alSourcePlay(source);
	}
}
