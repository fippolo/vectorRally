package test;

import org.example.vetorrally.model.TrackElement;
import org.example.vetorrally.model.Vector2D;
import org.example.vetorrally.view.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class Test_Renderer {
    private Renderer renderer;
    private TrackElement[][] testGrid;

    @BeforeEach
    void setUp() {
        // Create a simple test grid
        testGrid = new TrackElement[][] {
                {TrackElement.BOUNDARY, TrackElement.BOUNDARY, TrackElement.BOUNDARY},
                {TrackElement.BOUNDARY, TrackElement.TRACK, TrackElement.BOUNDARY},
                {TrackElement.BOUNDARY, TrackElement.BOUNDARY, TrackElement.BOUNDARY}
        };
        renderer = new Renderer(testGrid);
    }

    @Test
    @DisplayName("Setting occupied space should update grid correctly")
    void testSetOccupied() {
        Vector2D pos = new Vector2D(1, 1); // Center position
        renderer.setOccupied(pos);

        // Print updated grid for debugging
        renderer.printTrack();

        // Create new renderer to read the grid and verify changes
        TrackElement[][] currentGrid = testGrid.clone();
        assertEquals(TrackElement.OCCUPIED, currentGrid[1][1],
                "Center position should be marked as occupied");
    }

    @Test
    @DisplayName("Setting occupied space outside grid should be handled safely")
    void testSetOccupiedOutOfBounds() {
        Vector2D outPos = new Vector2D(5, 5);
        assertDoesNotThrow(() -> renderer.setOccupied(outPos),
                "Setting occupied space outside grid should not throw exception");
    }

    @Test
    @DisplayName("Setting player position should update grid correctly")
    void testSetOccupiedByPlayer() {
        renderer.setOccupiedByPlayer(1, 1);
        TrackElement[][] currentGrid = testGrid.clone();
        assertEquals(TrackElement.PLAYER, currentGrid[1][1],
                "Center position should be marked as player position");
    }

    @Test
    @DisplayName("Clearing occupied spaces should work correctly")
    void testClearAllOccupiedSpaces() {
        // First occupy some spaces
        renderer.setOccupied(new Vector2D(1, 1));
        renderer.setOccupiedByPlayer(1, 1);

        // Clear all occupied spaces
        renderer.clearAllOccupiedSpaces();

        // Verify spaces are cleared
        TrackElement[][] currentGrid = testGrid.clone();
        assertEquals(TrackElement.TRACK, currentGrid[1][1],
                "Occupied spaces should be cleared back to TRACK");
    }

    @Test
    @DisplayName("Clearing single occupied space should work correctly")
    void testClearOccupiedSpace() {
        Vector2D pos = new Vector2D(1, 1);
        renderer.setOccupied(pos);
        renderer.clearOcucpiedSpace(pos);

        TrackElement[][] currentGrid = testGrid.clone();
        assertEquals(TrackElement.TRACK, currentGrid[1][1],
                "Cleared space should return to TRACK");
    }

    @Test
    @DisplayName("Projecting car position should update grid correctly")
    void testProjectCarCenterPos() {
        Vector2D projPos = new Vector2D(1, 1);
        renderer.projectCarCenterPos(projPos);

        TrackElement[][] currentGrid = testGrid.clone();
        assertEquals(TrackElement.NEXTPROJECTION, currentGrid[1][1],
                "Projection should be marked on grid");
    }

    @Test
    @DisplayName("Projecting position outside grid should be handled safely")
    void testProjectOutOfBounds() {
        Vector2D outPos = new Vector2D(5, 5);
        assertDoesNotThrow(() -> renderer.projectCarCenterPos(outPos),
                "Projecting outside grid should not throw exception");
    }

    @Test
    @DisplayName("Multiple operations sequence should work correctly")
    void testOperationSequence() {
        Vector2D pos = new Vector2D(1, 1);

        // Sequence: project -> occupy -> clear -> project again
        renderer.projectCarCenterPos(pos);
        renderer.setOccupied(pos);
        renderer.clearOcucpiedSpace(pos);
        renderer.projectCarCenterPos(pos);

        TrackElement[][] currentGrid = testGrid.clone();
        assertEquals(TrackElement.NEXTPROJECTION, currentGrid[1][1],
                "Final state should show projection");
    }

    @Test
    @DisplayName("Projection should not override occupied spaces")
    void testProjectionOverOccupied() {
        Vector2D pos = new Vector2D(1, 1);
        renderer.setOccupied(pos);
        renderer.projectCarCenterPos(pos);

        TrackElement[][] currentGrid = testGrid.clone();
        assertEquals(TrackElement.OCCUPIED, currentGrid[1][1],
                "Occupied space should not be overridden by projection");
    }

    @Test
    @DisplayName("Projection should be cleared when new projection is made")
    void testProjectionClearing() {
        // First projection
        Vector2D pos1 = new Vector2D(1, 1);
        renderer.projectCarCenterPos(pos1);

        // Second projection at different location
        Vector2D pos2 = new Vector2D(1, 2);
        renderer.projectCarCenterPos(pos2);

        TrackElement[][] currentGrid = testGrid.clone();
        assertEquals(TrackElement.TRACK, currentGrid[1][1],
                "Old projection should be cleared");
        assertEquals(TrackElement.BOUNDARY, currentGrid[2][1],
                "New projection should not be visible because it's out fo bound");
    }
}