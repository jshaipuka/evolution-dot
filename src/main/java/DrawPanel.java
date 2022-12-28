import board.Board;
import items.Food;
import items.Item;
import items.Robot;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    private static final int CELL_SIZE_IN_PIXELS = 20;

    private final Board board;

    public DrawPanel(Board board) {
        this.board = board;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
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
    }

    public Dimension getPreferredSize() {
        return new Dimension(board.getWidth() * CELL_SIZE_IN_PIXELS, board.getHeight() * CELL_SIZE_IN_PIXELS);
    }
}
