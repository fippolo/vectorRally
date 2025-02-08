package test;

import org.example.vetorrally.model.TrackElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class Test_TrackElement {

    @Test
    @DisplayName("Each TrackElement should have the correct symbol")
    void testSymbols() {
        assertEquals('#', TrackElement.BOUNDARY.getSymbol());
        assertEquals('S', TrackElement.START.getSymbol());
        assertEquals('F', TrackElement.FINISH.getSymbol());
        assertEquals(' ', TrackElement.TRACK.getSymbol());
        assertEquals('O', TrackElement.OCCUPIED.getSymbol());
        assertEquals('N', TrackElement.NEXTPROJECTION.getSymbol());
        assertEquals('P', TrackElement.PLAYER.getSymbol());
    }

    @Test
    @DisplayName("fromChar should return correct TrackElement for valid symbols")
    void testFromCharValid() {
        assertEquals(TrackElement.BOUNDARY, TrackElement.fromChar('#'));
        assertEquals(TrackElement.START, TrackElement.fromChar('S'));
        assertEquals(TrackElement.FINISH, TrackElement.fromChar('F'));
        assertEquals(TrackElement.TRACK, TrackElement.fromChar(' '));
        assertEquals(TrackElement.OCCUPIED, TrackElement.fromChar('O'));
        assertEquals(TrackElement.NEXTPROJECTION, TrackElement.fromChar('N'));
        assertEquals(TrackElement.PLAYER, TrackElement.fromChar('P'));
    }

    @Test
    @DisplayName("fromChar should return BOUNDARY for invalid symbols")
    void testFromCharInvalid() {
        assertEquals(TrackElement.BOUNDARY, TrackElement.fromChar('X'));
        assertEquals(TrackElement.BOUNDARY, TrackElement.fromChar('?'));
        assertEquals(TrackElement.BOUNDARY, TrackElement.fromChar('0'));
    }

    @Test
    @DisplayName("Each TrackElement should be unique")
    void testUniqueSymbols() {
        // Create array of all symbols
        char[] symbols = {
                TrackElement.BOUNDARY.getSymbol(),
                TrackElement.START.getSymbol(),
                TrackElement.FINISH.getSymbol(),
                TrackElement.TRACK.getSymbol(),
                TrackElement.OCCUPIED.getSymbol(),
                TrackElement.NEXTPROJECTION.getSymbol(),
                TrackElement.PLAYER.getSymbol()
        };

        // Check each symbol against every other symbol
        for (int i = 0; i < symbols.length; i++) {
            for (int j = i + 1; j < symbols.length; j++) {
                assertNotEquals(symbols[i], symbols[j],
                        "Symbols should be unique: " + symbols[i] + " vs " + symbols[j]);
            }
        }
    }

    @Test
    @DisplayName("All TrackElements should have non-null symbols")
    void testNonNullSymbols() {
        for (TrackElement element : TrackElement.values()) {
            assertNotNull(element.getSymbol(),
                    "Symbol for " + element.name() + " should not be null");
        }
    }

    @Test
    @DisplayName("Every TrackElement should be convertible back to itself through fromChar")
    void testSymbolRoundTrip() {
        for (TrackElement element : TrackElement.values()) {
            char symbol = element.getSymbol();
            TrackElement converted = TrackElement.fromChar(symbol);
            assertEquals(element, converted,
                    "Converting " + element.name() + " through its symbol should return the same element");
        }
    }
}