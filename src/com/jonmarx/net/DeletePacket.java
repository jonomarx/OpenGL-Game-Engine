package com.jonmarx.net;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * 17 byte packet
 * @author Jon
 *
 */
public class DeletePacket extends Packet {
    public final byte id = 2;
    public UUID entityId;
    
    @Override
    public byte[] toBytes() {
        byte[] bytes = new byte[17];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put(id);
        buffer.putLong(entityId.getLeastSignificantBits());
        buffer.putLong(entityId.getMostSignificantBits());
        return null;
    }

    /**
     * Make sure to run ByteBuffer.get() before running this method.
     */
    @Override
    public void fromBytes(ByteBuffer buffer) {
        long lSig = buffer.getLong();
        long mSig = buffer.getLong();
        entityId = new UUID(lSig, mSig);
    }
}
