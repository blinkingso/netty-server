package com.yz.chat.serialize;

public interface Serializer {

    /**
     * JSON serializer
     */
    byte SERIALIZER_JSON = 1;

    /**
     * default serializer
     */
    Serializer DEFAULT = new JSONSerializer();

    byte getSerializer();

    byte[] serialize(Object obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);

    static Serializer getSerializer(byte serializer) {
        return switch (serializer) {
            case SERIALIZER_JSON -> Serializer.DEFAULT;
            default -> null;
        };
    }

}
