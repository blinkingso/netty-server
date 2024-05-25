package com.yz;

import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

@DisplayName("ByteBuf tests in Netty")
public class ByteBufTests {

    final ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(2048);

    @BeforeEach
    void setUp() {
        buffer.writeBytes("Hello world".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("test ByteBuf(readerIndex, writerIndex)")
    void testByteBuf() {
        assertAll("ByteBuf index", () -> assertEquals(0, buffer.readerIndex()), () -> assertEquals(11, buffer.writerIndex()));
    }

    @Test
    @DisplayName("ByteBuf.slice,duplicate,copy")
    void testSlice() {
        ByteBuf slice = buffer.slice();
        assertEquals(11, slice.readableBytes());
        assertThrows(IndexOutOfBoundsException.class, () -> slice.writeByte('!'));
        assertEquals(11, buffer.writerIndex());
        // duplicate copied buffer's data without indices (readerIndex or
        // writerIndex)
        ByteBuf duplicate = buffer.duplicate();
        assertEquals(11, duplicate.readableBytes());
        byte h = duplicate.readByte();
        assertEquals('H', h);
        assertEquals(0, buffer.readerIndex());
        duplicate.writeByte('!');
        assertEquals(11, duplicate.readableBytes());
        buffer.writerIndex(12);
        assertEquals(12, buffer.writerIndex());
        buffer.markReaderIndex();
        buffer.readerIndex(11);
        assertEquals('!', buffer.readByte());
        buffer.resetReaderIndex();
        assertEquals(0, buffer.readerIndex());
    }

    @DisplayName("ByteBuf copied tests")
    @Nested
    class Copied {

        @Test
        void testCopied() {
            // Test copy
            ByteBuf copied = buffer.copy();
            assertAll("copy()", () -> assertEquals(copied, buffer), () -> assertEquals(11, copied.readableBytes()), () -> {
                copied.writeByte(' ');
                copied.writeBytes("Netty".getBytes(StandardCharsets.UTF_8));
                assertEquals(17, copied.readableBytes());
                assertEquals(17, copied.writerIndex());
            }, () -> assertEquals(11, buffer.writerIndex()));

        }
    }

    @Test
    void testAll() {
        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(9, 100);
        assertEquals(9, buffer.capacity());
        assertEquals(100, buffer.maxCapacity());
        assertEquals(0, buffer.readerIndex());
        assertEquals(0, buffer.writerIndex());
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        assertEquals(0, buffer.readerIndex());
        assertEquals(4, buffer.writerIndex());
        byte pos = buffer.readByte();
        assertEquals(1, pos);
        assertEquals(1, buffer.readerIndex());
        buffer.writeInt(12);
        assertEquals(8, buffer.writerIndex());
        assertEquals(9, buffer.capacity());
        buffer.writeBytes(new byte[]{8, 9, 10});
        assertEquals(11, buffer.writerIndex());
        // capacity extend
        assertEquals(16, buffer.capacity());
        byte[] dst = new byte[4];
        buffer.getBytes(0, dst);
        assertArrayEquals(new byte[]{1, 2, 3, 4}, dst);
        assertEquals(1, buffer.readerIndex());
    }
}
