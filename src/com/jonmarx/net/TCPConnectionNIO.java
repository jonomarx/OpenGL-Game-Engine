/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * too lazy to write a UDP implemention
 * @author Jon
 */
public class TCPConnectionNIO extends Connection {
    
    private SocketChannel socket;
    
    /**
     * Split up so its like 127.0.0.0/8083
     * host/port
     * @param connectTo
     * @throws IOException
     */
    public TCPConnectionNIO(String connectTo) throws IOException {
        super(connectTo);
        String[] parts = connectTo.split("/");
        if(parts.length != 2) throw new RuntimeException("Should be formatted host/port");
        socket = SocketChannel.open(new InetSocketAddress(parts[0], Integer.parseInt(parts[1])));
    }
    
    public TCPConnectionNIO(SocketChannel socket) throws IOException {
        super(socket.getRemoteAddress().toString());
        if(!socket.isOpen()) throw new SocketException("Socket not open");
        
        this.socket = socket;
    }
    
    @Override
    public Packet getMessage() throws IOException {
        if(!socket.isOpen()) return new NullPacket();
        
        ByteBuffer buf = ByteBuffer.allocate(1);
        if(socket.read(buf) != 1) {
            return new NullPacket();
        }
        
        ByteBuffer data;
        int i;
        Packet out;
        
        switch(buf.get(0)) {
            default:
            case -1:
                return new NullPacket();
            case 0:
                data = ByteBuffer.allocate(33);
                socket.configureBlocking(true);
                i = socket.read(data);
                if(i != 32) {
                    throw new RuntimeException("Ooops! something went wrong. Only read " + i + " bytes!");
                }
                out = new CreatePacket();
                data.position(0);
                out.fromBytes(data);
                socket.configureBlocking(false);
                return out;
            case 1:
                data = ByteBuffer.allocate(16);
                socket.configureBlocking(true);
                i = socket.read(data);
                if(i != 15) {
                    throw new RuntimeException("Ooops! something went wrong. Only read " + i + " bytes!");
                }
                out = new DeletePacket();
                data.position(0);
                out.fromBytes(data);
                socket.configureBlocking(false);
                return out;
            case 2:
                data = ByteBuffer.allocate(82);
                socket.configureBlocking(true);
                i = socket.read(data);
                if(i != 81) {
                    throw new RuntimeException("Ooops! something went wrong. Only read " + i + " bytes!");
                }
                out = new UpdatePacket();
                data.position(0);
                out.fromBytes(data);
                socket.configureBlocking(false);
                return out;
        }
    }
    
    @Override
    public void sendMessage(Packet msg) throws IOException {
        if(!socket.isOpen()) return;
        
        socket.configureBlocking(true);
        socket.write(ByteBuffer.wrap(msg.toBytes()));
    }
    
    @Override
    public boolean isClosed() {
        return socket.socket().isOutputShutdown();
    }
}
