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

        JSONObject notDataInPwFile;

        try {
            if (notPwsFile.exists()) {
                String content = new String(Files.readAllBytes(notPwsFile.toPath()), StandardCharsets.UTF_8);
                notDataInPwFile = new JSONObject(content.isEmpty() ? "{}" : content); // Handle empty files
            } else {
                notDataInPwFile = new JSONObject();
            }

            String username = UserManager.getLAstCheckedUserName();
            JSONArray userRecs = notDataInPwFile.optJSONArray(username);

            if (userRecs == null) {
                userRecs = new JSONArray();
                notDataInPwFile.put(username, userRecs);
            }

            // Check for Duplicate Record Names
            for (int i = 0; i < userRecs.length(); i++) {
                JSONObject record = userRecs.getJSONObject(i);
                if (record.getString("name").equalsIgnoreCase(pwRecName)) {
                    System.out.println("A record with this name already exists. Please choose a different name.");
                    return; // Exit and don't proceed further if the name is taken
                }
            }

            // Determine the next ID
            int nextId = 1; // Default starting ID
            for (int i = 0; i < userRecs.length(); i++) {
                JSONObject record = userRecs.getJSONObject(i);
                int currentId = record.optInt("id", 0); // Fetch existing IDs
                if (currentId >= nextId) {
                    nextId = currentId + 1;
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

                // Create the new record JSON Object
                JSONObject newRecord = new JSONObject();
                newRecord.put("id", nextId); // Add the auto-incremented ID
                newRecord.put("name", pwRecName);
                newRecord.put("hint", encryptedHint);
                newRecord.put("password", encryptedPw);

                userRecs.put(newRecord); // Add the new record to the user's records array
            } catch (Exception e) {
                System.err.println("Error encrypting data: " + e.getMessage());
                return;
            }

            // Step 5: Save the Updated JSON Object
            Files.write(notPwsFile.toPath(), notDataInPwFile.toString(4).getBytes(StandardCharsets.UTF_8));
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
            JSONObject notDataInPwFile = new JSONObject(content);

            String username = UserManager.getLAstCheckedUserName();

            JSONArray userRecs = notDataInPwFile.optJSONArray(username);

            if (userRecs != null && userRecs.length() > 0) {
                System.out.println("\n=== List of Records ===");

                for (int i = 0; i < userRecs.length(); i++) {

                    JSONObject record = userRecs.getJSONObject(i);

                    System.out.println((i + 1) + ". " + record.getString("name"));
                }

                Scanner scanner = new Scanner(System.in);
                System.out.print("\nWhich record would you like to view (enter the number): ");

                int selectedRecordNumber = scanner.nextInt();

                if (selectedRecordNumber < userRecs.length()) {
                    // to open a single record
                    openThisRecord(userRecs, selectedRecordNumber);
                } else if (selectedRecordNumber > userRecs.length()) {
                    System.out.println("invalid input");
                }

            } else {
                System.out.println("No records found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading records: " + e.getMessage());
        }

    }

    // to open a single record
    private static void openThisRecord(JSONArray userRecs, int selectedRecordNumber) {

        JSONObject selectedRec = userRecs.getJSONObject(selectedRecordNumber - 1);

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

                InputHandler.showRecOpts(selectedRec);

            } catch (Exception e) {
                // Handle the exception, e.g., print error message
                System.out.println("Error decrypting password: " + e.getMessage());
            }
        } else {
            System.out.println("No valid AES key found. Please log in again.");
        }

    }

    public static void editRecord(JSONObject jObject) {

        // Retrieve the ID of the selected JSON record
        int selectedObjId = jObject.getInt("id");
        System.out.println("Editing record with ID: " + selectedObjId);

        // Get a scanner for user input
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the new name: ");
        String newName = scan.nextLine().trim();

        System.out.println("Enter a new Hint for this password (this will be encrypted): ");
        String newHint = scan.nextLine().trim();

        System.out.println("Enter the new raw password: ");
        String newPassword = scan.nextLine().trim();

        // Retrieve AES key from the session manager (for encryption)
        SecretKey aesKey = SessionManager.getAESKey();

        if (aesKey != null) {
            try {
                // Encrypt the new hint and password
                String encryptedHint = Utils.encryptData(newHint, aesKey);
                String encryptedPassword = Utils.encryptData(newPassword, aesKey);

                // Update the record with new values
                jObject.put("name", newName);
                jObject.put("hint", encryptedHint);
                jObject.put("password", encryptedPassword);

                // Load the existing JSON file
                File notPwsFile = new File("D:\\PROJECTS\\Olympus Vault\\app\\src\\main\\resources\\notpws.json");
                String content = new String(Files.readAllBytes(notPwsFile.toPath()), StandardCharsets.UTF_8);
                JSONObject notDataInPwFile = new JSONObject(content);

                String username = UserManager.getLAstCheckedUserName();
                JSONArray userRecs = notDataInPwFile.optJSONArray(username);

                if (userRecs != null) {
                    // Iterate to find the record with matching ID and replace it
                    for (int i = 0; i < userRecs.length(); i++) {
                        JSONObject record = userRecs.getJSONObject(i);
                        if (record.getInt("id") == selectedObjId) {
                            userRecs.put(i, jObject); // Replace the old record with updated one
                            break;
                        }
                    }

                    // Save the updated JSON back to the file
                    Files.write(notPwsFile.toPath(), notDataInPwFile.toString(4).getBytes(StandardCharsets.UTF_8));
                    System.out.println("Record updated successfully.");
                } else {
                    System.out.println("No records found for this user.");
                }

            } catch (Exception e) {
                System.out.println("Error updating record: " + e.getMessage());
            }
        } else {
            System.out.println("No valid AES key found. Please log in again.");
        }
    }

    public static void deleteRecord(JSONObject jObject) {

        // Retrieve the ID of the selected JSON record
        int selectedObjId = jObject.getInt("id");
        System.out.println("Deleting record with ID: " + selectedObjId);

        // Load the existing JSON file
        File notPwsFile = new File("D:\\PROJECTS\\Olympus Vault\\app\\src\\main\\resources\\notpws.json");

        try {
            // Read the file content into a string
            String content = new String(Files.readAllBytes(notPwsFile.toPath()), StandardCharsets.UTF_8);
            JSONObject notDataInPwFile = new JSONObject(content);

            String username = UserManager.getLAstCheckedUserName();
            JSONArray userRecs = notDataInPwFile.optJSONArray(username);

            if (userRecs != null) {
                // Iterate over the records and find the one with the matching ID
                for (int i = 0; i < userRecs.length(); i++) {
                    JSONObject record = userRecs.getJSONObject(i);
                    if (record.getInt("id") == selectedObjId) {
                        // Remove the record from the JSONArray
                        userRecs.remove(i);
                        break;
                    }
                }

                // Save the updated data back to the file
                Files.write(notPwsFile.toPath(), notDataInPwFile.toString(4).getBytes(StandardCharsets.UTF_8));
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("No records found for this user.");
            }

        } catch (IOException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    // edit a rec
    public static void editRecord2(JSONObject jObject) {

        // get the id of this selected json record
        int selectedObjId = jObject.getInt("id");
        System.out.println(selectedObjId);

        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.println("enter the new name");
        String newName = scan.nextLine().trim();

        System.out.println("Enter a new Hint for this password \n(this will be encrypted too): ");
        String newHint = scan.nextLine().trim();

        // Step 3: Enter Raw Password
        System.out.println("Enter the new raw password");
        String newPassword = scan.nextLine().trim();

        // lets just skip the encryption part for now

        jObject.put("name", newName);
        jObject.put("hint", newHint);
        jObject.put("password", newPassword);

    }

}
