package evolution;

import org.spiderland.Psh.Interpreter;

import java.util.function.Supplier;


public class RobotInterpreter extends Interpreter {
    private Supplier<Boolean> isAlive;

    @Override
    public int Step(int maxSteps) {
        int executed = 0;
        while (isAlive.get() && maxSteps != 0 && execStack.size() > 0) {
            executeInstruction(execStack.pop());
            maxSteps--;
            executed++;
        }

        totalStepsTaken += executed;
        return executed;
    }

    public void setIsAlive(Supplier<Boolean> isAlive) {
        this.isAlive = isAlive;
    }
}
