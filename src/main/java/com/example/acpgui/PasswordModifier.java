package com.example.acpgui;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class PasswordModifier {
    private String password;
    private final char[] passwordList;
    private final int shift = 18;
    private String encodedMessage;
    private final PrivateKey privateKey;
    private PublicKey publicKey;
    private String decryptedMessage;



    public PasswordModifier(String password) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        this.password = password;
        passwordList = new char[password.length()];
        for (int i = 0; i < password.length(); i++){
            passwordList[i] = password.charAt(i);
        }
        caesarEncrypt();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
        rsaEncryption();

    }

    public PasswordModifier(String privateKeyString, String encodedMessage) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] privateBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        privateKey = keyFactory.generatePrivate(keySpec);
        this.encodedMessage = encodedMessage;
        rsaDecryption();
        password = decryptedMessage;
        passwordList = new char[password.length()];
        for (int i = 0; i < password.length(); i++){
            passwordList[i] = password.charAt(i);
        }
        caesarDecrypt();
        decryptedMessage = password;
    }

    private void caesarEncrypt(){
        for (int i = 0; i < passwordList.length; i++){
            passwordList[i] = (char)(passwordList[i] + shift);
        }
        StringBuilder sb = new StringBuilder();
        for (char c : passwordList) {
            String theChar = String.valueOf(c);
             sb.append(theChar);
        }
        password = sb.toString();
    }

    private void caesarDecrypt(){
        for (int i = 0; i < passwordList.length; i++){
            passwordList[i] = (char)(passwordList[i] - shift);
        }
        StringBuilder sb = new StringBuilder();
        for (char c : passwordList) {
            String theChar = String.valueOf(c);
            sb.append(theChar);
        }
        password = sb.toString();
    }

    public String privateKeyToString(){
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    private void rsaEncryption() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] secretMessageBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
    }
    public String getEncodedMessage(){
        return encodedMessage;
    }

    private void rsaDecryption() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessageBytes = decryptCipher.doFinal(Base64.getDecoder().decode(encodedMessage));
        decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
    }

    public String decryptedMessage(){
        return decryptedMessage;
    }




}
