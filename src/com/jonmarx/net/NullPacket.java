package com.jonmarx.net;

import java.nio.ByteBuffer;

/**
 * 1 byte packet, a terminator
 *
 */
public class NullPacket extends Packet {
    public final byte id = 0;
    
    @Override
    public byte[] toBytes() {
        return new byte[] {0};
    }

    /**
     * doesnt do anything
     */
    @Override
    public void fromBytes(ByteBuffer bytes) {
        
    }

}
