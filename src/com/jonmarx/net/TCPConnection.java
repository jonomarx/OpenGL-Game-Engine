/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * too lazy to write a UDP implemention
 * @author Jon
 */
public class TCPConnection extends Connection {
    
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    
    /**
     * Split up so its like 127.0.0.0/8083
     * host/port
     * @param connectTo
     * @throws IOException
     */
    public TCPConnection(String connectTo) throws IOException {
        super(connectTo);
        String[] parts = connectTo.split("/");
        if(parts.length != 2) throw new RuntimeException("Should be formatted host/port");
        Socket socket = new Socket(parts[0], Integer.parseInt(parts[1]));
        
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }
    
    public TCPConnection(Socket socket) throws IOException {
        super(socket.getInetAddress().toString());
        if(!socket.isBound()) throw new SocketException("Socket not bound");
        
        this.socket = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }
    
    /**
     * Please use uniform sized messages
     */
    @Override
    public byte[] getMessage() throws IOException {
        if(socket.isClosed()) return new byte[0];
        
        byte[] data = new byte[1024];
        in.read(data);
        return data;
    }
    
    /**
     * Please use uniform sized messages
     */
    @Override
    public void sendMessage(byte[] msg) throws IOException {
        if(socket.isClosed()) return;
        
        out.write(msg);
    }
    
    public boolean isClosed() {
        return socket.isClosed();
    }
    
}
