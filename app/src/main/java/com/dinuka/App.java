package com.dinuka;

// import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        welcomeMsg();

        while (true) { // Keep the app running
            if (!UserManager.checkUserNameExistance()) {
                System.out.println("NAME NOT FOUND");
                InputHandler.handleUserNotFound();
            } else {
                Utils.clearConsole();
                System.out.println("Welcome back!");

                if (InputHandler.validatePw()) {
                    // Utils.clearConsole();
                    System.out.println("You have entered the main application!");

                    while (true) { // Main menu loop
                        InputHandler.mainMenu();
                    }

                }

                break;
            }
        }

        // InputHandler.mainMenu();

    }

    public static void welcomeMsg() {
     
        System.out.println();
        System.out.println(BLUE + "##############################################" + RESET);
        System.out.println(BLUE + "##############################################" + RESET);
        System.out.println(BLUE + "###############" + RESET + GREEN + " OLYMPUS VAULT " + RESET + BLUE + "################" + RESET);
        System.out.println(BLUE + "##############################################" + RESET);
        System.out.println(BLUE + "##############################################" + RESET);
        System.out.println();
    }

    // Color codes
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[44m"; 


}
