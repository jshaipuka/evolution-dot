import board.Board;
import items.Food;
import items.Item;
import items.Robot;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import static java.util.stream.IntStream.range;

public class DrawPanel extends JPanel {

    private static final int CELL_SIZE_IN_PIXELS = 20;

    private final Board board;

    public DrawPanel(final Board board) {
        this.board = board;
    }

    public Dimension getPreferredSize() {
        return new Dimension(board.getWidth() * CELL_SIZE_IN_PIXELS, board.getHeight() * CELL_SIZE_IN_PIXELS);
    }

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        range(0, board.getHeight()).forEach(i -> range(0, board.getWidth()).forEach(j -> fillCell(g, i, j)));
    }

    private void fillCell(final Graphics g, final int i, final int j) {
        final Item cell = board.getCells()[i][j];
        if (cell instanceof Food) {
            g.setColor(Color.BLUE);
        } else if (cell instanceof Robot) {
            g.setColor(board.isAlive() ? Color.RED : Color.BLACK);
        } else {
            g.setColor(Color.GREEN);
        }
        g.fillRect(j * CELL_SIZE_IN_PIXELS, i * CELL_SIZE_IN_PIXELS, CELL_SIZE_IN_PIXELS, CELL_SIZE_IN_PIXELS);
    }
}
