package com.dinuka;

import java.util.Scanner;

public class App {
    // public String getGreeting() {
    // return "Hello World!";
    // }

    // inside main >>> // System.out.println(new App().getGreeting());

    public static void main(String[] args) {

        welcomeMsg();

        if (!UserManager.checkUserNameExistance()) {

            System.out.println("Given Username not found");
            System.out.println();

            Scanner scan = new Scanner(System.in);
            int userChoice;

            while (true) {

                try {
                    System.out.println("Choose Next Action ");
                    System.out.println("Enter 1 for create a new profile");
                    System.out.println("Enter 2 for Exit");

                    if (scan.hasNextInt()) {
                        userChoice = scan.nextInt();

                        if (userChoice == 1) {
                            UserManager.createNewProfile();
                            break;
                        } else if (userChoice == 2) {
                            System.exit(0);
                        } else {
                            // how to run the same thing again in here????
                        }

                    } else {
                        String invalidInput = scan.next();
                        System.out.println("Invalid input. '" + invalidInput + "' is not a number. Please try again.");
                    }

                } catch (Exception e) {

                    System.out.println("An error occurred. Please enter a valid number.");
                    scan.next(); // Clear the invalid input

                }

            }

            scan.close();

        }

    }

    public static void welcomeMsg() {

        System.out.println();
        System.out.println("################################");
        System.out.println("################################");
        System.out.println("######### OLYMPUS VAULT ########");
        System.out.println("################################");
        System.out.println("################################");
        System.out.println();

    }
}
