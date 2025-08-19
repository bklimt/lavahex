import java.io.Serializable;

public class ServerMessage implements Serializable {
    public static class JoinResponse implements Serializable {
        boolean red = false;
    }

    public static class TileUpdate implements Serializable {
        int row;
        int column;
        TileView.State state;

        public TileUpdate(int row, int column, TileView.State state) {
            this.row = row;
            this.column = column;
            this.state = state;
        }
    }

    public JoinResponse joinResponse = null;
    public TileUpdate tileUpdate = null;

    public ServerMessage(JoinResponse joinResponse) {
        this.joinResponse = joinResponse;
    }

    public ServerMessage(TileUpdate tileUpdate) {
        this.tileUpdate = tileUpdate;
    }
}
