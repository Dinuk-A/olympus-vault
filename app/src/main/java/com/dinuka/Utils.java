package com.dinuka;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
// import javax.crypto.SecretKey;
// import javax.crypto.SecretKeyFactory;
// import javax.crypto.spec.PBEKeySpec;
// import java.security.NoSuchAlgorithmException;
// import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class Utils {

    public static void clearConsole() {

        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("An error occurred while clearing the console.");
            e.printStackTrace();
        }

    }

    public static String encryptData(String data, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptData(String encryptedData, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    

    public static SecretKey deriveKey(String keyMaterial) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = keyMaterial.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        keyBytes = sha.digest(keyBytes); // Generate a 256-bit hash
        return new SecretKeySpec(keyBytes, "AES");
    }
    
    
}
