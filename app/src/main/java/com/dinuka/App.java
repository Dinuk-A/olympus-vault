package com.dinuka;

import java.util.Scanner;

public class App {
    // public String getGreeting() {
    // return "Hello World!";
    // }

    // inside main >>> // System.out.println(new App().getGreeting());

    public static void main(String[] args) {

        welcomeMsg();

        while (true) { // Keep the app running
            if (!UserManager.checkUserNameExistance()) {
                System.out.println("NAME NOT FOUND");
                InputHandler.handleUserNotFound();
            } else {
                System.out.println("Welcome back!");
                // Proceed with the main application logic here
                break; // Exit the loop or add a main menu loop
            }
        }

        // if (!UserManager.checkUserNameExistance()) {

        //     System.out.println("user name not found");
        //     InputHandler.handleUserNotFound();

        // }

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
