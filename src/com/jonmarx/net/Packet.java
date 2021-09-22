package com.jonmarx.net;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class Packet {
    public abstract byte[] toBytes();
    /**
     * Does not read the first byte.
     * @param bytes
     */
    public abstract void fromBytes(ByteBuffer bytes);
    
    public Packet[] parsePackets(byte[] packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet);
        ArrayList<Packet> packets = new ArrayList<>();
        while(true) {
            Packet pac = parsePacket(buffer);
            if(pac instanceof NullPacket) break;
            packets.add(pac);
        }
        return packets.toArray(new Packet[0]);
    }
    
    private Packet parsePacket(ByteBuffer buffer) {
        byte pType = buffer.get();
        Packet packet;
        switch(pType) {
            case 1:
                packet = new CreatePacket();
                packet.fromBytes(buffer);
                break;
            case 2:
                packet = new DeletePacket();
                packet.fromBytes(buffer);
                break;
            default:
                packet = new NullPacket();
                break;    
        }
        return packet;
    }
}
