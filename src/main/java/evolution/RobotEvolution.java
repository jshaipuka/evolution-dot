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

    private static final int NUMBER_OF_BATTLES_PER_TEST = 5;
    private static final int WIDTH = 15;
    private static final int HEIGHT = 7;
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
        var left = new RobotInstruction(() -> boardProxy.move(Direction.LEFT), distance);
        var up = new RobotInstruction(() -> boardProxy.move(Direction.UP), distance);
        var right = new RobotInstruction(() -> boardProxy.move(Direction.RIGHT), distance);
        var down = new RobotInstruction(() -> boardProxy.move(Direction.DOWN), distance);
        interpreter.AddInstruction("robot.moveleft", left);
        interpreter.AddInstruction("robot.moveup", up);
        interpreter.AddInstruction("robot.moveright", right);
        interpreter.AddInstruction("robot.movedown", down);
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
            program = new Program("(exec.dup (((((((exec.stackdepth exec.do*times (robot.movedown robot.moveleft robot.moveleft)) robot.moveup exec.rand) (robot.movedown exec.shove robot.movedown) robot.movedown ((((exec.stackdepth (robot.moveleft) (exec.yank (robot.moveright)) (exec.pop)) (exec.stackdepth robot.moveup robot.movedown) exec.yank) exec.rand (robot.movedown exec.shove robot.movedown)) exec.do*count exec.stackdepth)) (exec.y robot.movedown (exec.shove (exec.=)) (exec.pop)) exec.stackdepth) robot.moveleft robot.movedown) exec.do*times exec.do*times) (exec.pop robot.moveup exec.flush) robot.moveleft robot.movedown) (exec.stackdepth exec.y robot.movedown))");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        _interpreter.Execute(program, _executionLimit);
        int maxDistance = WIDTH * HEIGHT / 5 + INITIAL_ENERGY;
        return maxDistance - distance.get();
    }
}
