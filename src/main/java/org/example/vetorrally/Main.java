package org.example.vetorrally;

import org.example.vetorrally.controller.GameEngine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    // entry point
    public static void main(String[] args) {
        try{
            Scanner scanner = new Scanner(System.in);

            // Prompt the user to enter a file path
            System.out.print("Please enter the file path: ");
            String filePathString = scanner.nextLine();

            // Convert the string to a Path object
            Path filePath = Paths.get(filePathString);

            // Print the Path object to verify the input
            System.out.println("You entered the file path: " + filePath);

            // Close the scanner to free up resources
            scanner.close();
            GameEngine newGame = new GameEngine(filePath.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
