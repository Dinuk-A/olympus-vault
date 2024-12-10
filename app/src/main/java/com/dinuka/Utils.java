package com.dinuka;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

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
     public static SecretKeySpec getKeyFromPassword(String password) {
        try {
            // Use a simple method of generating an AES key from the password
            byte[] key = password.getBytes("UTF-8");
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Encrypt data using AES
    public static String encrypt(String data, String password) {
        try {
            SecretKeySpec secretKey = getKeyFromPassword(password);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Decrypt data using AES
    public static String decrypt(String encryptedData, String password) {
        try {
            SecretKeySpec secretKey = getKeyFromPassword(password);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
