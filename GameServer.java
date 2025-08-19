import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GameServer {
    private static final class Client {
        ObjectOutputStream outputStream;
    }

    private final ArrayList<Client> clients = new ArrayList<>();
    private final Object clientLock = new Object();

    private void boardcastTileUpdate(int row, int column, TileView.State state) {
        System.out.println(String.format("broadcasting update of (%d, %d) to %s", row, column, state));
        ServerMessage message = new ServerMessage(new ServerMessage.TileUpdate(row, column, state));
        synchronized (clientLock) {
            for (Client client : clients) {
                try {
                    client.outputStream.writeObject(message);
                } catch (IOException e) {
                    System.err.println("unable to send message to client: " + e.getMessage());
                }
            }
        }
    }

    private void run() {
        System.out.println("Initializing game...");
        Game game = new Game();

        game.getBoard().setListener(new Board.Listener() {
            @Override
            public void onTileUpdated(int row, int column, TileView.State state) {
                boardcastTileUpdate(row, column, state);
            }
        });

        BlockingQueue<ClientMessage> inputQueue = new ArrayBlockingQueue<ClientMessage>(100);
        Thread inputThread = new Thread(() -> {
            while (true) {
                try {
                    ClientMessage message = inputQueue.take();
                    if (message.setRequest != null) {
                        int row = message.setRequest.row;
                        int column = message.setRequest.column;
                        if (message.setRequest.red) {
                            System.out.println(String.format("setting (%d, %d) to red", row, column));
                            game.setRed(row, column);
                        } else {
                            System.out.println(String.format("setting (%d, %d) to blue", row, column));
                            game.setBlue(row, column);
                        }
                    }
                    if (message.syncRequest != null) {
                        for (int row = 0; row < Board.ROWS; row++) {
                            int columns = Board.COLUMNS;
                            if (Board.isShortRow(row)) {
                                columns--;
                            }
                            for (int column = 0; column < columns; column++) {
                                TileView tile = game.getBoard().getTileView(row, column);
                                if (tile.getState() != TileView.State.NONE) {
                                    boardcastTileUpdate(row, column, tile.getState());
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Input Queue");
        inputThread.start();

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9876);
            System.out.println("listening on port 9876");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Client client = new Client();
                client.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                synchronized (clientLock) {
                    clients.add(client);
                }
                Thread clientThread = new Thread(() -> {
                    try {
                        ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                        while (true) {
                            try {
                                ClientMessage message = (ClientMessage) inputStream.readObject();
                                inputQueue.put(message);
                            } catch (Exception e) {
                                System.err.println("error reading from client: " + e.getMessage());
                                break;
                            }
                        }
                        inputStream.close();
                    } catch (IOException e) {
                        System.err.println("error opening input stream to client: " + e.getMessage());
                    }
                }, "Client Handler");
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("unable to create server: " + e.getMessage());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    // Whatever.
                }
            }
        }
    }

    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.run();
    }
}
