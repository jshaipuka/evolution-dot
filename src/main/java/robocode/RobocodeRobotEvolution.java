package robocode;

import board.Board;
import org.spiderland.Psh.GAIndividual;
import org.spiderland.Psh.GATestCase;
import org.spiderland.Psh.Interpreter;
import org.spiderland.Psh.Program;
import org.spiderland.Psh.PushGP;
import org.spiderland.Psh.PushGPIndividual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RobocodeRobotEvolution extends PushGP {

    private static final int NUMBER_OF_BATTLES_PER_TEST = 1;
    private static final int MAX_SCORE = 180;
    private static final int MAX_POSSIBLE_DIFFERENCE = MAX_SCORE * 2;
    private static final int PENALTY_FOR_NOT_MOVING = 10;

    private final AtomicInteger steps = new AtomicInteger(0);
    private final Board board;

    public RobocodeRobotEvolution() {
        this.board = new Board(7, 15);
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
        Stream.of("robot.moveleft", "robot.moveup", "robot.moveright", "robot.movedown")
                .map(it -> new RobotInstruction(it, steps))
                .forEach(it -> interpreter.AddInstruction(it.getName(), it));
    }

    @Override
    public float EvaluateTestCase(final GAIndividual individual, final Object input, final Object output) {
        steps.set(0);
        final PushGPIndividual robot = (PushGPIndividual) individual;
        final Program program = robot._program;
        final String programString = program.toString();
        System.setProperty("RobotProgram.push", programString);
        final Results results = battleRunner.runBattle();

        final int diff = MAX_POSSIBLE_DIFFERENCE - (results.myResults().getScore() - results.enemyResults().getScore() + MAX_SCORE);
        if (diff == MAX_POSSIBLE_DIFFERENCE) {
            _interpreter.clearStacks();
            _interpreter.Execute(program, _executionLimit);
            return diff + (steps.get() == 0 ? PENALTY_FOR_NOT_MOVING : 0);
        } else {
            return diff;
        }
    }

    /**
     * Let us optimize the worst battle. The method assumes that the list of the errors is not empty and the errors are all non-negative.
     */
    @Override
    protected float AbsoluteAverageOfErrors(final ArrayList<Float> errors) {
        return Collections.max(errors);
    }
}