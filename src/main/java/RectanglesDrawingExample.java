import board.Board;
import board.Direction;
import evolution.RobotInstruction;
import items.Food;
import items.Item;
import items.Robot;
import org.spiderland.Psh.Interpreter;
import org.spiderland.Psh.Program;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
                    g.fillRect(j * 20, i * 20, 20, 20);
                }
            }
        }

        public Dimension getPreferredSize() {
            return new Dimension(board.getWidth() * 20, board.getHeight() * 20);
        }
    }

    private static Runnable createCallback(final Board board, final Component component, final Direction direction) {
        return () -> {
            board.move(direction);
            component.repaint();
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static void main(final String[] args) {
        final Board board = new Board(75, 35);
        final Program program;
        try {
            program = new Program(args[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final AtomicInteger distance = new AtomicInteger(0);
        final Interpreter interpreter = new Interpreter();
        interpreter.SetRandomParameters(-10, 10, 1, -10, 10, 0.01f, 40, 100);
        EventQueue.invokeLater(() -> {
            final RectanglesDrawingExample panel = new RectanglesDrawingExample(board);
            final Thread thread = new Thread(() -> {
                Stream.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
                        .map(it -> new RobotInstruction("robot.move" + it.name().toLowerCase(), createCallback(board, panel, it), distance))
                        .forEach(it -> interpreter.AddInstruction(it.getName(), it));
                while (true) {
                    interpreter.Execute(program);
                }
            });
            thread.start();
        });
    }
}
