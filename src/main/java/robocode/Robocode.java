package robocode;

import board.Board;
import board.Direction;
import org.spiderland.Psh.Interpreter;
import org.spiderland.Psh.Program;

import java.util.concurrent.atomic.AtomicInteger;

public class Robocode {
    private AtomicInteger counter = new AtomicInteger(0);
    private Interpreter interpreter;
    private Program program;
    public Robocode(Board board, String robotInstructions) throws Exception {
        this.interpreter = new Interpreter();

        var left = new RobotInstruction(() -> board.move(Direction.LEFT), counter);
        var up = new RobotInstruction(() -> board.move(Direction.UP), counter);
        var right = new RobotInstruction(() -> board.move(Direction.RIGHT), counter);
        var down = new RobotInstruction(() -> board.move(Direction.DOWN), counter);

        interpreter.AddInstruction("robot.moveleft", left);
        interpreter.AddInstruction("robot.moveup", up);
        interpreter.AddInstruction("robot.moveright", right);
        interpreter.AddInstruction("robot.movedown", down);

        program = new Program(interpreter,robotInstructions);
    }

    public int run(){
        interpreter.Execute(program);
        return counter.get();
    }
}
