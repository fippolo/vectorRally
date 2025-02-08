package test_Car;

import org.example.vetorrally.model.Car;


import org.example.vetorrally.model.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarTest {
    private Car car;
    private Vector2D startPosition;

    @BeforeEach
    void setUp() {
        startPosition = new Vector2D(5, 5);
        car = new Car(startPosition);
    }

    @Test
    void constructor_ShouldInitializeWithCorrectValues() {
        assertEquals(5, car.getPosition().getX());
        assertEquals(5, car.getPosition().getY());
        assertEquals(0, car.getSpeed().getX());
        assertEquals(0, car.getSpeed().getY());
    }

    @Test
    void getNextPosition_WithNoMovement_ShouldReturnCurrentPosition() {
        Vector2D nextPos = car.getNextPosition();
        assertEquals(startPosition.getX(), nextPos.getX());
        assertEquals(startPosition.getY(), nextPos.getY());
    }

    @Test
    void move_ValidMove_ShouldUpdatePositionAndSpeed() {
        Vector2D delta = new Vector2D(1, 1);
        car.move(delta);

        // Check speed updated
        assertEquals(1, car.getSpeed().getX());
        assertEquals(1, car.getSpeed().getY());

        // Check position updated (position = startPos + speed)
        assertEquals(6, car.getPosition().getX());
        assertEquals(6, car.getPosition().getY());
    }

    @Test
    void move_MultipleValidMoves_ShouldAccumulateInertia() {
        car.move(new Vector2D(1, 0));  // First move right
        car.move(new Vector2D(1, 0));  // Second move right

        // Speed should accumulate
        assertEquals(2, car.getSpeed().getX());
        assertEquals(0, car.getSpeed().getY());

        // Position should reflect total movement
        assertEquals(8, car.getPosition().getX());
        assertEquals(5, car.getPosition().getY());
    }

    @Test
    void move_InvalidMove_ShouldThrowException() {
        Vector2D invalidDelta = new Vector2D(2, 0);  // Too large movement
        assertThrows(RuntimeException.class, () -> car.move(invalidDelta));
    }

    @Test
    void getNextPosition_AfterMovement_ShouldReflectInertia() {
        car.move(new Vector2D(1, 1));
        Vector2D nextPos = car.getNextPosition();

        // Next position should be current position + current speed
        assertEquals(7, nextPos.getX());
        assertEquals(7, nextPos.getY());
    }
}