package evolution;

import board.Board;
import board.Direction;
import org.spiderland.Psh.GAIndividual;
import org.spiderland.Psh.GATestCase;
import org.spiderland.Psh.Interpreter;
import org.spiderland.Psh.Program;
import org.spiderland.Psh.PushGP;
import org.spiderland.Psh.PushGPIndividual;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class RobotEvolution extends PushGP {

    private static final int NUMBER_OF_BATTLES_PER_TEST = 3;
    private static final int HEIGHT = 7;
    private static final int WIDTH = 15;
    public static final int INITIAL_ENERGY = 10;

    private final AtomicInteger distance = new AtomicInteger(0);
    private final Board board;

    public RobotEvolution() {
        this.board = new Board(HEIGHT, WIDTH);
    }

    @Override
    protected void InitFromParameters() throws Exception {
        super.InitFromParameters();
        _testCases = IntStream.range(0, NUMBER_OF_BATTLES_PER_TEST)
                .mapToObj(i -> new GATestCase(i, 0))
                .toList();
    }

    @Override
    protected void InitInterpreter(final Interpreter interpreter) {
        RobotInterpreter robotInterpreter = (RobotInterpreter) interpreter;
        robotInterpreter.setIsAlive(board::isAlive);
        var left = new RobotInstruction(() -> board.move(Direction.LEFT), distance);
        var up = new RobotInstruction(() -> board.move(Direction.UP), distance);
        var right = new RobotInstruction(() -> board.move(Direction.RIGHT), distance);
        var down = new RobotInstruction(() -> board.move(Direction.DOWN), distance);
        robotInterpreter.AddInstruction("robot.moveleft", left);
        robotInterpreter.AddInstruction("robot.moveup", up);
        robotInterpreter.AddInstruction("robot.moveright", right);
        robotInterpreter.AddInstruction("robot.movedown", down);
    }

    @Override
    public float EvaluateTestCase(final GAIndividual individual, final Object input, final Object output) {
        distance.set(0);
        final PushGPIndividual robot = (PushGPIndividual) individual;
        final Program program = robot._program;
        _interpreter.Execute(program, _executionLimit);
        int maxDistance = WIDTH * HEIGHT + INITIAL_ENERGY;
        return maxDistance - distance.get();
    }
}