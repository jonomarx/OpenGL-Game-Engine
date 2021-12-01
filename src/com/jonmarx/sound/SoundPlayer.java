package com.jonmarx.sound;

import static org.lwjgl.openal.ALC11.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.alDistanceModel;
import static org.lwjgl.openal.AL11.*;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import glm_.vec3.Vec3;

import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class SoundPlayer {
	private static long device;
	private static long context;
	
	public static void init() {
		device = alcOpenDevice((ByteBuffer)null);
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		context = alcCreateContext(device, (IntBuffer)null);
		alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
		
		// inital stuff
		float[] orientation = {0f, 0f, -1f, 0f, 1f, 0f};
		SoundListener.setPosition(new Vec3(0,0,0));
		SoundListener.setVelocity(new Vec3(0,0,0));
		SoundListener.setOrientation(orientation);
		SoundListener.setGain(0.5f);
		alDistanceModel(AL_INVERSE_DISTANCE);
	}
	
	private static SoundSource source;
	public static void createSource() {
		source = new SoundSource();
		source.setGain(0.5f);
		source.setPitch(1);
		source.setPosition(new Vec3(0,0,0));
		source.setVelocity(new Vec3(0,0,0));
	}
	
	private static int buffer;
	public static void bufferAudio() {
		stackPush();
		IntBuffer channelsBuffer = stackMallocInt(1);
		stackPush();
		IntBuffer sampleRateBuffer = stackMallocInt(1);
		ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename("D:\\backup\\memes (video and audio)\\wait_incomplete.ogg", channelsBuffer, sampleRateBuffer);
		int channels = channelsBuffer.get();
		int sampleRate = sampleRateBuffer.get();
		System.out.println("Channels: " + channels);
		System.out.println("Sample Rate: " + sampleRate);
		
		stackPop();
		stackPop();
		
		// buffering
		int format = -1;
		if(channels == 1) {
			format = AL_FORMAT_MONO16;
		} else if(channels == 2) {
			format = AL_FORMAT_STEREO16;
		}
		buffer = alGenBuffers();
		alBufferData(buffer, format, rawAudioBuffer, sampleRate);
		memFree(rawAudioBuffer);
	}
	
	public static void bindAudio() {
		source.bindBuffer(buffer);
	}
	
	public static void playSound() {
		source.play();
	}
	
	public static SoundSource getSource() {
		return source;
	}
}
