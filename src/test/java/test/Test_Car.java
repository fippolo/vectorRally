package test;

import org.example.vetorrally.model.Car;
import org.example.vetorrally.model.Vector2D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class Test_Car {
    private Car car;
    private Vector2D startPosition;

    @BeforeEach
    void setUp() {
        startPosition = new Vector2D(5, 5);
        car = new Car(startPosition);
    }

    @Test
    @DisplayName("Constructor should initialize car with correct position and zero speed")
    void testConstructor() {
        assertEquals(5, car.getPosition().getX());
        assertEquals(5, car.getPosition().getY());
        assertEquals(0, car.getSpeed().getX());
        assertEquals(0, car.getSpeed().getY());
    }

    @Test
    @DisplayName("getNextPosition should return current position when no movement made")
    void testGetNextPositionNoMovement() {
        Vector2D nextPos = car.getNextPosition();
        assertEquals(startPosition.getX(), nextPos.getX());
        assertEquals(startPosition.getY(), nextPos.getY());
    }

    @Test
    @DisplayName("Move with valid delta should update position and speed correctly")
    void testValidMove() {
        Vector2D delta = new Vector2D(1, 1);
        car.move(delta);

        // Check speed updated correctly
        assertEquals(1, car.getSpeed().getX());
        assertEquals(1, car.getSpeed().getY());

        // Check position updated correctly (position = startPos + speed)
        assertEquals(6, car.getPosition().getX());
        assertEquals(6, car.getPosition().getY());
    }

    @Test
    @DisplayName("Move with invalid delta should throw RuntimeException")
    void testInvalidMove() {
        Vector2D invalidDelta = new Vector2D(2, 0);
        assertThrows(RuntimeException.class, () -> car.move(invalidDelta));
    }

    @Test
    @DisplayName("Multiple moves should accumulate speed (inertia)")
    void testInertia() {
        // First move
        car.move(new Vector2D(1, 0));
        assertEquals(1, car.getSpeed().getX());
        assertEquals(0, car.getSpeed().getY());
        assertEquals(6, car.getPosition().getX());
        assertEquals(5, car.getPosition().getY());

        // Second move - speed should accumulate
        car.move(new Vector2D(1, 0));
        assertEquals(2, car.getSpeed().getX());
        assertEquals(0, car.getSpeed().getY());
        assertEquals(8, car.getPosition().getX());
        assertEquals(5, car.getPosition().getY());
    }

    @Test
    @DisplayName("getNextPosition should correctly predict position based on current speed")
    void testGetNextPositionAfterMove() {
        car.move(new Vector2D(1, 1));
        Vector2D nextPos = car.getNextPosition();

        // Next position should be current position + current speed
        assertEquals(7, nextPos.getX());
        assertEquals(7, nextPos.getY());
    }

    @Test
    @DisplayName("Move with negative deltas should work correctly")
    void testNegativeMove() {
        Vector2D delta = new Vector2D(-1, -1);
        car.move(delta);

        assertEquals(-1, car.getSpeed().getX());
        assertEquals(-1, car.getSpeed().getY());
        assertEquals(4, car.getPosition().getX());
        assertEquals(4, car.getPosition().getY());
    }

    @Test
    @DisplayName("Move with zero deltas should not change speed or position")
    void testZeroMove() {
        Vector2D delta = new Vector2D(0, 0);
        car.move(delta);

        assertEquals(0, car.getSpeed().getX());
        assertEquals(0, car.getSpeed().getY());
        assertEquals(5, car.getPosition().getX());
        assertEquals(5, car.getPosition().getY());
    }
}