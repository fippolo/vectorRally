package test;

import org.example.vetorrally.model.Vector2D;
import org.example.vetorrally.model.Bot;
import org.example.vetorrally.model.Track;
import org.example.vetorrally.model.TrackElement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class Test_Bot {
    private Track track;
    private Bot bot;
    private Vector2D startPosition;
    private Path tempTrackFile;

    private void createTestTrack() throws IOException {
        // Create a test track with two start positions
        String trackContent =
                        "2##########\n" +  // 2 indicates 2 cars (1 player, 1 bot)
                        "#        #\n" +
                        "#SS    F #\n" +   // SS are two adjacent start positions
                        "#        #\n" +
                        "##########\n";

        // Create temporary file
        tempTrackFile = Files.createTempFile("testTrack", ".txt");
        Files.writeString(tempTrackFile, trackContent);
    }

    @BeforeEach
    void setUp() throws IOException {
        createTestTrack();
        track = new Track(tempTrackFile.toString());

        // Verify we have exactly two start positions
        assertEquals(2, track.getStartLine().size(), "Track should have exactly two start positions");

        // Get the second start position for the bot (index 1)
        startPosition = track.getStartLine().get(1);
        bot = new Bot(track, startPosition, 1, true); // ID 1, chasePlayer true
    }

    @Test
    @DisplayName("Bot constructor should initialize at correct start position")
    void testConstructor() {
        // Verify bot starts at the second start position
        assertEquals(startPosition.getX(), bot.getPosition().getX());
        assertEquals(startPosition.getY(), bot.getPosition().getY());
        assertEquals(0, bot.getSpeed().getX());
        assertEquals(0, bot.getSpeed().getY());

        // Verify start position is different from first start position (player's)
        Vector2D playerStart = track.getStartLine().get(0);
        assertNotEquals(playerStart.getX(), bot.getPosition().getX());
    }

    @Test
    @DisplayName("Bot should update player position correctly and chase player")
    void testChasePlayer() {
        // Use the first start position as initial player position
        Vector2D playerPos = track.getStartLine().get(0);
        bot.updatePlayerPosition(playerPos);

        Vector2D nextMove = bot.findNextMove();
        assertNotNull(nextMove);

        // Bot should move towards player
        int distanceToPlayerBefore = Math.abs(bot.getPosition().getX() - playerPos.getX()) +
                Math.abs(bot.getPosition().getY() - playerPos.getY());

        int distanceToPlayerAfter = Math.abs(nextMove.getX() - playerPos.getX()) +
                Math.abs(nextMove.getY() - playerPos.getY());

        // The next move should be closer to the player
        assertTrue(distanceToPlayerAfter <= distanceToPlayerBefore);
    }

    @Test
    @DisplayName("Bot should chase finish line when in finish line chase mode")
    void testChaseFinishLine() {
        // Create a bot in finish line chase mode
        Bot finishLineBot = new Bot(track, startPosition, 2, false);

        Vector2D nextMove = finishLineBot.findNextMove();
        assertNotNull(nextMove);

        // Calculate distances to finish line
        Vector2D finishPos = track.getFinishLine().get(0);
        int distanceToFinishBefore = Math.abs(finishLineBot.getPosition().getX() - finishPos.getX()) +
                Math.abs(finishLineBot.getPosition().getY() - finishPos.getY());

        int distanceToFinishAfter = Math.abs(nextMove.getX() - finishPos.getX()) +
                Math.abs(nextMove.getY() - finishPos.getY());

        // The next move should be closer to the finish line
        assertTrue(distanceToFinishAfter <= distanceToFinishBefore);
    }

    @Test
    @DisplayName("Bot should not move through boundaries")
    void testBoundaryCollision() {
        // Try to move towards boundary
        Vector2D boundaryPos = new Vector2D(0, 2); // Wall position

        // The move should throw a RuntimeException
        assertThrows(RuntimeException.class, () -> {
            bot.moveTo(boundaryPos);
        }, "Moving into a boundary should throw RuntimeException");

        // Position should remain unchanged
        assertEquals(startPosition.getX(), bot.getPosition().getX());
        assertEquals(startPosition.getY(), bot.getPosition().getY());
    }

    @Test
    @DisplayName("Bot should execute valid moves correctly")
    void testValidMove() {
        Vector2D initialPos = new Vector2D(bot.getPosition().getX(), bot.getPosition().getY());
        Vector2D targetPos = new Vector2D(initialPos.getX() + 1, initialPos.getY());

        bot.moveTo(targetPos);

        // Position should have changed
        assertNotEquals(initialPos.getX(), bot.getPosition().getX());
        // But should still be within track bounds
        assertTrue(bot.getPosition().getX() > 0 &&
                bot.getPosition().getX() < track.getWidth() - 1);
    }

    @Test
    @DisplayName("Bot should properly handle unreachable targets")
    void testUnreachableTarget() {
        // Set player position behind a wall
        Vector2D unreachablePos = new Vector2D(0, 0); // Corner position (wall)
        bot.updatePlayerPosition(unreachablePos);

        Vector2D nextMove = bot.findNextMove();

        // Should still return a valid move
        assertNotNull(nextMove);
        // Move should be within track bounds
        assertTrue(nextMove.getX() > 0 && nextMove.getX() < track.getWidth() - 1);
        assertTrue(nextMove.getY() > 0 && nextMove.getY() < track.getHeight() - 1);
    }

    @Test
    @DisplayName("Bot speed should accumulate correctly during movement")
    void testSpeedAccumulation() {
        Vector2D targetPos = new Vector2D(bot.getPosition().getX() + 1, bot.getPosition().getY());
        bot.moveTo(targetPos);

        Vector2D firstSpeed = new Vector2D(bot.getSpeed().getX(), bot.getSpeed().getY());

        // Move in same direction again
        targetPos = new Vector2D(bot.getPosition().getX() + 2, bot.getPosition().getY());
        bot.moveTo(targetPos);

        // Speed should have increased
        assertTrue(Math.abs(bot.getSpeed().getX()) > Math.abs(firstSpeed.getX()));
    }

    @Test
    @DisplayName("Both start positions should be valid track positions")
    void testStartPositionsValidity() {
        for (Vector2D startPos : track.getStartLine()) {
            assertEquals(TrackElement.START, track.getTrackElement(startPos));
        }
    }
}