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

public class RobotVisualizer extends JFrame {

    public RobotVisualizer(final Board board) {
        final var drawPanel = new DrawPanel(board);
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
        final var board = new Board(75, 35);
        final var program = new Program(args[0]);
        final var distance = new AtomicInteger(0);
        final var interpreter = new Interpreter();
        interpreter.setRandomParameters(-10, 10, 1, -10, 10, 0.01f, 40, 100);
        EventQueue.invokeLater(() -> {
            final var panel = new RobotVisualizer(board);
            final Thread thread = new Thread(() -> {
                Stream.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
                    .map(it -> new RobotInstruction("robot.move" + it.name().toLowerCase(), createCallback(board, panel, it), distance))
                    .forEach(it -> interpreter.addInstruction(it.getName(), it));
                interpreter.execute(program);
                panel.repaint();
            });
            thread.start();
        });
    }
}
