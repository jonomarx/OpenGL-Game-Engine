/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import com.jonmarx.net.TCPConnectionNIO;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jon
 */
public class ServerGameState extends GameState {
    
    protected ServerSocketChannel socket;
    protected List<TCPConnectionNIO> clients = new ArrayList<>();
    
    public ServerGameState() {
        super();
    }
    
    @Override
    protected void init() {
        super.init();
        try {
            socket = ServerSocketChannel.open();
            socket.bind(new InetSocketAddress(1884));
        } catch(IOException e) {
            throw new RuntimeException(e);
            // who tf cares about error catching
        }
    }
    
    @Override
    public void update() {
        super.update();
        
        // detect if new clients
        try {
            SocketChannel channel;
            while((channel = socket.accept()) != null) {
                clients.add(createClient(channel));
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
            // who tf cares about error catching
        }
    }
    
    private TCPConnectionNIO createClient(SocketChannel conn) throws IOException {
        // init stuff related to client...
        return new TCPConnectionNIO(conn);
    }
}
