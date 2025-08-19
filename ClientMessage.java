import java.io.Serializable;

/**
 * A message sent over the network.
 */
public class ClientMessage implements Serializable {
    ClientMessage(JoinRequest joinRequest) {
        this.joinRequest = joinRequest;
    }

    ClientMessage(SetRequest setRequest) {
        this.setRequest = setRequest;
    }

    public static class JoinRequest implements Serializable {
        public JoinRequest() {
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

    public JoinRequest joinRequest = null;
    public SetRequest setRequest = null;
}
