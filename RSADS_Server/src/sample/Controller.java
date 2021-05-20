package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TextArea messageField;

    private Server server = new Server();
    private String receivedMessage;
    private byte[] receivedSignature;
    private PublicKey publicKey;

    @FXML
    void onVerifyPressed(ActionEvent event) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        /*System.out.println(receivedMessage);
        System.out.println(new String(receivedSignature));*/
        String message = messageField.getText();
        verifyDS verification = new RSADS();
        verification.verify(message, receivedSignature, publicKey);
    }

    @FXML
    void onClearPressed(ActionEvent event){ messageField.clear(); }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Starts the server
        try {
            server.startServer();
        } catch (Exception e) {
            System.out.println("Could not start the server");
        }

        // Receives messages constantly
        new Thread(()-> {
            int count = 0;
            while (true) {
                try {
                    if(count == 0){
                        byte[] receivedPubKey = server.receiveMessageBytes();
                        publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(receivedPubKey));
                        count++;
                        System.out.println("Public key received");
                    }
                    if(count == 1) {
                        receivedMessage = server.receiveMessage();
                        messageField.setText(receivedMessage);
                        count++;
                    } else {
                        receivedSignature = server.receiveMessageBytes();
                        count = 1;
                    }
                    System.out.println("Message received");

                } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                    System.out.println("Nothing received");
                }
            }
        }).start();

    }

}
