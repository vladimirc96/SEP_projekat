package com.sep.bankservice.crypto;

import com.google.common.base.Throwables;
import com.google.common.io.BaseEncoding;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class Crypto {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private Cipher cipher;
    private IvParameterSpec ivspec;


    public Crypto(){
        // init za Cipher objekat
        try {
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }

    public String encrypt(String message, SecretKeySpec secretKeySpec){
        // generisanje Initialization Vector objekta - svaki put drugacije
        SecureRandom srandom  = new SecureRandom();
        byte[] iv = new byte[128/8];
        srandom.nextBytes(iv);
        ivspec = new IvParameterSpec(iv);

        String base64EncodedEncryptedMsg = null;
        String base64EncodedEncryptedIV = null;
        try{

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);
            byte[] encryptedMessageInBytes = cipher.doFinal(message.getBytes("UTF-8"));
            base64EncodedEncryptedMsg = BaseEncoding.base64().encode(encryptedMessageInBytes);
            base64EncodedEncryptedIV = BaseEncoding.base64().encode(ivspec.getIV());

            System.out.println("******************** MERCHANT PASSWORD: " + message);
            System.out.println("******************** MERCHANT PASSWORD CRYPT: " + base64EncodedEncryptedIV + ":" + base64EncodedEncryptedMsg);

        } catch(InvalidKeyException | InvalidAlgorithmParameterException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e){

            throw Throwables.propagate(e);

        }
        // za potrebe decrypt-a potrebno je dodati IV na sifrovanu poruku
        return base64EncodedEncryptedIV + ":" + base64EncodedEncryptedMsg;
    }

    public String decrypt(String message, SecretKeySpec secretKeySpec) {
        String[] temp = message.split(":");
        String msg = temp[1];
        String iv = temp[0];

        // vrati u byte kako bi instancirao IV za dekodiranje
        byte[] ivTemp = BaseEncoding.base64().decode(iv);
        ivspec = new IvParameterSpec(ivTemp);

        String originalMessage = null;
        try {

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);
            byte[] encryptedTextBytes = BaseEncoding.base64().decode(msg);
            byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
            originalMessage = new String(decryptedTextBytes);

            System.out.println("******************** MERCHANT PASSWORD: " + originalMessage);
            System.out.println("******************** MERCHANT PASSWORD CRYPT: " + msg);

        }catch(InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException  e){
            throw Throwables.propagate(e);
        }

        return originalMessage;
    }



}
