package test;

import org.example.vetorrally.model.Player;
import org.example.vetorrally.model.Vector2D;
import org.example.vetorrally.controller.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class Test_Player {
    private Player player;
    private Vector2D startPosition;
    private InputHandler inputHandler;

    @BeforeEach
    void setUp() {
        startPosition = new Vector2D(5, 5);
        inputHandler = new InputHandler();
        player = new Player(startPosition, inputHandler);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    @DisplayName("Player constructor should initialize correctly")
    void testConstructor() {
        assertEquals(startPosition.getX(), player.getPosition().getX());
        assertEquals(startPosition.getY(), player.getPosition().getY());
        assertEquals(0, player.getSpeed().getX());
        assertEquals(0, player.getSpeed().getY());
    }

    @Test
    @DisplayName("Player should move correctly with valid input")
    void testValidMove() {
        // Simulate input for move right (1,0)
        String input = "1\n0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Create new inputHandler and player with this input
        InputHandler testInputHandler = new InputHandler();
        Player testPlayer = new Player(new Vector2D(5,5), testInputHandler);

        testPlayer.nextmove();

        // Check if moved right
        assertEquals(6, testPlayer.getPosition().getX());
        assertEquals(5, testPlayer.getPosition().getY());
        assertEquals(1, testPlayer.getSpeed().getX());
        assertEquals(0, testPlayer.getSpeed().getY());

        // Reset System.in
        System.setIn(System.in);
    }

    @Test
    @DisplayName("Player should handle diagonal movement")
    void testDiagonalMove() {
        // Simulate input for move diagonal (1,1)
        String input = "1\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Create new inputHandler and player with this input
        InputHandler testInputHandler = new InputHandler();
        Player testPlayer = new Player(new Vector2D(5,5), testInputHandler);

        testPlayer.nextmove();

        // Check if moved diagonally
        assertEquals(6, testPlayer.getPosition().getX());
        assertEquals(6, testPlayer.getPosition().getY());
        assertEquals(1, testPlayer.getSpeed().getX());
        assertEquals(1, testPlayer.getSpeed().getY());

        // Reset System.in
        System.setIn(System.in);
    }

    @Test
    @DisplayName("Player should handle consecutive moves and accumulate speed")
    void testSpeedAccumulation() {
        // Provide input for both moves at once: "1 0 1 0" (two right moves)
        provideInput("1\n0\n1\n0\n");

        InputHandler inputHandler = new InputHandler();
        Player player = new Player(new Vector2D(5,5), inputHandler);

        // First move
        player.nextmove();

        // Verify first move
        assertEquals(6, player.getPosition().getX());
        assertEquals(5, player.getPosition().getY());
        assertEquals(1, player.getSpeed().getX());
        assertEquals(0, player.getSpeed().getY());

        // Second move
        player.nextmove();

        // Verify speed accumulated
        assertEquals(2, player.getSpeed().getX());
        assertEquals(0, player.getSpeed().getY());
        assertEquals(8, player.getPosition().getX());
        assertEquals(5, player.getPosition().getY());


    }

    @Test
    @DisplayName("Player should handle invalid input by recursing")
    void testInvalidInput() {
        // Simulate invalid input followed by valid input
        provideInput("2\n3\n1\n0\n"); // First try invalid (2,3), then valid (1,0)


        // Create new inputHandler and player with this input
        InputHandler testInputHandler = new InputHandler();
        Player testPlayer = new Player(new Vector2D(5,5), testInputHandler);

        testPlayer.nextmove();

        // Should have used the valid input
        assertEquals(6, testPlayer.getPosition().getX());
        assertEquals(5, testPlayer.getPosition().getY());
    }

    @Test
    @DisplayName("Player getNextPosition should correctly predict position")
    void testGetNextPosition() {
        // First move (1,0)
        String input = "1\n0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Create new inputHandler and player with this input
        InputHandler testInputHandler = new InputHandler();
        Player testPlayer = new Player(new Vector2D(5,5), testInputHandler);

        testPlayer.nextmove();

        Vector2D nextPos = testPlayer.getNextPosition();
        assertEquals(7, nextPos.getX()); // Current position (6) + speed (1)
        assertEquals(5, nextPos.getY());

        // Reset System.in
        System.setIn(System.in);
    }
}