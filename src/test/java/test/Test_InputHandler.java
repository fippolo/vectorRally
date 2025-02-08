package test;

import org.example.vetorrally.controller.InputHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class Test_InputHandler {
    private final InputStream systemIn = System.in;

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    @DisplayName("Should get valid integer input")
    void testGetIntInput() {
        provideInput("42\n");
        InputHandler handler = new InputHandler();

        int result = handler.getIntInput("Enter a number: ");
        assertEquals(42, result);

        System.setIn(systemIn);
        handler.cleanup();
    }

    @Test
    @DisplayName("Should get valid string input")
    void testGetStringInput() {
        provideInput("test string\n");
        InputHandler handler = new InputHandler();

        String result = handler.getStringInput("Enter a string: ");
        assertEquals("test string", result);

        System.setIn(systemIn);
        handler.cleanup();
    }

    @Test
    @DisplayName("Should handle invalid integer input")
    void testInvalidIntInput() {
        provideInput("not a number\n42\n");
        InputHandler handler = new InputHandler();

        int result = handler.getIntInput("Enter a number: ");
        assertEquals(42, result);

        System.setIn(systemIn);
        handler.cleanup();
    }

    @Test
    @DisplayName("Should handle multiple integer inputs")
    void testMultipleIntInputs() {
        provideInput("1\n2\n");
        InputHandler handler = new InputHandler();

        int first = handler.getIntInput("Enter first number: ");
        int second = handler.getIntInput("Enter second number: ");

        assertEquals(1, first);
        assertEquals(2, second);

        System.setIn(systemIn);
        handler.cleanup();
    }

    @Test
    @DisplayName("Should handle multiple string inputs")
    void testMultipleStringInputs() {
        provideInput("first string\nsecond string\n");
        InputHandler handler = new InputHandler();

        String first = handler.getStringInput("Enter first string: ");
        String second = handler.getStringInput("Enter second string: ");

        assertEquals("first string", first);
        assertEquals("second string", second);

        System.setIn(systemIn);
        handler.cleanup();
    }

    @Test
    @DisplayName("Should handle mixed string and integer inputs")
    void testMixedInputs() {
        provideInput("test string\n42\n");
        InputHandler handler = new InputHandler();

        String str = handler.getStringInput("Enter a string: ");
        int num = handler.getIntInput("Enter a number: ");

        assertEquals("test string", str);
        assertEquals(42, num);

        System.setIn(systemIn);
        handler.cleanup();
    }
}