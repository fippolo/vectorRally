package org.example.vetorrally.controller;

import java.util.Scanner;


public class InputHandler {
    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public int getIntInput(String prompt) {
        System.out.println(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public String getStringInput(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    public void cleanup() {
        scanner.close();
    }
}

