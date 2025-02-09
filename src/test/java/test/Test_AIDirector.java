package test;

import org.example.vetorrally.controller.AIDirector;
import org.example.vetorrally.model.*;
import org.example.vetorrally.view.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class Test_AIDirector {
    private AIDirector aiDirector;
    private Track gameTrack;
    private List<Bot> bots;
    private Renderer renderer;
    private Path tempTrackFile;

    private void createTestTrack() throws IOException {
        // Create a simple test track with three start positions
        String trackContent =
                "3##########\n" +  // 3 indicates 3 cars (1 player, 2 bots)
                        "#        #\n" +
                        "#SSS   F #\n" +   // SSS are three adjacent start positions
                        "#        #\n" +
                        "##########\n";

        tempTrackFile = Files.createTempFile("testTrack", ".txt");
        Files.writeString(tempTrackFile, trackContent);
    }

    @BeforeEach
    void setUp() throws IOException {
        createTestTrack();
        gameTrack = new Track(tempTrackFile.toString());
        renderer = new Renderer(gameTrack.getGrid());
        bots = new ArrayList<>();

        // Create two bots with different strategies
        bots.add(new Bot(gameTrack, gameTrack.getStartLine().get(1), 1, true));  // Chase player
        bots.add(new Bot(gameTrack, gameTrack.getStartLine().get(2), 2, false)); // Chase finish line

        aiDirector = new AIDirector(gameTrack, bots, renderer);
    }

    @Test
    @DisplayName("AIDirector should initialize correctly")
    void testInitialization() {
        assertNotNull(aiDirector);
        assertEquals(2, bots.size());
        assertNotNull(renderer);
    }

    @Test
    @DisplayName("Player position update should be reflected in bot behavior")
    void testPlayerPositionUpdate() {
        Vector2D playerPos = new Vector2D(5, 2);  // Position in middle of track
        aiDirector.updatePlayerPosition(playerPos);

        // Store initial positions
        Vector2D initialPos1 = new Vector2D(bots.get(0).getPosition().getX(), bots.get(0).getPosition().getY());
        Vector2D initialPos2 = new Vector2D(bots.get(1).getPosition().getX(), bots.get(1).getPosition().getY());

        // Move bots
        aiDirector.moveBots();

        // First bot (player chaser) should have moved toward player position
        Bot chasingBot = bots.get(0);
        // Assert that the chasing bot has moved (position changed)
        boolean hasPositionChanged =
                initialPos1.getX() != chasingBot.getPosition().getX() ||
                        initialPos1.getY() != chasingBot.getPosition().getY();
        assertTrue(hasPositionChanged, "Chasing bot should move when player position updates");

        // Second bot (finish line chaser) movement should be independent of player position
        Bot finishBot = bots.get(1);
        Vector2D finishPos = gameTrack.getFinishLine().get(0);
        int initialDistanceToFinish = Math.abs(initialPos2.getX() - finishPos.getX())
                + Math.abs(initialPos2.getY() - finishPos.getY());
        int newDistanceToFinish = Math.abs(finishBot.getPosition().getX() - finishPos.getX())
                + Math.abs(finishBot.getPosition().getY() - finishPos.getY());

        assertTrue(newDistanceToFinish <= initialDistanceToFinish);
    }

    @Test
    @DisplayName("Bots should not collide with boundaries")
    void testBoundaryCollisionAvoidance() {
        // Move player near boundary to test chasing bot's collision avoidance
        Vector2D nearBoundary = new Vector2D(1, 1);  // Next to wall
        aiDirector.updatePlayerPosition(nearBoundary);

        aiDirector.moveBots();

        // Check that no bot is on a boundary
        for (Bot bot : bots) {
            assertNotEquals(TrackElement.BOUNDARY,
                    gameTrack.getTrackElement(bot.getPosition()));
        }
    }

    @Test
    @DisplayName("Multiple bot movements should be handled correctly")
    void testMultipleBotMovements() {
        Vector2D playerPos = new Vector2D(5, 2);
        aiDirector.updatePlayerPosition(playerPos);

        // Store initial positions
        List<Vector2D> initialPositions = new ArrayList<>();
        for (Bot bot : bots) {
            initialPositions.add(new Vector2D(bot.getPosition().getX(), bot.getPosition().getY()));
        }

        // Move bots multiple times
        for (int i = 0; i < 3; i++) {
            aiDirector.moveBots();
        }

        // Verify each bot has moved from its initial position
        for (int i = 0; i < bots.size(); i++) {
            Vector2D initialPos = initialPositions.get(i);
            Vector2D currentPos = bots.get(i).getPosition();

            assertFalse(
                    initialPos.getX() == currentPos.getX() && initialPos.getY() == currentPos.getY(),
                    "Bot " + i + " should have moved from its initial position"
            );
        }
    }

    @Test
    @DisplayName("Renderer should reflect bot movements")
    void testRendererUpdates() {
        Vector2D playerPos = new Vector2D(5, 2);
        aiDirector.updatePlayerPosition(playerPos);

        // Get initial bot positions from renderer
        TrackElement[][] initialGrid = gameTrack.getGrid().clone();

        // Move bots
        aiDirector.moveBots();

        // Verify that the renderer has updated to show new positions
        boolean positionsChanged = false;
        for (Bot bot : bots) {
            Vector2D pos = bot.getPosition();
            if (gameTrack.getTrackElement(pos) == TrackElement.OCCUPIED) {
                positionsChanged = true;
                break;
            }
        }

        assertTrue(positionsChanged, "Renderer should show updated bot positions");
    }
}