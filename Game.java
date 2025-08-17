import java.util.ArrayList;

public class Game {
    public static final int ROWS = 9;
    public static final int COLUMNS = 5;

    public static class Tile {
        enum HoverState {
            NONE,
            HOVER,
            PARTIAL;
        }

        public HoverState hover = HoverState.NONE;
    }

    private ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();

    public Game() {
        for (int row = 0; row < ROWS; row++) {
            int columns = COLUMNS;
            if (row % 2 != 0) {
                columns--;
            }
            ArrayList<Tile> array = new ArrayList<>();
            for (int column = 0; column < columns; column++) {
                Tile tile = new Tile();
                array.add(tile);
            }
            tiles.add(array);
        }
    }

    public Tile getTile(int row, int column) {
        return tiles.get(row).get(column);
    }

    private boolean isValid(int row, int column) {
        if (row < 0 || column < 0) {
            return false;
        }
        if (row >= ROWS) {
            return false;
        }
        if (row % 2 == 0) {
            if (column >= COLUMNS) {
                return false;
            }
        } else {
            if (column >= COLUMNS - 1) {
                return false;
            }
        }
        return true;
    }

    public void setHover(int row, int column) {
        for (ArrayList<Tile> array : tiles) {
            for (Tile tile : array) {
                tile.hover = Tile.HoverState.NONE;
            }
        }

        getTile(row, column).hover = Tile.HoverState.HOVER;
        if (isValid(row - 2, column)) {
            getTile(row - 2, column).hover = Tile.HoverState.PARTIAL;
        }
        if (isValid(row + 2, column)) {
            getTile(row + 2, column).hover = Tile.HoverState.PARTIAL;
        }
        if (isValid(row - 1, column)) {
            getTile(row - 1, column).hover = Tile.HoverState.PARTIAL;
        }
        if (isValid(row + 1, column)) {
            getTile(row + 1, column).hover = Tile.HoverState.PARTIAL;
        }
        if (row % 2 == 0) {
            if (isValid(row - 1, column - 1)) {
                getTile(row - 1, column - 1).hover = Tile.HoverState.PARTIAL;
            }
            if (isValid(row + 1, column - 1)) {
                getTile(row + 1, column - 1).hover = Tile.HoverState.PARTIAL;
            }
        } else {
            if (isValid(row - 1, column + 1)) {
                getTile(row - 1, column + 1).hover = Tile.HoverState.PARTIAL;
            }
            if (isValid(row + 1, column + 1)) {
                getTile(row + 1, column + 1).hover = Tile.HoverState.PARTIAL;
            }
        }
    }
}
