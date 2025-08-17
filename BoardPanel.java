
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(50, 50, 100, 75);
    }
}
