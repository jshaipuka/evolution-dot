import board.Board;
import board.Direction;
import evolution.RobotInstruction;
import org.spiderland.Psh.Interpreter;
import org.spiderland.Psh.Program;

import javax.swing.JFrame;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class RectanglesDrawingExample extends JFrame {

    public RectanglesDrawingExample(final Board board) {
        final DrawPanel drawPanel = new DrawPanel(board);
        add(drawPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
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
        final Program program = new Program(args[0]);
        final AtomicInteger distance = new AtomicInteger(0);
        final Interpreter interpreter = new Interpreter();
        interpreter.setRandomParameters(-10, 10, 1, -10, 10, 0.01f, 40, 100);
        EventQueue.invokeLater(() -> {
            final RectanglesDrawingExample panel = new RectanglesDrawingExample(board);
            final Thread thread = new Thread(() -> {
                Stream.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
                    .map(it -> new RobotInstruction("robot.move" + it.name().toLowerCase(), createCallback(board, panel, it), distance))
                    .forEach(it -> interpreter.addInstruction(it.getName(), it));
                while (true) {
                    interpreter.execute(program);
                }
            });
            thread.start();
        });
    }
}
