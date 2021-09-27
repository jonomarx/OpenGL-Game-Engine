/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.discord;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;

/**
 *
 * @author Jon
 */
public class RichTextManager {
    private Unsafe unsafe;
    private static RichTextManager instance;
    
    static {
        System.loadLibrary("discord_game_sdk");
        System.out.println("Discord loaded!");
        System.loadLibrary("libDiscordJavaIntEx");
        System.out.println("Wrapper Loaded!");
    }
    
    public static RichTextManager get() {
        if(instance == null) instance = new RichTextManager();
        return instance;
    }
    
    private RichTextManager() {
        init();
        System.out.println("Inited JNI");
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(RichTextManager.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Could not gain access to the Unsafe.");
        }
    }
    
    public void updateRichText(RichTextObject text) {
        long addressSize = unsafe.addressSize();
        
        if(unsafe == null) return;
        // 5 strings in the thing
        long pointers = unsafe.allocateMemory(addressSize * 5);
        
        long ptrcpy = pointers;
        long state = generateCharArray(text.state);
        unsafe.putAddress(ptrcpy, state);
        
        ptrcpy += addressSize;
        long details = generateCharArray(text.details);
        unsafe.putAddress(ptrcpy, details);
        
        ptrcpy += addressSize;
        long type = generateCharArray(text.type);
        unsafe.putAddress(ptrcpy, type);
        
        ptrcpy += addressSize;
        long name = generateCharArray(text.name);
        unsafe.putAddress(ptrcpy, name);
        
        ptrcpy += addressSize;
        long instance = generateCharArray(text.instance);
        unsafe.putAddress(ptrcpy, instance);
        
        // c++ code will clean up the memory
        pushRichText(pointers);
        // no it wont?
        
        unsafe.freeMemory(state);
        unsafe.freeMemory(details);
        unsafe.freeMemory(type);
        unsafe.freeMemory(name);
        unsafe.freeMemory(instance);
        
        unsafe.freeMemory(pointers);
    }
    
    private long generateCharArray(String string) {
        // no null terminator in java
        long pointer = unsafe.allocateMemory(string.length()+1);
        long pointercpy = pointer;
        for(char c : string.toCharArray()) {
            unsafe.putChar(pointercpy, c);
            pointercpy += 1;
        }
        unsafe.putChar(pointercpy, '\0');
        return pointer;
    }
    
    private native void init();
    public native void pushRichText(long pointer);
    public native void tick();
}
