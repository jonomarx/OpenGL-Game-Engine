package com.jonmarx.discord;

import java.util.List;

import com.jonmarx.game.ECSEntity;
import com.jonmarx.game.ECSEvent;
import com.jonmarx.game.ECSStorage;
import com.jonmarx.game.ECSSystem;

import glm_.vec3.Vec3;

public class DiscordSystem extends ECSSystem {
	
	private ECSEntity amogus;

	public DiscordSystem() {
		super("discordSystem", new String[] {}, new String[] {});
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
	}
	
	private int tick = 0;
	private Vec3 lastSpot;
	@Override
	protected void updateO() {
		RichTextManager text = RichTextManager.get();
        text.tick();
        tick++;
        
        if(amogus == null) {
			amogus = (ECSEntity) ECSStorage.getVariable("amogus");
			if(amogus == null) return;
		}
        
        if(tick % 900 == 0) {
        	if(lastSpot == null) {
        		lastSpot = (Vec3) amogus.getField("position");
        	}
        	Vec3 pos = (Vec3) amogus.getField("position");
            RichTextObject obj = new RichTextObject();
            obj.details = "Position: " + amogus.getField("position");
            obj.name = "Game Thing";
            obj.state = "Has moved " + pos.minus(lastSpot) + " since then";
            obj.type = "l";
            obj.instance = "t";
            text.updateRichText(obj);
            
            lastSpot = pos;
        }
	}

	@Override
	protected void init() {
		
	}

}
