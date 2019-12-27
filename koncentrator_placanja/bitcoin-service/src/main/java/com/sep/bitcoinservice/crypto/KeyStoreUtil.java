package com.sep.bitcoinservice.crypto;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class KeyStoreUtil {

    public static Key getKeyFromKeyStore(final String keystoreLocation, final String keystorePass, final String alias, final String keyPass) {
        try {

            InputStream keystoreStream = new FileInputStream(keystoreLocation);

            KeyStore keystore = KeyStore.getInstance("JCEKS");

            keystore.load(keystoreStream, keystorePass.toCharArray());

            if (!keystore.containsAlias(alias)) {
                throw new RuntimeException("Alias for key not found");
            }

            Key key = keystore.getKey(alias, keyPass.toCharArray());

            return key;

        } catch (UnrecoverableKeyException | KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
