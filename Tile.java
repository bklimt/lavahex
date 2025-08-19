
public class Tile implements TileView {
    public State state = State.NONE;

    private int row;
    private int column;

    Tile(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public TileView.State getState() {
        return state;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }
}
