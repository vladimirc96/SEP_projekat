package com.sep.bankservice.service;

import com.sep.bankservice.crypto.Crypto;
import com.sep.bankservice.crypto.KeyStoreUtil;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Service
public class CryptoService {

    private static String alias = "jceksaes";
    private static String keystorePass = "mystorepass";
    private static String keyPass = "mykeypass";
    private static String keystoreLocation = "C:\\GITHUB\\SEP_PROJEKAT\\keystores\\symmetric\\aes-keystore.jck";
    private Crypto crypto;

    public String encrypt(String message){
        crypto = new Crypto();
        // sifrovati AES algoritmom merchantPassword
        Key key = KeyStoreUtil.getKeyFromKeyStore(keystoreLocation, keystorePass, alias, keyPass);
        SecretKeySpec secretKeySpecification = new SecretKeySpec(key.getEncoded(), "AES");

        return crypto.encrypt(message, secretKeySpecification);
    }

    public String decrypt(String message){
        crypto = new Crypto();
        Key key = KeyStoreUtil.getKeyFromKeyStore(keystoreLocation, keystorePass, alias, keyPass);
        SecretKeySpec secretKeySpecification = new SecretKeySpec(key.getEncoded(), "AES");

        return crypto.decrypt(message, secretKeySpecification);
    }


}
