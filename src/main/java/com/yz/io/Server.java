package com.yz.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class Server {
    public static void main(String[] args) throws IOException {
        final ServerSocket server = new ServerSocket(8888);

        // 启动服务器
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {

                    final Socket socket = server.accept();
                    new Thread(() -> {
                        try {
                            int len;
                            byte[] bytes = new byte[1024];
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            InputStream is = socket.getInputStream();
                            // 读取流中的数据
                            while ((len = is.read(bytes)) != -1) {
                                buffer.put(bytes, 0, len);
                            }
                            buffer.flip();
                            CharBuffer message = Charset.defaultCharset().decode(buffer);
                            System.out.println("Got Msg: " + message);
                            buffer.clear();
                        } catch (IOException e) {
                            System.out.println("Failed to read bytes from client for: " + e.getMessage());
                        }
                    }).start();
                } catch (IOException e) {
                    System.out.println("Failed to accept connection for : " + e.getMessage());
                }
            }
            try {
                server.close();
            } catch (IOException e) {
                System.out.println("Failed to close server for: " + e.getMessage());
            }
        }, "ServerThread").start();
    }

}
