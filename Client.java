
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Client {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Lava Hex");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setLayout(new BorderLayout());

            BoardPanel board = new BoardPanel();
            frame.add(board, BorderLayout.CENTER);

            int width = Game.COLUMNS * BoardPanel.TILE_WIDTH
                + (Game.COLUMNS - 1) * BoardPanel.TILE_WIDTH / 2
                + (BoardPanel.START_X - BoardPanel.TILE_WIDTH / 2) * 2;
            int height = ((Game.ROWS + 1) / 2) * BoardPanel.TILE_HEIGHT
                + (BoardPanel.START_Y - BoardPanel.TILE_HEIGHT / 2) * 2;

            height += 40; // A guess at the window title bar size.

            frame.setSize(width, height);

            frame.setVisible(true);
        });
    }
}
