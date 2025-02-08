package org.example.vetorrally.model;

/**
 * enumerator to represent the value of each track position
 */
public enum TrackElement {
    BOUNDARY('#'),   // Out-of-bound points
    START('S'),      // Start line
    FINISH('F'),     // Finish line
    TRACK(' '),      // In-bound points
    OCCUPIED('O'),   // TRACK PLACE IS OCCUPIED BY ANOTHER CAR
    NEXTPROJECTION('N'),
    PLAYER('P');

    private char symbol;

    /**
     * constructor
     * @param symbol
     */
    TrackElement(char symbol) {
        this.symbol = symbol;
    }

    /**
     * get symbol of a specified enum. constant
     * @return the symbol of the specified constant
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * get the enumerator constant from a char symbol
     * @param symbol char representation of the constant
     * @return constant represented by symbol
     */
    public static TrackElement fromChar(char symbol) {
        for (TrackElement element : values()) {
            if (element.symbol == symbol) {
                return element;
            }
        }
        return BOUNDARY;
    }
}
