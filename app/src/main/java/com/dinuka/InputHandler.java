package com.dinuka;

import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class InputHandler {

    private static final Scanner scan = new Scanner(System.in);

    // Prevent instantiation
    private InputHandler() {
    }

    // 1 for create new profile or exit
    public static void displayOptionsUNNotFound() {

        System.out.println("Choose Next Action ");
        System.out.println("Enter 1 for create a new profile");
        System.out.println("Enter 2 for Exit");

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
                        return true;
                    } else {
                        System.out.println("Incorrect password. Try again. You have " + (2 - attempt) + " attempts");
                    }
                }
            }
        }
        System.out.println("Maximum attempts reached. Exiting...");
        System.exit(0);

        return false;
    }
}
