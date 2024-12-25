package com.dinuka;

import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.SecretKey;

import java.nio.file.Files;

import java.io.File;

import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;

public class InputHandler {

    private static final Scanner scan = new Scanner(System.in);

    // Prevent instantiation
    private InputHandler() {
    }

    // 1 for create new profile or exit
    public static void displayOptionsUNNotFound() {
        System.out.println("No user profile found. Please choose your next action:");
        System.out.println("1. Create a new profile");
        System.out.println("2. Exit the application");
        System.out.println("Please enter your choice (1 or 2):");
    }

    // 2
    public static int getUserChoice() {

        while (true) {
            try {

                // Clear any leftover input
                // scan.nextLine();

                if (scan.hasNextInt()) {
                    int choice = scan.nextInt();

                    return choice;
                } else {

                    // Handle non-integer input
                    handleInvalidInput();
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred 123.");

            }
        }
    }

    // 3
    private static void handleInvalidInput() {

        String invalidInput = scan.next();
        System.out.println("Invalid input. '" + invalidInput + "' is not a valid number.");
        System.out.println("Please try again.");

    }

    // 4 main one
    public static void handleUserNotFound() {
        while (true) {
            displayOptionsUNNotFound();
            int userChoice = getUserChoice();

            switch (userChoice) {
                case 1:
                    UserManager.createNewProfile();
                    // Break out of the loop after profile creation
                    return; // Exit the method
                case 2:
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // check pws
    public static boolean validatePw() {
        Scanner scanner = new Scanner(System.in);
        JSONArray userList = UserManager.getUserList();
        String username = UserManager.getLAstCheckedUserName();

        for (int attempt = 0; attempt < 3; attempt++) {
            System.out.print("Enter your password (or type 'EXIT' to quit): ");
            String enteredPw = scanner.nextLine();

            if (enteredPw.equalsIgnoreCase("EXIT")) {
                System.out.println("Exiting...");
                System.exit(0);
            }

            for (int i = 0; i < userList.length(); i++) {
                JSONObject user = userList.getJSONObject(i);

                if (user.getString("username").equals(username)) {
                    String storedPw = user.getString("password");

                    if (BCrypt.checkpw(enteredPw, storedPw)) {
                        System.out.println("Password validated successfully!");

                        // Derive AES key and store it in SessionManager
                        // byte[] salt = Utils.generateSalt(); // Retrieve or generate the salt
                        try {
                            SecretKey aesKey = Utils.deriveKey(enteredPw);
                            SessionManager.setAESKey(aesKey); // Store the key for this session
                            return true; // Indicate successful validation
                        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                            System.err.println("Error deriving AES key: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Incorrect password. Try again. You have " + (2 - attempt) + " attempts.");
                    }
                }
            }
        }
        System.out.println("Maximum attempts reached. Exiting...");
        System.exit(0);
        return false;
    }

    public static void mainMenu() {
        Scanner scannerMM = new Scanner(System.in);

        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Create a New Password Record");
        System.out.println("2. View all records");
        System.out.println("3. Exit");
        System.out.println();
        System.out.print("Choose an option: ");

        String choice = scannerMM.nextLine();

        switch (choice) {
            case "1":
                System.out.println("Creating a New Password Record");
                MainOps.createNewRecord();
                break;
            case "2":
                System.out.println("Viewing all records");
                MainOps.viewAllRecs();

                // The method just waits for the next action and moves on to the next line of
                // code after scannerMM.nextLine().

                System.out.println("\nPress Enter to go back to the main menu...");
                scannerMM.nextLine();

                break;
            case "3":
                System.out.println("Exiting the application. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    // show options when a new rec opened
    public static void showRecOpts(JSONObject selectedRec) {
        System.out.println();
        System.out.println("++++++ Record Options +++++");
        System.out.println("1. Edit the selected record");
        System.out.println("2. Delete the selected record");
        System.out.println("3. Exit");
        System.out.println();
        System.out.print("Please choose an option (1, 2, or 3): ");
    
        Scanner pwRecOptScanner = new Scanner(System.in);
        String choice = pwRecOptScanner.nextLine();
    
        switch (choice) {
            case "1":
                System.out.println("You chose to edit the record.");
                System.out.println("Editing the record for: " + selectedRec.getString("name"));
                MainOps.editRecord(selectedRec);
                break;
            case "2":
                System.out.println("You chose to delete the record.");
                System.out.println("Deleting the record for: " + selectedRec.getString("name"));
                MainOps.deleteRecord(selectedRec);
                break;
            case "3":
                System.out.println("Exiting the options menu.");
                break;
            default:
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
        }
    }
    
}
