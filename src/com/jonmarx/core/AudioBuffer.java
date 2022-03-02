package com.jonmarx.core;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class AudioBuffer {
	private int buffer;
	
	public AudioBuffer(String location) {
		stackPush();
		IntBuffer channelsBuffer = stackMallocInt(1);
		stackPush();
		IntBuffer sampleRateBuffer = stackMallocInt(1);
		ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename("src" + location, channelsBuffer, sampleRateBuffer);
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
		if(rawAudioBuffer == null) {
			System.err.println("Yo... the audio buffer is null");
			return;
		}
		Cleanup.addAudio(buffer);
	}
	
	public int getId() {
		return buffer;
	}
}
