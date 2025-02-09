package test;

import org.example.vetorrally.model.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class Test_Vector2D {
    private Vector2D vector;

    @BeforeEach
    void setUp() {
        vector = new Vector2D(3, 4);
    }

    @Test
    @DisplayName("Constructor should initialize with correct values")
    void testConstructor() {
        assertEquals(3, vector.getX());
        assertEquals(4, vector.getY());
    }

    @Test
    @DisplayName("Setters should update values correctly")
    void testSetters() {
        vector.setX(5);
        vector.setY(6);
        assertEquals(5, vector.getX());
        assertEquals(6, vector.getY());
    }

    @Test
    @DisplayName("Adding vectors should work correctly")
    void testAdd() {
        Vector2D other = new Vector2D(2, 3);
        vector.add(other);
        assertEquals(5, vector.getX());
        assertEquals(7, vector.getY());
    }

    @Test
    @DisplayName("Adding zero vector should not change original vector")
    void testAddZeroVector() {
        Vector2D zero = new Vector2D(0, 0);
        int originalX = vector.getX();
        int originalY = vector.getY();

        vector.add(zero);
        assertEquals(originalX, vector.getX());
        assertEquals(originalY, vector.getY());
    }

    @Test
    @DisplayName("Subtracting vectors should work correctly")
    void testSubtract() {
        Vector2D other = new Vector2D(1, 1);
        vector.subtract(other);
        assertEquals(2, vector.getX());
        assertEquals(3, vector.getY());
    }

    @Test
    @DisplayName("Subtracting same vector should result in zero vector")
    void testSubtractSameVector() {
        Vector2D same = new Vector2D(vector.getX(), vector.getY());
        vector.subtract(same);
        assertEquals(0, vector.getX());
        assertEquals(0, vector.getY());
    }

    @Test
    @DisplayName("Multiplication by scalar should work correctly")
    void testMultiply() {
        vector.multiply(2);
        assertEquals(6, vector.getX());
        assertEquals(8, vector.getY());
    }

    @Test
    @DisplayName("Multiplication by zero should result in zero vector")
    void testMultiplyByZero() {
        vector.multiply(0);
        assertEquals(0, vector.getX());
        assertEquals(0, vector.getY());
    }

    @Test
    @DisplayName("Division by scalar should work correctly")
    void testDivide() {
        vector = new Vector2D(6, 8);  // Use even numbers for clean division
        vector.divide(2);
        assertEquals(3, vector.getX());
        assertEquals(4, vector.getY());
    }

    @Test
    @DisplayName("Division by zero should not modify the vector")
    void testDivideByZero() {
        int originalX = vector.getX();
        int originalY = vector.getY();

        vector.divide(0);
        assertEquals(originalX, vector.getX());
        assertEquals(originalY, vector.getY());
    }

    @Test
    @DisplayName("toString should return correct string representation")
    void testToString() {
        String expected = "Vector2D{x=3, y=4}";
        assertEquals(expected, vector.toString());
    }

    @Test
    @DisplayName("Vector operations with negative values should work correctly")
    void testNegativeValues() {
        Vector2D negative = new Vector2D(-1, -2);
        vector.add(negative);
        assertEquals(2, vector.getX());
        assertEquals(2, vector.getY());

        vector.multiply(-1);
        assertEquals(-2, vector.getX());
        assertEquals(-2, vector.getY());
    }

    @Test
    @DisplayName("Sequence of operations should work correctly")
    void testOperationSequence() {
        // Perform a sequence of operations
        vector.add(new Vector2D(1, 1));      // (4, 5)
        vector.multiply(2);                   // (8, 10)
        vector.subtract(new Vector2D(3, 5));  // (5, 5)
        vector.divide(5);                     // (1, 1)

        assertEquals(1, vector.getX());
        assertEquals(1, vector.getY());
    }
}