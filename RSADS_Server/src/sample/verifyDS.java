package sample;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

public interface verifyDS {
    void verify(String receivedMessage, byte[] receivedSignature, PublicKey receivedPublicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;
}
