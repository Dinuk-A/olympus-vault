package com.dinuka;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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

    // Generate AES key from master password
    // public static SecretKeySpec getKeyFromPassword(String password) {
    // try {
    // // Use a simple method of generating an AES key from the password
    // byte[] key = password.getBytes("UTF-8");
    // return new SecretKeySpec(key, "AES");
    // } catch (Exception e) {
    // e.printStackTrace();
    // return null;
    // }
    // }

    // Encrypt data using AES
    // public static String encrypt(String data, String password) {
    // try {
    // SecretKeySpec secretKey = getKeyFromPassword(password);
    // Cipher cipher = Cipher.getInstance("AES");
    // cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    // byte[] encrypted = cipher.doFinal(data.getBytes());
    // return Base64.getEncoder().encodeToString(encrypted);
    // } catch (Exception e) {
    // e.printStackTrace();
    // return null;
    // }
    // }

    // Decrypt data using AES
    // public static String decrypt(String encryptedData, String password) {
    // try {
    // SecretKeySpec secretKey = getKeyFromPassword(password);
    // Cipher cipher = Cipher.getInstance("AES");
    // cipher.init(Cipher.DECRYPT_MODE, secretKey);
    // byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
    // return new String(decrypted);
    // } catch (Exception e) {
    // e.printStackTrace();
    // return null;
    // }
    // }

    // Utility method to encrypt data using AES key
    // public static String encryptData(String data, SecretKey aesKey) throws Exception {
    //     Cipher cipher = Cipher.getInstance("AES");
    //     cipher.init(Cipher.ENCRYPT_MODE, aesKey);
    //     byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    //     return Base64.getEncoder().encodeToString(encryptedBytes);
    // }

    public static String encryptData(String data, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Utility method to decrypt data using AES key
    // public static String decryptData(String encryptedData, SecretKey aesKey) throws Exception {
    //     Cipher cipher = Cipher.getInstance("AES");
    //     cipher.init(Cipher.DECRYPT_MODE, aesKey);
    //     byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
    //     return new String(decryptedBytes, StandardCharsets.UTF_8); // Ensure it's converted back to a UTF-8 string
    // }

    public static String decryptData(String encryptedData, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    

    // Generate a secure random salt
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 16 bytes = 128 bits
        random.nextBytes(salt);
        return salt;
    }

    // Derive a key using PBKDF2 with HMAC-SHA256
    // public static SecretKey deriveKey(String password, byte[] salt)
    //         throws NoSuchAlgorithmException, InvalidKeySpecException {
    //     int iterations = 65536; // Number of iterations
    //     int keyLength = 256; // Key length in bits

    //     // Create a PBEKeySpec with the password, salt, iterations, and key length
    //     PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
    //     SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    //     byte[] keyBytes = keyFactory.generateSecret(spec).getEncoded();

    //     return new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
    // }

    // public static SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    //     SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    //     PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
    //     SecretKey tmp = factory.generateSecret(spec);
    //     return new SecretKeySpec(tmp.getEncoded(), "AES");
    // }

    public static SecretKey deriveKey(String keyMaterial) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = keyMaterial.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        keyBytes = sha.digest(keyBytes); // Generate a 256-bit hash
        return new SecretKeySpec(keyBytes, "AES");
    }
    
    
}
