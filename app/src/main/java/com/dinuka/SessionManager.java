package com.dinuka;

import javax.crypto.SecretKey;

public class SessionManager {

    private static SecretKey aesKey;

    // Set AES key for the session
    public static void setAESKey(SecretKey key) {
        aesKey = key;
    }

    // Get AES key for the session
    public static SecretKey getAESKey() {
        return aesKey;
    }

    // public static boolean isAesKeyAvailable() {
    //     return aesKey != null;
    // }

}
