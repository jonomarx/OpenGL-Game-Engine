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
import java.nio.ByteBuffer;

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
    
    @Override
    public Packet getMessage() throws IOException {
        if(socket.isClosed()) return new NullPacket();
        
        byte[] num = new byte[1];
        in.read(num);
        
        byte[] data;
        int i;
        Packet out;
        
        switch(num[0]) {
            default:
            case -1:
                return new NullPacket();
            case 0:
                data = new byte[33];
                i = in.read(data);
                if(i != 33) {
                    throw new RuntimeException("Ooops! something went wrong. Only read " + i + " bytes!");
                }
                out = new CreatePacket();
                out.fromBytes(ByteBuffer.wrap(data));
                return out;
            case 1:
                data = new byte[17];
                i = in.read(data);
                if(i != 17) {
                    throw new RuntimeException("Ooops! something went wrong. Only read " + i + " bytes!");
                }
                out = new DeletePacket();
                out.fromBytes(ByteBuffer.wrap(data));
                return out;
            case 2:
                data = new byte[82];
                i = in.read(data);
                if(i != 82) {
                    throw new RuntimeException("Ooops! something went wrong. Only read " + i + " bytes!");
                }
                out = new UpdatePacket();
                out.fromBytes(ByteBuffer.wrap(data));
                return out;
        }
    }
    
    @Override
    public void sendMessage(Packet msg) throws IOException {
        if(socket.isClosed()) return;
        
        out.write(msg.toBytes());
    }
    
    public boolean isClosed() {
        return socket.isClosed();
    }
}
