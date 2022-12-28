import board.Board;
import board.Direction;
import items.Food;
import items.Item;
import items.Robot;

import javax.swing.*;
import java.awt.*;

public class RectanglesDrawingExample extends JFrame {

    private final Board board;

    final DrawPanel drawPanel = new DrawPanel();

    public RectanglesDrawingExample(final Board board) {
        this.board = board;
        add(drawPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class DrawPanel extends JPanel {

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
                    g.fillRect(j * 100, i * 100, 100, 100);
                }
            }
        }

        public Dimension getPreferredSize() {
            return new Dimension(board.getWidth() * 100, board.getHeight() * 100);
        }
    }

    public static void main(String[] args) {
        final Board board = new Board(7, 15);
        EventQueue.invokeLater(() -> {
            final RectanglesDrawingExample panel = new RectanglesDrawingExample(board);
            final Thread thread = new Thread(() -> {
                while (true) {
                    panel.repaint();
                    try {
                        board.move(Direction.RIGHT);
                        Thread.sleep(100);
                    } catch (Exception ignored) {

                    }
                }
            });
            thread.start();
        });
    }
}