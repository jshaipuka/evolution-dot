import board.Board;
import items.Food;
import items.Item;
import items.Robot;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    private int width;
    private int height;
    private Board board;

    public DrawPanel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                final Item item = board.getCells()[j][i];
                if (item instanceof Food) {
                    g.setColor(Color.BLUE);
                    g.fillRect(i * 100, j * 100, 100, 100);
                } else if (item instanceof Robot) {
                    g.setColor(Color.RED);
                    g.fillRect(i * 100, j * 100, 100, 100);
                }
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}