
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
    public static final int TILE_WIDTH = 100;
    public static final int TILE_HEIGHT = (int) (Math.sin(Math.PI / 3.0) * TILE_WIDTH);
    public static final int START_X = TILE_WIDTH;
    public static final int START_Y = 100;

    Game game = new Game();

    private class Hexagon extends Polygon {
        private final int row;
        private final int column;

        public Hexagon(int row, int column, int centerX, int centerY) {
            super();

            this.row = row;
            this.column = column;

            addPoint(centerX + TILE_WIDTH / 2, centerY);
            addPoint(centerX + TILE_WIDTH / 4, centerY - TILE_HEIGHT / 2);
            addPoint(centerX - TILE_WIDTH / 4, centerY - TILE_HEIGHT / 2);
            addPoint(centerX - TILE_WIDTH / 2, centerY);
            addPoint(centerX - TILE_WIDTH / 4, centerY + TILE_HEIGHT / 2);
            addPoint(centerX + TILE_WIDTH / 4, centerY + TILE_HEIGHT / 2);
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public Game.Tile getTile() {
            return game.getTile(row, column);
        }
    }

    private ArrayList<Hexagon> hexagons = new ArrayList<>();

    BoardPanel() {
        createHexagons();
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (Hexagon hexagon : hexagons) {
                    if (hexagon.contains(e.getPoint())) {
                        game.setHover(hexagon.getRow(), hexagon.getColumn());
                        BoardPanel.this.repaint();
                    }
                }
            }
        });
    }

    private void createHexagons() {
        int x = START_X;
        int y = START_Y;
        for (int row = 0; row < Game.ROWS; row++) {
            int columns = Game.COLUMNS;
            if (game.isShortRow(row)) {
                x += (TILE_WIDTH - TILE_WIDTH / 4);
                columns--;
            }
            for (int column = 0; column < columns; column++) {
                hexagons.add(new Hexagon(row, column, x, y));
                x += (TILE_WIDTH + TILE_WIDTH / 2);
            }
            y += TILE_HEIGHT / 2;
            x = START_X;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Hexagon hexagon : hexagons) {
            /*
            if (hexagon.getRow() % 2 != 0) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.GREEN);
            }
            */
            g.setColor(new Color(1.0f, 0.8f, 0.0f));
            g.fillPolygon(hexagon);

            if (hexagon.getTile().hover == Game.Tile.HoverState.HOVER) {
                g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
                g.fillPolygon(hexagon);
            }
            if (hexagon.getTile().hover == Game.Tile.HoverState.PARTIAL) {
                g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
                g.fillPolygon(hexagon);
            }

            g.setColor(Color.BLACK);
            g.drawPolygon(hexagon);
            // g.drawString(String.format("(%d, %d)", hexagon.getRow(), hexagon.getColumn()), x, y);
        }
    }
}
