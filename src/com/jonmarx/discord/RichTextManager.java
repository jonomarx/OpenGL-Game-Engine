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
    private RichTextManager instance;
    
    public RichTextManager get() {
        if(instance == null) instance = new RichTextManager();
        return instance;
    }
    
    private RichTextManager() {
        init();
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
        // 4 strings in the thing
        long pointers = unsafe.allocateMemory(addressSize * 4);
        
        long ptrcpy = pointers;
        long state = generateCharArray(text.state);
        unsafe.putAddress(ptrcpy, state);
        
        ptrcpy += addressSize;
        long details = generateCharArray(text.details);
        unsafe.putAddress(ptrcpy, details);
        
        ptrcpy += addressSize;
        long type = generateCharArray(text.details);
        unsafe.putAddress(ptrcpy, type);
        
        ptrcpy += addressSize;
        long name = generateCharArray(text.details);
        unsafe.putAddress(ptrcpy, name);
        
        // c++ code will clean up the memory
        pushRichText(pointers);
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

class RichTextObject {
    public String state;
    public String details;
    public String type;
    public String name;
    public String instance;
}
