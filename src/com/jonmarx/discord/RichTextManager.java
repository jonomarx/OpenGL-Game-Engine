/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.discord;

/**
 *
 * @author Jon
 */
public class RichTextManager {
    public class RichTextWrapper {
        public String state;
        public String details;
        
        public String largeImage;
        public String smallImage;
        
        public boolean isInstance;
    }
    public static native void init();
    public static native void pushRichText(RichTextWrapper wrapper);
    public static native void tick();
}
