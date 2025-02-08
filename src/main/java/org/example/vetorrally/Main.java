package org.example.vetorrally;

import org.example.vetorrally.controller.GameEngine;
import org.example.vetorrally.controller.InputHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    // entry point
    public static void main(String[] args) {
        try{
            InputHandler inputHandler = new InputHandler();

            String filePathString = inputHandler.getStringInput("Please enter the path to the file: ");

            // Convert the string to a Path object
            Path filePath = Paths.get(filePathString);

            // Print the Path object to verify the input
            System.out.println("You entered the file path: " + filePath);
            new GameEngine(filePath.toString(), inputHandler);
            inputHandler.cleanup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
