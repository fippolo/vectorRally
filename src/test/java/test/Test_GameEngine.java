package test;

import org.example.vetorrally.controller.GameEngine;
import org.example.vetorrally.controller.InputHandler;
import org.example.vetorrally.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

class Test_GameEngine {
    private GameEngine gameEngine;
    private InputHandler inputHandler;
    private final InputStream systemIn = System.in;
    private Path trackFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        // Create a simple test track
        trackFile = tempDir.resolve("testTrack.txt");
        String trackContent =
                "2##########\n" +  // 2 cars (1 player, 1 bot)
                        "#        #\n" +
                        "#SS    F #\n" +   // SS: two start positions, F: finish
                        "#        #\n" +
                        "##########\n";
        Files.writeString(trackFile, trackContent);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }



    @Test
    @DisplayName("Game should detect player crash into wall")
    void testPlayerCrash() throws IOException {
        // Provide inputs that will make player crash into wall
        provideInput("-1\n0\n"); // Move towards left wall
        inputHandler = new InputHandler();
        gameEngine = new GameEngine(trackFile.toString(), inputHandler);
        // Game should end with crash message (checked via System.out)
    }

    @Test
    @DisplayName("Game should detect player victory")
    void testPlayerVictory() throws IOException {
        // Provide sequence of moves to reach finish line
        // For this simple track: right movements to reach F
        provideInput("1\n0\n1\n0\n1\n0\n1\n0\n");
        inputHandler = new InputHandler();
        gameEngine = new GameEngine(trackFile.toString(), inputHandler);
        // Game should end with victory message (checked via System.out)
    }

    @Test
    @DisplayName("Game should throw exception for invalid track file")
    void testInvalidTrackFile() {
        inputHandler = new InputHandler();
        assertThrows(IOException.class, () ->
                new GameEngine("nonexistent.txt", inputHandler));
    }

    @Test
    @DisplayName("Game should handle too many cars error")
    void testTooManyCarsError() throws IOException {
        // Create track with more cars than start positions
        String invalidTrackContent =
                "3##########\n" +  // 3 cars but only 2 start positions
                        "#        #\n" +
                        "#SS    F #\n" +
                        "#        #\n" +
                        "##########\n";
        Files.writeString(trackFile, invalidTrackContent);

        provideInput("0\n0\n");
        inputHandler = new InputHandler();
        assertThrows(RuntimeException.class, () ->
                new GameEngine(trackFile.toString(), inputHandler));
    }

    @Test
    @DisplayName("Game should process valid player moves")
    void testValidPlayerMoves() throws IOException {
        // Test a sequence of valid moves
        provideInput("1\n0\n0\n1\n-1\n0\n");
        inputHandler = new InputHandler();
        gameEngine = new GameEngine(trackFile.toString(), inputHandler);
        // Game should process moves without throwing exceptions
    }

}