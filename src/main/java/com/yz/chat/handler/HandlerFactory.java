package com.yz.chat.handler;

import com.yz.chat.protocol.Packet;
import com.yz.chat.protocol.packet.LoginRequestPacket;
import com.yz.chat.protocol.packet.LoginResponsePacket;
import com.yz.chat.protocol.packet.MessageRequestPacket;
import com.yz.chat.protocol.packet.MessageResponsePacket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerFactory {
    private static final Map<Class<? extends Packet>, PacketHandler> HANDLER_MAP = new ConcurrentHashMap<>();

    static {
        // request handlers
        HANDLER_MAP.put(LoginRequestPacket.class, new LoginRequestHandler());
        HANDLER_MAP.put(MessageRequestPacket.class, new MessageRequestHandler());

        // response handlers
        HANDLER_MAP.put(LoginResponsePacket.class, new LoginResponseHandler());
        HANDLER_MAP.put(MessageResponsePacket.class, new MessageResponseHandler());
    }

    public static PacketHandler getHandler(Class<? extends Packet> packetClass) {
        PacketHandler handler = HANDLER_MAP.get(packetClass);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for packet class: " + packetClass);
        }
        return handler;
    }
}
