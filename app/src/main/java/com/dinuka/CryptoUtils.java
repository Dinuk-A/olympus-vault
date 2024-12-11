// package com.dinuka;

// import javax.crypto.SecretKey;
// import javax.crypto.SecretKeyFactory;
// import javax.crypto.spec.PBEKeySpec;
// import java.security.NoSuchAlgorithmException;
// import java.security.SecureRandom;
// import java.security.spec.InvalidKeySpecException;

// public class CryptoUtils {

//     // Generate a secure random salt
//     public static byte[] generateSalt() {
//         SecureRandom random = new SecureRandom();
//         byte[] salt = new byte[16]; // 16 bytes = 128 bits
//         random.nextBytes(salt);
//         return salt;
//     }

//     // Derive a key using PBKDF2 with HMAC-SHA256
//     public static SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
//         int iterations = 65536; // Number of iterations
//         int keyLength = 256;    // Key length in bits

//         // Create a PBEKeySpec with the password, salt, iterations, and key length
//         PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
//         SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//         byte[] keyBytes = keyFactory.generateSecret(spec).getEncoded();

//         return new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
//     }
// }

