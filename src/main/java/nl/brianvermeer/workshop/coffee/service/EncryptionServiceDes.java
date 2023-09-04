package nl.brianvermeer.workshop.coffee.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionServiceDes {

    private SecretKey secretKey;
    private Cipher cipher;

    public EncryptionServiceDes() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(56, new SecureRandom());
            this.secretKey = keyGenerator.generateKey();
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String original) throws GeneralSecurityException {
        if (original == null) {
            original = "empty";
        }

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Encrypt the original data
        byte[] encryptedData = cipher.doFinal(original.getBytes(StandardCharsets.UTF_8));

        // Encode the encrypted data in base64 for better handling
        return Base64.getEncoder().encodeToString(encryptedData);
    }



    public String decrypt(String cypher) throws GeneralSecurityException{
        if (cypher == null) {
            return cypher;
        }

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Decode the base64-encoded ciphertext
        byte[] encryptedData = Base64.getDecoder().decode(cypher);

        // Decrypt the data
        byte[] decryptedData = cipher.doFinal(encryptedData);

        return new String(decryptedData, StandardCharsets.UTF_8);
    }

}
