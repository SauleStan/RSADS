package sample;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class RSADS implements verifyDS{
    private static KeyPair pair;

    public KeyPair getPair() {
        return pair;
    }

    /***
     *  Generates key
     */
    public static void generateKey() throws NoSuchAlgorithmException {
        // Creating KeyPair generator object
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

        // Initializing the key pair generator
        keyPairGen.initialize(2048);

        // Generate the pair of keys
        pair = keyPairGen.generateKeyPair();
    }

    /***
     * Takes hashed message in byte array format and signs it using RSA
     */
    public byte[] signMessage(byte[] hashedMsgBytes) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        // Getting the private key from the key pair
        PrivateKey privKey = pair.getPrivate();

        // Creating a Signature object
        Signature sign = Signature.getInstance("SHA256withRSA");

        // Initialize the signature
        sign.initSign(privKey);

        // Adding data to the signature
        sign.update(hashedMsgBytes);

        // Calculating the signature
        byte[] signedMessageBytes = sign.sign();

        // Returns signed message
        return signedMessageBytes;
    }

    /***
     *  Verifies signature
     */
    public void verify(String receivedMessage, byte[] receivedSignature, PublicKey receivedPublicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        System.out.println("Verifying...");
        byte[] hashedMsgBytes = hashString(receivedMessage);
        Signature verifySig = Signature.getInstance("SHA256withRSA");
        verifySig.initVerify(receivedPublicKey);

        verifySig.update(hashedMsgBytes);
        System.out.println(verifySig.verify(receivedSignature));

        // TODO: Display on the UI that it has been verified.
        // TODO: allow tampering with the message
        // TODO: ^ take message from server text field and use it as received plain message
    }

    /***
     * Takes byte array and returned string of it in hex format
     */
    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    /***
     * Hashes the provided string and returned byte array.
     * Bytes encoded in UTF_8
     * Hash algorithm - SHA-256
     */
    public static byte[] hashString(String msg) throws NoSuchAlgorithmException {
        byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedMsgBytes = digest.digest(msgBytes);

        return hashedMsgBytes;
    }
}
