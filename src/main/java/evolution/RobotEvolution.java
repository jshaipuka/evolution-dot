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
import java.util.stream.Stream;

public class RobotEvolution extends PushGP {

    private static final int NUMBER_OF_BATTLES_PER_TEST = 5;
    private static final int WIDTH = 75;
    private static final int HEIGHT = 35;
    public static final int INITIAL_ENERGY = 10;

    private final AtomicInteger distance = new AtomicInteger(0);
    private final BoardProxy boardProxy = new BoardProxy();

    @Override
    protected void InitFromParameters() throws Exception {
        super.InitFromParameters();
        _testCases = IntStream.range(0, NUMBER_OF_BATTLES_PER_TEST)
                .mapToObj(i -> new GATestCase(i, 0))
                .toList();
    }

    @Override
    protected void InitInterpreter(final Interpreter interpreter) {
        Stream.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
                .map(it -> new RobotInstruction("robot.move" + it.name().toLowerCase(), () -> boardProxy.move(it), distance))
                .forEach(it -> interpreter.AddInstruction(it.getName(), it));
    }

    @Override
    public float EvaluateTestCase(final GAIndividual individual, final Object input, final Object output) {
        boardProxy.setBoard(new Board(WIDTH, HEIGHT));
        distance.set(0);
        final RobotInterpreter robotInterpreter = (RobotInterpreter) _interpreter;
        robotInterpreter.setIsAlive(boardProxy::isAlive);
        final PushGPIndividual robot = (PushGPIndividual) individual;
        final Program program;
        try {
            program = new Program(robot._program);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        _interpreter.Execute(program, _executionLimit);
        int maxDistance = WIDTH * HEIGHT / 5 + INITIAL_ENERGY;
        return maxDistance - distance.get();
    }
}
