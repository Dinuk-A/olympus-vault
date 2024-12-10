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
        System.out.println("################################");
        System.out.println("################################");
        System.out.println("######### OLYMPUS VAULT ########");
        System.out.println("################################");
        System.out.println("################################");
        System.out.println();

        // System.out.println(GREEN + "This is green text!" + RESET);
        // System.out.println(RED + "This is red text!" + RESET);
        // System.out.println(BLUE + "This is blue text!" + RESET);
        // System.out.println("This is normal text, without color.");

        // demonstrateColors();

    }

    //test
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";

    //test colours
    public static void demonstrateColors() {
        // 1. Standard Colors
        System.out.println("Standard Foreground Colors:");
        for (int i = 30; i <= 37; i++) {
            System.out.printf("\u001B[%dmColor %d\u001B[0m\n", i, i - 30);
        }

        // 2. Bright Colors
        System.out.println("\nBright Foreground Colors:");
        for (int i = 90; i <= 97; i++) {
            System.out.printf("\u001B[%dmBright Color %d\u001B[0m\n", i, i - 90);
        }

        // 3. Background Colors
        System.out.println("\nBackground Colors:");
        for (int i = 40; i <= 47; i++) {
            System.out.printf("\u001B[%dmBackground Color %d\u001B[0m\n", i, i - 40);
        }

        // 4. Bright Background Colors
        System.out.println("\nBright Background Colors:");
        for (int i = 100; i <= 107; i++) {
            System.out.printf("\u001B[%dmBright Background Color %d\u001B[0m\n", i, i - 100);
        }

        // 5. 256 Colors (foreground only)
        System.out.println("\n256 Colors (Foreground):");
        for (int i = 0; i < 16; i++) {
            System.out.printf("\u001B[38;5;%dmColor %d\u001B[0m ", i, i);
        }
        System.out.println();

        // 6. True Color RGB Example (foreground and background)
        System.out.println("\nTrueColor (RGB) Example:");
        System.out.printf("\u001B[38;2;255;0;0mForeground RGB (255, 0, 0)\u001B[0m\n");
        System.out.printf("\u001B[48;2;0;255;0mBackground RGB (0, 255, 0)\u001B[0m\n");

        // Add a little spacing for clarity
        System.out.println("\n");

        // Reset to default
        System.out.println("\u001B[0mEnd of Color Demonstration.");
    }
    
}
