import java.io.Serializable;

/**
 * A message sent over the network.
 */
public class ClientMessage implements Serializable {
    ClientMessage(SyncRequest syncRequest) {
        this.syncRequest = syncRequest;
    }

    ClientMessage(SetRequest setRequest) {
        this.setRequest = setRequest;
    }

    public static class SyncRequest implements Serializable {
        public SyncRequest() {
        }
    }

    public static class SetRequest implements Serializable {
        int row;
        int column;
        boolean red;

        public SetRequest(int row, int column, boolean red) {
            this.row = row;
            this.column = column;
            this.red = red;
        }
    }

    public SyncRequest syncRequest = null;
    public SetRequest setRequest = null;
}
