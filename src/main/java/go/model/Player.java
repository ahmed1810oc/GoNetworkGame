package go.model;

/**
 * Represents a player in the game.
 */
public class Player {

    private String name;
    private Stone stone;

    public Player(String name, Stone stone) {
        this.name = name;
        this.stone = stone;
    }

    public String getName() {
        return name;
    }

    public Stone getStone() {
        return stone;
    }

    @Override
    public String toString() {
        return name + " (" + stone + ")";
    }
}