package cn.ac.gabriel.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    ServerSocket serverSocket;

    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("ChatServer started on port " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (true) {
                Socket socket = this.serverSocket.accept();
                System.out.println("Accepting connection..." + socket.getLocalAddress());
                new Thread(() -> {
                    try (socket){
                        while (!socket.isClosed()) {
                            InputStream inputStream = socket.getInputStream();
                            byte[] bytes = new byte[1024];

                            while (inputStream.read(bytes) != -1) {
                                System.out.println(new String(bytes));
                                bytes = new byte[1024];
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(8888);
        server.start();
    }
}
