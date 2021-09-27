/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import com.jonmarx.net.TCPConnectionNIO;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jon
 */
public class ClientGameState extends GameState {
    protected TCPConnectionNIO socket;
    protected String server;
    
    public ClientGameState(String server) {
        super();
        this.server = server;
        
        String[] parts = server.split("/");
        try {
            socket = new TCPConnectionNIO(SocketChannel.open(new InetSocketAddress(parts[0], Integer.parseInt(parts[1]))));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    protected void init() {
        
    }
    
    @Override
    public void update() {
        
    }
}
