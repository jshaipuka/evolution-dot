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
                    g.fillRect(j * 10, i * 10, 10, 10);
                }
            }
        }

        public Dimension getPreferredSize() {
            return new Dimension(board.getWidth() * 10, board.getHeight() * 10);
        }
    }

    public static void main(String[] args) {
        final Board board = new Board(150, 70);
        final Program program;
        try {
            program = new Program("(exec.y (robot.moveup robot.moveleft))");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final AtomicInteger distance = new AtomicInteger(0);
        final Interpreter interpreter = new Interpreter();
        interpreter.SetRandomParameters(-10, 10, 1, -10, 10, 0.01f, 40, 100);
        EventQueue.invokeLater(() -> {
            final RectanglesDrawingExample panel = new RectanglesDrawingExample(board);
            final Thread thread = new Thread(() -> {
                while (true) {
                    var left = new RobotInstruction(() -> {
                        board.move(Direction.LEFT);
                        panel.repaint();
                    }, distance);
                    var up = new RobotInstruction(() -> {
                        board.move(Direction.UP);
                        panel.repaint();
                    }, distance);
                    var right = new RobotInstruction(() -> {
                        board.move(Direction.RIGHT);
                        panel.repaint();
                    }, distance);
                    var down = new RobotInstruction(() -> {
                        board.move(Direction.DOWN);
                        panel.repaint();
                    }, distance);
                    interpreter.AddInstruction("robot.moveleft", left);
                    interpreter.AddInstruction("robot.moveup", up);
                    interpreter.AddInstruction("robot.moveright", right);
                    interpreter.AddInstruction("robot.movedown", down);
                    interpreter.Execute(program);
                }
            });
            thread.start();
        });
    }
}
