package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private TextArea messageField;
    private Client client = new Client();

    @FXML
    void onSendMsgPressed(ActionEvent event) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String message = messageField.getText();
        client.sendMessage(message);
    }

    @FXML
    void onClearPressed(ActionEvent event) {
        messageField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            client.connectToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
