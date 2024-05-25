package com.yz.io;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("127.0.0.1", 8888)) {
            socket.getOutputStream().write("Hello World\n".getBytes());
            socket.getOutputStream().write("This is a test\n".getBytes());
            socket.getOutputStream().write("I Think you are write\n".getBytes());
        }
    }
}
