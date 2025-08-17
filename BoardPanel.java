
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
    public static final int TILE_WIDTH = 60;
    public static final int TILE_HEIGHT = (int) (Math.sin(Math.PI / 3.0) * TILE_WIDTH);
    public static final int START_X = TILE_WIDTH;
    public static final int START_Y = 60;

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

        public TileView getTile() {
            return game.getTileView(row, column);
        }
    }

    private int hoverRow = -1;
    private int hoverColumn = -1;
    Game game = new Game();
    private ArrayList<Hexagon> hexagons = new ArrayList<>();

    BoardPanel() {
        createHexagons();
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (Hexagon hexagon : hexagons) {
                    if (hexagon.contains(e.getPoint())) {
                        setHover(hexagon.getRow(), hexagon.getColumn());
                        BoardPanel.this.repaint();
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                for (Hexagon hexagon : hexagons) {
                    if (hexagon.contains(e.getPoint())) {
                        setHover(hexagon.getRow(), hexagon.getColumn());
                        BoardPanel.this.repaint();
                    }
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                for (Hexagon hexagon : hexagons) {
                    if (hexagon.contains(e.getPoint())) {
                        TileView tile = hexagon.getTile();
                        if (tile.getState() == Tile.State.LIGHT_RED) {
                            game.setRed(hexagon.getRow(), hexagon.getColumn());
                        }
                        if (tile.getState() == Tile.State.LIGHT_BLUE) {
                            game.setBlue(hexagon.getRow(), hexagon.getColumn());
                        }
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

    private void setHover(int row, int column) {
        hoverRow = row;
        hoverColumn = column;
    }

    private boolean isHover(int row, int column) {
        return row == hoverRow && column == hoverColumn;
    }

    private boolean isPartialHover(int row, int column) {
        return Game.isNeighbor(row, column, hoverRow, hoverColumn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Hexagon hexagon : hexagons) {
            Color color = Color.BLACK;
            TileView tile = hexagon.getTile();
            switch (tile.getState()) {
                case Tile.State.NONE:
                    color = new Color(1.0f, 0.8f, 0.0f);
                    break;
                case Tile.State.RED:
                    color = Color.RED;
                    break;
                case Tile.State.BLUE:
                    color = Color.BLUE;
                    break;
                case Tile.State.LIGHT_RED:
                    color = new Color(1.0f, 0.4f, 0.4f);
                    break;
                case Tile.State.LIGHT_BLUE:
                    color = new Color(0.4f, 0.4f, 1.0f);
                    break;
                case Tile.State.GRAY:
                    color = new Color(0.5f, 0.5f, 0.5f);
                    break;
            }
            g.setColor(color);
            g.fillPolygon(hexagon);

            if (isHover(hexagon.getRow(), hexagon.getColumn())) {
                g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
                g.fillPolygon(hexagon);
            } else if (isPartialHover(hexagon.getRow(), hexagon.getColumn())) {
                g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
                g.fillPolygon(hexagon);
            }

            g.setColor(Color.BLACK);
            g.drawPolygon(hexagon);
            // g.drawString(String.format("(%d, %d)", hexagon.getRow(), hexagon.getColumn()), x, y);
        }
    }
}
