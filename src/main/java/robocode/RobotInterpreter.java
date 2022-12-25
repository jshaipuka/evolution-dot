package robocode;

import org.spiderland.Psh.Interpreter;
import java.util.function.Supplier;


public class RobotInterpreter extends Interpreter {
    private final Supplier<Boolean> isAlive;

    public RobotInterpreter(Supplier<Boolean> isAlive) {
        this.isAlive = isAlive;
    }

    @Override
    public int Step(int maxSteps) {
        int executed = 0;
        while (isAlive.get() && maxSteps != 0 && _execStack.size() > 0) {
            ExecuteInstruction(_execStack.pop());
            maxSteps--;
            executed++;
        }

        _totalStepsTaken += executed;
        return executed;
    }
}
