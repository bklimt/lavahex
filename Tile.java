
public class Tile implements TileView {
    public State state = State.NONE;

    @Override
    public TileView.State getState() {
        return state;
    }
}
