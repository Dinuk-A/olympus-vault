package com.dinuka;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

// import org.json.JSONArray;

public class UserManager {

    private static final String USER_DB_FILE = "resources/users.json";

    // load users from file
    // public static JSONArray loadUsers() {

    //     File myFile = new File(USER_DB_FILE);

    //     if (!myFile.exists()) {
    //         return new JSONArray();
    //     }

    //     try (FileReader reader = new FileReader(myFile)) {
    //         StringBuilder content = new StringBuilder();
    //         int i;

    //         while ((i = reader.read()) != -1) {
    //             content.append((char) i);
    //         }
    //         return new JSONArray(content.toString());

    //     } catch (IOException e) {

    //         e.printStackTrace();
    //         return new JSONArray();
    //     }

    // }

    // Method to save users to the JSON file
    // public static void saveUsers(JSONArray users) {
    //     try (FileWriter writer = new FileWriter(USER_DB_FILE)) {
    //         writer.write(users.toString());
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    //

}
