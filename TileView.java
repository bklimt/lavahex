
public interface TileView {
    enum State {
        NONE,
        BLUE,
        LIGHT_BLUE,
        RED,
        LIGHT_RED,
        GRAY,
    }

    public State getState();
}
