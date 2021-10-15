package com.jonmarx.game;

import com.jonmarx.core.Main;
import com.jonmarx.core.State;
import static org.lwjgl.nuklear.Nuklear.*;
import org.lwjgl.nuklear.NkPluginAllocI;
import org.lwjgl.nuklear.NkPluginFreeI;

import org.lwjgl.nuklear.NkAllocator;
import org.lwjgl.nuklear.NkBuffer;
import org.lwjgl.nuklear.NkContext;
import static org.lwjgl.system.MemoryUtil.*;

public class GUIStateTest extends State {
	
	public GUIStateTest() {
		NkContext ctx = NkContext.create();
		NkBuffer cmds = NkBuffer.create();
		NkAllocator allocator = NkAllocator.create().alloc(new NkPluginAllocI() {
			@Override
			public long invoke(long arg0, long arg1, long arg2) {
				return nmemAllocChecked(arg2);
			}
			
		}).mfree(new NkPluginFreeI() {
			@Override
			public void invoke(long arg0, long arg1) {
				nmemFree(arg1);
			}
		});
		nk_init(ctx, allocator, null);
		
		nk_buffer_init(cmds, allocator, 1024);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
