
package go.model;

/*
 * @author ahmed
 * Represents the content of one cell on the Go board.
 * A cell can be empty, black, or white.
 */

public enum Stone {
    EMPTY,
    BLACK,
    WHITE;

    /**
     * Returns the opposite stone color.
     * BLACK -> WHITE
     * WHITE -> BLACK
     * EMPTY -> EMPTY
     */
    public Stone opposite() {
        if (this == BLACK) {
            return WHITE;
        } else if (this == WHITE) {
            return BLACK;
        } else {
            return EMPTY;
        }
    }
}
