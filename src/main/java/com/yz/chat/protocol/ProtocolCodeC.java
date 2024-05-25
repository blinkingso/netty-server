package com.yz.chat.protocol;

import com.yz.chat.command.Command;
import com.yz.chat.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class ProtocolCodeC {

    private ProtocolCodeC() {
    }

    public static final ProtocolCodeC INSTANCE = new ProtocolCodeC();

    public static final int MAGIC_NUMBER = 0xCAFEBABE;

    public ByteBuf encode(ByteBufAllocator alloc, Serializer serializer, Packet packet) {
        // 1. alloc buf
        ByteBuf buffer = alloc.ioBuffer();
        // 2. serialize java object.
        byte[] bytes = serializer.serialize(packet);
        // 3. encode
        buffer.writeInt(MAGIC_NUMBER);
        buffer.writeByte(packet.getVersion());
        buffer.writeByte(serializer.getSerializer());
        buffer.writeByte(packet.getCommand());
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        return buffer;
    }

    public ByteBuf encode(ByteBuf out, Serializer serializer, Packet packet) {
        // 2. serialize java object.
        byte[] bytes = serializer.serialize(packet);
        // 3. encode
        out.writeInt(MAGIC_NUMBER);
        out.writeByte(packet.getVersion());
        out.writeByte(serializer.getSerializer());
        out.writeByte(packet.getCommand());
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        return out;
    }

    public Packet decode(ByteBuf buffer) {
        int magicNumber = buffer.readInt();
        if (magicNumber != MAGIC_NUMBER) {
            throw new IllegalArgumentException("Illegal magic number: " + magicNumber);
        }
        // skip version check.
        buffer.skipBytes(1);
        byte serializer = buffer.readByte();
        byte command = buffer.readByte();
        int length = buffer.readInt();
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);

        Class<? extends Packet> request = Command.getPacketType(command);
        Serializer ser = Serializer.getSerializer(serializer);
        if (request != null && ser != null) {
            return ser.deserialize(bytes, request);
        }
        return null;
    }
}
