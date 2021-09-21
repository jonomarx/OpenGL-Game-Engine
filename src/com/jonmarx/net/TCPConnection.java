/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.net;

import java.net.Socket;
import java.net.SocketException;

/**
 * too lazy to write a UDP implemention
 * @author Jon
 */
public class TCPConnection extends Connection {
    
    private InputStream in;
    private OutputStream out;

    public TCPConnection(String connectTo) throws IOException {
        super(connectTo);
        // more code here
    }
    
    public TCPConnection(Socket socket) throws IOException {
        super(socket.getInetAddress().toString());
        if(!socket.isBound()) throw new SocketException("Socket not bound");
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    @Override
    public byte[] getMessage() {
        return null;
    }

    @Override
    public void sendMessage(byte[] msg) {
        
    }
    
}
