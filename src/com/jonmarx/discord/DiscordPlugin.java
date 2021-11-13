/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.discord;

import com.jonmarx.plugin.Plugin;
import com.jonmarx.game.Crewmate;
import com.jonmarx.core.Renderer;

/**
 *
 * @author Jon
 */
public class DiscordPlugin implements Plugin {
    @Override
    public void onInit() {
        System.out.println("Discord plugin");
        RichTextManager text = RichTextManager.get();
        RichTextObject obj = new RichTextObject();
        obj.details = "<None>";
        obj.name = "Game Thing";
        obj.state = "Startup State";
        obj.type = "l";
        obj.instance = "f";
        text.updateRichText(obj);
    }
    
    private int tick = 0;
    @Override
    public void onUpdate() {
        RichTextManager text = RichTextManager.get();
        text.tick();
        tick++;
        
        if(tick % 900 == 0) {
            RichTextObject obj = new RichTextObject();
            //Crewmate entity = (Crewmate) Renderer.getEntity("amongus");
            obj.details = "Position: " + /*entity.getPos()*/ "N/A";
            obj.name = "Game Thing";
            obj.state = "In The Game";
            obj.type = "p";
            obj.instance = "f";
            text.updateRichText(obj);
        }
    }
}
