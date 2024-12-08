package com.dinuka;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

import java.io.BufferedReader;

import org.mindrot.jbcrypt.BCrypt;

public class UserManager {

    public static JSONArray getUserList() {
        final String USER_DB_FILE = "D:\\PROJECTS\\Olympus Vault\\app\\src\\main\\resources\\users.json";
        File userFile = new File(USER_DB_FILE);

        if (!userFile.exists()) {
            System.out.println("File not found: " + USER_DB_FILE);
            return new JSONArray();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            StringBuilder content = new StringBuilder();
            String line = reader.readLine(); // Step 1: Read a line from the file
            while (line != null) { // Read file line by line
                content.append(line);
                line = reader.readLine(); // Read the next line
            }
            return new JSONArray(content.toString()); // Parse the file content
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray(); // Return an empty array on I/O error
        } catch (org.json.JSONException e) {
            System.out.println("Error: Invalid JSON format in the users.json file.");
            return new JSONArray(); // Return an empty array on JSON error
        }
    }

    // get the users data by reading the user.json file
    public static JSONArray getUserListOri() {

        // save as a var
        final String USER_DB_FILE = "D:\\PROJECTS\\Olympus Vault\\app\\src\\main\\resources\\users.json";

        // convert into a File
        File userFile = new File(USER_DB_FILE);

        // check the existence of that file
        if (!userFile.exists()) {

            // return an empty json array + do not continue in this method (EARLY RETURN)
            return new JSONArray();
        }

        try {

            // convert it to a readable file
            FileReader userFileReader = new FileReader(userFile);

            // start to reading the file, read() always return the equalant ASCII integer
            int i = userFileReader.read();

            // create an empty string container
            StringBuilder content = new StringBuilder();

            // -1 means end of the stream
            while (i != -1) {

                // modify that empty string container by adding each letter reads, but first >>>
                // the-ASCII int has to be type casted into char
                content.append((char) i);
            }

            // for better protection
            userFileReader.close();

            // return that read block as json array
            return new JSONArray(content.toString());

        } catch (IOException e) {
            e.printStackTrace();

            // return an empty json array
            return new JSONArray();
        }

    }

    // Global var
    // to support pw validation
    private static String lastCheckedUsername;

    // CHECK THE PASSED USERNAME'S EXISTANCE
    public static boolean checkUserNameExistance() {

        System.out.println("Please Enter Your Username to Proceed");
        System.out.println();
        Scanner scanUserName = new Scanner(System.in);
        String passedUserName = scanUserName.nextLine();

        JSONArray userList = getUserList();

        for (int i = 0; i < userList.length(); i++) {
            JSONObject userJsonObj = userList.getJSONObject(i);

            if (userJsonObj.getString("username").equals(passedUserName)) {
                lastCheckedUsername = passedUserName;
                return true;
            }
        }

        return false;
    }

    // to support pw validation
    public static String getLAstCheckedUserName() {
        return lastCheckedUsername;
    }

    public static void createNewProfile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Profile Creation ===");

        // Collect new username
        System.out.print("Enter a new username: ");
        String newUsername = scanner.nextLine();

        // Collect new password
        System.out.print("Enter a password: ");
        String newPassword = scanner.nextLine();

        // Validate username uniqueness
        JSONArray userList = getUserList();
        for (int i = 0; i < userList.length(); i++) {
            JSONObject user = userList.getJSONObject(i);
            if (user.getString("username").equals(newUsername)) {
                System.out.println("Username already exists. Please try again.");
                return; // Exit this method and go back to the username check
            }
        }

        String hashedPw = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Create a new JSON object for the profile
        JSONObject newUser = new JSONObject();
        newUser.put("username", newUsername);
        newUser.put("password", hashedPw);

        // Add to the user list
        userList.put(newUser);

        // Save back to the file
        saveUserList(userList);

        System.out.println("Profile created successfully!");
    }

    public static void saveUserList(JSONArray userList) {
        final String USER_DB_FILE = "D:\\PROJECTS\\Olympus Vault\\app\\src\\main\\resources\\users.json";
        File userFile = new File(USER_DB_FILE);

        try {
            // Ensure the directory exists
            userFile.getParentFile().mkdirs();

            // Write data to the file
            try (FileWriter fileWriter = new FileWriter(userFile)) {
                fileWriter.write(userList.toString());
                fileWriter.flush();
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }


    

}
