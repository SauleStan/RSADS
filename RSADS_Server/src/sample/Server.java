package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    private ServerSocket server;
    private Socket connection;
    private String host = "localhost";
    private int port = 10430;
    private DataOutputStream serverOut;
    private DataInputStream serverIn;

    public void startServer() throws Exception {
        server = new ServerSocket(port, 50, InetAddress.getByName(host));
        System.out.println("Server started.");
        System.out.println("Waiting for connection...");
        connection = server.accept();
        System.out.println("Connection established.");
        serverOut = new DataOutputStream(connection.getOutputStream());
        serverIn = new DataInputStream(connection.getInputStream());
    }

    public void sendMessage(String message) throws IOException {
        //byte[] messageByte = "Hewwo?".getBytes();
        byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
        serverOut.writeInt(messageByte.length);
        serverOut.write(messageByte);
        serverOut.flush();
        System.out.println("Message sent to client.");
    }

    public String receiveMessage() throws IOException {
        byte[] messageIn = new byte[0];

        int length = serverIn.readInt();
        if (length > 0) {
            messageIn = new byte[length];
            serverIn.readFully(messageIn, 0, length);
        }
        return new String(messageIn);
    }

    public byte[] receiveMessageBytes() throws IOException {
        byte[] messageIn = new byte[0];

        int length = serverIn.readInt();
        if (length > 0) {
            messageIn = new byte[length];
            serverIn.readFully(messageIn, 0, length);
        }
        return messageIn;
    }

    public void closeServer() throws IOException {
        serverOut.close();
        System.out.println("Connection closed");
        server.close();
        System.out.println("Server terminated.");
    }
}
