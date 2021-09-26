/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Jon
 */
public class ServerGameState extends GameState {
    
    protected ServerSocket socket;
    
    public ServerGameState() {
        super();
    }
    
    @Override
    protected void init() {
        super.init();
        try {
            socket = new ServerSocket(1884);
        } catch(IOException e) {
            throw new RuntimeException(e);
            // who tf cares about error catching
        }
        socket.accept();
    }
}
