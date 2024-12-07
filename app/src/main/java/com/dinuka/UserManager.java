package com.dinuka;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class UserManager {

    // get the users data by reading the user.json file
    public static JSONArray getUserList() {

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
                return true;
            }
        }

        return false;
    }

    // need to create ORIGINAL
    // public static void createNewProfile() {

    //     System.out.println("to profile creation");
    //     System.out.println("this will implemented later");

    // }

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
    
        // Create a new JSON object for the profile
        JSONObject newUser = new JSONObject();
        newUser.put("username", newUsername);
        newUser.put("password", newPassword); // Ideally, hash the password for security
    
        // Add to the user list
        userList.put(newUser);
    
        // Save back to the file
        saveUserList(userList);
    
        System.out.println("Profile created successfully!");
    }

    // public static void saveUserList(JSONArray userList) {
    //     final String USER_DB_FILE = "resources/users.json";
    
    //     try (FileWriter fileWriter = new FileWriter(USER_DB_FILE)) {
    //         fileWriter.write(userList.toString());
    //         fileWriter.flush();
    //     } catch (IOException e) {
    //         System.out.println("Error saving user data: " + e.getMessage());
    //     }
    // }

    public static void saveUserList(JSONArray userList) {
        final String USER_DB_FILE = "resources/users.json";
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
