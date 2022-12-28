import board.Board;
import board.Direction;
import evolution.RobotInstruction;
import lombok.SneakyThrows;
import org.spiderland.Psh.Interpreter;
import org.spiderland.Psh.Program;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Renderer extends JFrame {
    private static final int WIDTH = 15;
    private static final int HEIGHT = 7;
    private static final int D_W = WIDTH * 100;
    private static final int D_H = HEIGHT * 100;
    private final DrawPanel drawPanel = new DrawPanel(D_W, D_H);

    @SneakyThrows
    public Renderer() {
        add(drawPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        final Board board = new Board(WIDTH, HEIGHT);
        final Program program;
        try {
            program = new Program("(exec.y (robot.moveup robot.moveleft))");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final AtomicInteger distance = new AtomicInteger(0);
        Interpreter interpreter = new Interpreter();
        var left = new RobotInstruction(() -> board.move(Direction.LEFT), distance);
        var up = new RobotInstruction(() -> board.move(Direction.UP), distance);
        var right = new RobotInstruction(() -> board.move(Direction.RIGHT), distance);
        var down = new RobotInstruction(() -> board.move(Direction.DOWN), distance);
        interpreter.AddInstruction("robot.moveleft", left);
        interpreter.AddInstruction("robot.moveup", up);
        interpreter.AddInstruction("robot.moveright", right);
        interpreter.AddInstruction("robot.movedown", down);
        interpreter.SetRandomParameters(-10, 10, 1, -10, 10, 0.01f, 40, 100);
        interpreter.LoadProgram(program);
        while (interpreter.execStack().size() > 0) {
            drawPanel.setBoard(board);
            drawPanel.repaint();
            try {
                final Object instruction = interpreter.execStack().pop();
                interpreter.ExecuteInstruction(instruction);
                Thread.sleep(500);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void render(final Board board) {
        drawPanel.setBoard(board);
        drawPanel.repaint();
    }

    @SneakyThrows
    public static void main(String[] args) {
        EventQueue.invokeLater(Renderer::new);
    }
}
