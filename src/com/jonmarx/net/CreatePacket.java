package com.jonmarx.net;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * 33 byte packet
 * @author Jon
 *
 */
public class CreatePacket extends Packet {
    public final byte id = 0;
    public UUID entityId;
    public UUID createAs;
    
    @Override
    public byte[] toBytes() {
        byte[] bytes = new byte[33];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put(id);
        buffer.putLong(createAs.getLeastSignificantBits());
        buffer.putLong(createAs.getMostSignificantBits());
        buffer.putLong(entityId.getLeastSignificantBits());
        buffer.putLong(entityId.getMostSignificantBits());
        return bytes;
    }
    
    /**
     * Make sure to call ByteBuffer.get() before running this method.
     */
    @Override
    public void fromBytes(ByteBuffer bytes) {
        long lSig1 = bytes.getLong();
        long mSig1 = bytes.getLong();
        long lSig2 = bytes.getLong();
        long mSig2 = bytes.getLong();
        
        createAs = new UUID(lSig1, mSig1);
        entityId = new UUID(lSig2, mSig2);
    }
}
