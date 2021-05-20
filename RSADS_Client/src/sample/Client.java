package sample;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class Client {
    private String host = "localhost";
    private int port = 10430;
    private Socket client;
    private DataOutputStream clientOut;
    private DataInputStream clientIn;
    private RSADS rsads = new RSADS();
    private KeyPair pair;

    public void connectToServer() throws Exception {
        System.out.println("Connecting to server...");
        client = new Socket(host, port);
        clientOut = new DataOutputStream(client.getOutputStream());
        clientIn = new DataInputStream(client.getInputStream());

        // Sends public key to the server
        RSADS.generateKey();
        pair = rsads.getPair();
        byte[] pairByte = pair.getPublic().getEncoded();
        clientOut.writeInt(pairByte.length);
        clientOut.write(pairByte);
        clientOut.flush();
        System.out.println("Connection established.");
    }

    public void sendMessage(String message) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //byte[] messageByte = "Hewwo?".getBytes();

        byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
        // Hashes message bytes
        byte[] hashedMessageByte = rsads.hashString(message);
        // Sends out unhashed message
        clientOut.writeInt(messageByte.length);
        clientOut.write(messageByte);
        clientOut.flush();

        // Signs the hashed message
        byte[] messageSignature = rsads.signMessage(hashedMessageByte);
        // Sends hashed message signature
        clientOut.writeInt(messageSignature.length);
        clientOut.write(messageSignature);
        clientOut.flush();

        // Log
        System.out.println("Message sent to server.");
    }

    public String receiveMessage() throws IOException {
        byte[] messageIn = new byte[0];
        
        int length = clientIn.readInt();
        if (length > 0) {
            messageIn = new byte[length];
            clientIn.readFully(messageIn, 0, messageIn.length);
        }
        return new String(messageIn);
    }

    private void closeClient() throws IOException {
        clientOut.close();
        System.out.println("Connection closed");
    }
}