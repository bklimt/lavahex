
public class Tile {
    enum HoverState {
        NONE,
        HOVER,
        PARTIAL;
    }

    enum State {
        NONE,
        BLUE,
        LIGHT_BLUE,
        RED,
        LIGHT_RED,
        GRAY,
    }

    public HoverState hover = HoverState.NONE;
    public State state = State.NONE;
}
