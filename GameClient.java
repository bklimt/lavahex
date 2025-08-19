import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.SwingUtilities;

public class GameClient {
    private final Board board = new Board();;
    private final BlockingQueue<ClientMessage> queue = new ArrayBlockingQueue<ClientMessage>(100);

    private Listener listener = null;
    private Thread clientThread = null;
    private Thread serverThread = null;

    public static interface Listener {
        void onError(Throwable error, boolean fatal);

        void onBoardUpdate();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    // You must call setListener before start.
    public void start() {
        try {
            // TODO: Figure out when to close this.
            Socket socket = new Socket("localhost", 9876);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            clientThread = new Thread(() -> processClientMessages(outputStream), "Client");
            serverThread = new Thread(() -> processServerMessages(inputStream), "Server");

            clientThread.start();
            serverThread.start();

            sync();
        } catch (Exception e) {
            // TODO: Figure out how to stop the threads.
            SwingUtilities.invokeLater(() -> listener.onError(e, false));
        }
    }

    private void processClientMessages(ObjectOutputStream outputStream) {
        while (true) {
            try {
                ClientMessage message = queue.take();
                outputStream.writeObject(message);

            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> listener.onError(e, true));
                return;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processServerMessages(ObjectInputStream inputStream) {
        while (true) {
            try {
                Object message = inputStream.readObject();
                if (!(message instanceof ServerMessage)) {
                    throw new RuntimeException("invalid server message type");
                }
                ServerMessage serverMessage = (ServerMessage) message;
                if (serverMessage.tileUpdate != null) {
                    ServerMessage.TileUpdate update = serverMessage.tileUpdate;
                    SwingUtilities.invokeLater(() -> {
                        board.setState(update.row, update.column, update.state);
                        listener.onBoardUpdate();
                    });
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> listener.onError(e, true));
                return;
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void sync() {
        ClientMessage message = new ClientMessage(new ClientMessage.SyncRequest());
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setRed(int row, int column) {
        ClientMessage message = new ClientMessage(new ClientMessage.SetRequest(row, column, true));
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setBlue(int row, int column) {
        ClientMessage message = new ClientMessage(new ClientMessage.SetRequest(row, column, false));
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
