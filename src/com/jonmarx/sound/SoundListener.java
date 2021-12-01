package com.jonmarx.sound;

import static org.lwjgl.openal.AL10.*;

import glm_.vec3.Vec3;

/**
 * Singleton?
 * @author Jon
 *
 */
public class SoundListener {
	public static void setPosition(Vec3 pos) {
		alListener3f(AL_POSITION, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static void setVelocity(Vec3 vel) {
		alListener3f(AL_VELOCITY, vel.getX(), vel.getY(), vel.getZ());
	}
	
	public static void setOrientation(float[] orientation) {
		alListenerfv(AL_ORIENTATION, orientation);
	}
	
	public static void setGain(float gain) {
		alListenerf(AL_GAIN, gain);
	}
}
