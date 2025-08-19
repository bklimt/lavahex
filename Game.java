
public class Game {
    private final Board board = new Board();

    public Game() {
        setRed(0, Board.COLUMNS / 2 - 1);
        setBlue(Board.ROWS - 1, Board.COLUMNS / 2 - 1);
    }

    public Board getBoard() {
        return board;
    }

    public TileView getTileView(int row, int column) {
        return board.getTileView(row, column);
    }

    public void setRed(int row, int column) {
        board.setState(row, column, Tile.State.RED);
        for (TileView neighbor : board.getNeighbors(row, column)) {
            if (neighbor.getState() == Tile.State.NONE) {
                board.setState(neighbor.getRow(), neighbor.getColumn(), Tile.State.LIGHT_RED);
            }
            if (neighbor.getState() == Tile.State.LIGHT_BLUE) {
                board.setState(neighbor.getRow(), neighbor.getColumn(), Tile.State.GRAY);
            }
        }
    }

    public void setBlue(int row, int column) {
        board.setState(row, column, Tile.State.BLUE);
        for (TileView neighbor : board.getNeighbors(row, column)) {
            if (neighbor.getState() == Tile.State.NONE) {
                board.setState(neighbor.getRow(), neighbor.getColumn(), Tile.State.LIGHT_BLUE);
            }
            if (neighbor.getState() == Tile.State.LIGHT_RED) {
                board.setState(neighbor.getRow(), neighbor.getColumn(), Tile.State.GRAY);
            }
        }
    }
}
