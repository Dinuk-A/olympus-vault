package com.dinuka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import java.util.Base64;
import javax.crypto.SecretKey;

public class MainOps {

    public static void createNewRecord() {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Enter Record Name
        System.out.println("Enter a name for this record: ");
        String pwRecName = scanner.nextLine().trim();

        // Load the existing JSON file
        File notPwsFile = new File("D:\\PROJECTS\\Olympus Vault\\app\\src\\main\\resources\\notpws.json");

        JSONObject dataToSendNotPw;
        try {
            if (notPwsFile.exists()) {
                String content = new String(Files.readAllBytes(notPwsFile.toPath()), StandardCharsets.UTF_8);
                dataToSendNotPw = new JSONObject(content);
            } else {
                dataToSendNotPw = new JSONObject();
            }

            String username = UserManager.getLAstCheckedUserName();
            JSONArray userRecs = dataToSendNotPw.optJSONArray(username);

            if (userRecs == null) {
                userRecs = new JSONArray();
                dataToSendNotPw.put(username, userRecs);
            }

            // Check for Duplicate Record Names immediately
            for (int i = 0; i < userRecs.length(); i++) {
                JSONObject record = userRecs.getJSONObject(i);
                if (record.getString("name").equalsIgnoreCase(pwRecName)) {
                    System.out.println("A record with this name already exists. Please choose a different name.");
                    return; // Exit and don't proceed further if the name is taken
                }
            }

            // Retrieve the AES key from SessionManager
            SecretKey aesKey = SessionManager.getAESKey();

            // Step 2: Enter Hint
            System.out.println("Enter a Hint for this password \n(this will be encrypted too): ");
            String pwRecHint = scanner.nextLine().trim();

            // Step 3: Enter Raw Password
            System.out.println("Enter the raw password");
            String rawPW = scanner.nextLine().trim();

            try {
                String encryptedHint = Utils.encryptData(pwRecHint, aesKey);
                String encryptedPw = Utils.encryptData(rawPW, aesKey);

                JSONObject newRecord = new JSONObject();
                newRecord.put("name", pwRecName);
                newRecord.put("hint", encryptedHint);
                newRecord.put("password", encryptedPw);

                userRecs.put(newRecord);
            } catch (Exception e) {
                System.err.println("Error encrypting data: " + e.getMessage());
                return;
            }

            // Step 5: Save the Updated JSON Object
            Files.write(notPwsFile.toPath(), dataToSendNotPw.toString(4).getBytes(StandardCharsets.UTF_8));
            System.out.println("Password record saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving the record: " + e.getMessage());
        }
    }

    // 2
    public static void viewAllRecs() {

        // load the file
        File notPwsFile = new File("D:\\PROJECTS\\Olympus Vault\\app\\src\\main\\resources\\notpws.json");

        try {
            String content = new String(Files.readAllBytes(notPwsFile.toPath()), StandardCharsets.UTF_8);
            JSONObject dataToSendNotPw = new JSONObject(content);

            String username = UserManager.getLAstCheckedUserName();
            JSONArray userRecs = dataToSendNotPw.optJSONArray(username);

            if (userRecs != null && userRecs.length() > 0) {
                System.out.println("\n=== List of Records ===");
                for (int i = 0; i < userRecs.length(); i++) {
                    JSONObject record = userRecs.getJSONObject(i);
                    System.out.println((i + 1) + ". " + record.getString("name"));
                }

                // Ask user which record they want to view
                Scanner scanner = new Scanner(System.in); // Create a new Scanner instance here
                System.out.print("\nWhich record would you like to view (enter the number): ");
                int recordNumber = scanner.nextInt();

                // to open a single record
                openThisRecord(userRecs, recordNumber);

            } else {
                System.out.println("No records found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading records: " + e.getMessage());
        }

    }

    // to open a single record
    private static void openThisRecord(JSONArray userRecs, int recordNumber) {

        JSONObject selectedRec = userRecs.getJSONObject(recordNumber - 1);
        String name = selectedRec.getString("name");
        String encryptedHint = selectedRec.getString("hint");
        String encryptedPassword = selectedRec.getString("password");

        SecretKey aesKey = SessionManager.getAESKey();

        if (aesKey != null) {
            try {
                // Decrypt the password using the AES key
                String decryptedPassword = Utils.decryptData(encryptedPassword, aesKey);

                String decryptedHint = Utils.decryptData(encryptedHint, aesKey);

                // Output the record with decrypted password
                System.out.println("Name: " + name);
                System.out.println("Hint: " + decryptedHint);
                System.out.println("Password: " + decryptedPassword);
            } catch (Exception e) {
                // Handle the exception, e.g., print error message
                System.out.println("Error decrypting password: " + e.getMessage());
            }
        } else {
            System.out.println("No valid AES key found. Please log in again.");
        }

    }

}
