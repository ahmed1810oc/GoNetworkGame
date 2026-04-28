package go.model;

/**
 * Represents one move made by a player.
 */
public class Move {

    private int row;
    private int col;
    private Stone stone;

    public Move(int row, int col, Stone stone) {
        this.row = row;
        this.col = col;
        this.stone = stone;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Stone getStone() {
        return stone;
    }

    @Override
    public String toString() {
        return "Move{" +
                "row=" + row +
                ", col=" + col +
                ", stone=" + stone +
                '}';
    }
}