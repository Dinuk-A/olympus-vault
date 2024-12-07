package com.dinuka;

import java.util.Scanner;

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
}
