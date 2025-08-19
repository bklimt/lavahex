import java.util.ArrayList;

public class Board {
    public static final int ROWS = 19;
    public static final int COLUMNS = 10;

    public interface Listener {
        void onTileUpdated(int row, int column, TileView.State state);
    }

    private ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
    private Listener listener = null;

    public Board() {
        for (int row = 0; row < ROWS; row++) {
            int columns = COLUMNS;
            if (isShortRow(row)) {
                columns--;
            }
            ArrayList<Tile> array = new ArrayList<>();
            for (int column = 0; column < columns; column++) {
                Tile tile = new Tile(row, column);
                array.add(tile);
            }
            tiles.add(array);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static boolean isShortRow(int row) {
        return row % 2 == 0;
    }

    private Tile getTile(int row, int column) {
        return tiles.get(row).get(column);
    }

    public TileView getTileView(int row, int column) {
        return tiles.get(row).get(column);
    }

    private static boolean isValid(int row, int column) {
        if (row < 0 || column < 0) {
            return false;
        }
        if (row >= ROWS) {
            return false;
        }
        if (!Board.isShortRow(row)) {
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

    public static boolean isNeighbor(int row1, int column1, int row2, int column2) {
        if (!Board.isValid(row1, column1)) {
            return false;
        }
        if (!Board.isValid(row2, column2)) {
            return false;
        }
        if (column1 == column2) {
            if (row2 == row1 - 2) {
                return true;
            }
            if (row2 == row1 + 2) {
                return true;
            }
            if (row2 == row1 - 1) {
                return true;
            }
            if (row2 == row1 + 1) {
                return true;
            }
        }
        if (!isShortRow(row1)) {
            if (row2 == row1 - 1 && column2 == column1 - 1) {
                return true;
            }
            if (row2 == row1 + 1 && column2 == column1 - 1) {
                return true;
            }
        } else {
            if (row2 == row1 - 1 && column2 == column1 + 1) {
                return true;
            }
            if (row2 == row1 + 1 && column2 == column1 + 1) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<TileView> getNeighbors(int row, int column) {
        ArrayList<TileView> neighbors = new ArrayList<>();
        if (isValid(row - 2, column)) {
            neighbors.add(getTile(row - 2, column));
        }
        if (isValid(row + 2, column)) {
            neighbors.add(getTile(row + 2, column));
        }
        if (isValid(row - 1, column)) {
            neighbors.add(getTile(row - 1, column));
        }
        if (isValid(row + 1, column)) {
            neighbors.add(getTile(row + 1, column));
        }
        if (!isShortRow(row)) {
            if (isValid(row - 1, column - 1)) {
                neighbors.add(getTile(row - 1, column - 1));
            }
            if (isValid(row + 1, column - 1)) {
                neighbors.add(getTile(row + 1, column - 1));
            }
        } else {
            if (isValid(row - 1, column + 1)) {
                neighbors.add(getTile(row - 1, column + 1));
            }
            if (isValid(row + 1, column + 1)) {
                neighbors.add(getTile(row + 1, column + 1));
            }
        }
        return neighbors;
    }

    public void setState(int row, int column, TileView.State state) {
        getTile(row, column).state = state;
        if (listener != null) {
            listener.onTileUpdated(row, column, state);
        }
    }
}
