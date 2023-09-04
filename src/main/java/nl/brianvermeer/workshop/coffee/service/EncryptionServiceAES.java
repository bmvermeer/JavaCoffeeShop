package nl.brianvermeer.workshop.coffee.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionServiceAES {

    private SecretKey secretKey;
    private Cipher cipher;

    public EncryptionServiceAES() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256, new SecureRandom());
            this.secretKey = keyGenerator.generateKey();
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String original) throws GeneralSecurityException{
        if (original == null) {
            original = "empty";
        }

        byte[] iv = new byte[16]; // Initialization Vector
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv); // Generate a random IV

//        var paraSpec = new GCMParameterSpec(128, iv);
        var paraSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paraSpec);
        byte[] encryptedData = cipher.doFinal(original.getBytes());

        var encoder = Base64.getEncoder();
        var encrypt64 = encoder.encode(encryptedData);
        var iv64 = encoder.encode(iv);
        return new String(encrypt64) + "#" + new String(iv64);
    }

    public String decrypt(String cypher) throws GeneralSecurityException{
        if (cypher == null) {
            return cypher;
        }

        var split = cypher.split("#");
        var decoder = Base64.getDecoder();
        var cypherText = decoder.decode(split[0]);
        var iv = decoder.decode(split[1]);

//        var paraSpec = new GCMParameterSpec(128, iv);
        var paraSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paraSpec);
        byte[] decryptedData = cipher.doFinal(cypherText);
        return new String(decryptedData);
    }

}
