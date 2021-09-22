package com.jonmarx.net;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.UUID;
import glm_.mat4x4.Mat4;

/**
 * 82 byte packet that sends update info for 1 entity
 * @author Jon
 */
public class UpdatePacket extends Packet {
    public final byte type = 3;
    public UUID id;
    public Mat4 locrot;
    public byte frame;
    
    @Override
    public byte[] toBytes() {
        byte[] out = new byte[82];
        ByteBuffer buffer = ByteBuffer.wrap(out);
        buffer.put(type);
        buffer.putLong(id.getLeastSignificantBits());
        buffer.putLong(id.getMostSignificantBits());
        buffer.put(frame);
        putFloats(buffer);
        return out;
    }
    
    /**
     * Make sure to call ByteBuffer.get() before running this method.
     */
    @Override
    public void fromBytes(ByteBuffer buffer) {
        long lSig = buffer.getLong();
        long mSig = buffer.getLong();
        id = new UUID(lSig, mSig);
        
        frame = buffer.get();
        getFloats(buffer);
    }
    
    private void putFloats(ByteBuffer buffer) {
        float[] data = locrot.getArray();
        for(float num : data) {
            buffer.putFloat(num);
        }
    }
    
    private void getFloats(ByteBuffer buffer) {
        float[] data = new float[16];
        for(int i = 0; i < data.length; i++) {
            data[i] = buffer.getFloat();
        }
        locrot = new Mat4(data);
    }
}
