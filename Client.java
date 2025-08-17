
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

            frame.setSize(1024, 768);
            frame.setVisible(true);
        });
    }
}
