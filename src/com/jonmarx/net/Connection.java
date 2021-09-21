/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.net;

/**
 *
 * @author Jon
 */
public abstract class Connection {
    
    protected String connectTo;
    
    public Connection(String connectTo) {
        this.connectTo = connectTo;
    }
    
    public abstract byte[] getMessage();
    public abstract void sendMessage(byte[] msg);
}
