package robocode;

import org.spiderland.Psh.Instruction;
import org.spiderland.Psh.Interpreter;

import java.util.concurrent.atomic.AtomicInteger;

public class RobotInstruction extends Instruction {
    private final Runnable callback;
    private final AtomicInteger counter;

    public RobotInstruction(Runnable callback, AtomicInteger counter) {
        this.callback = callback;
        this.counter = counter;
    }

    @Override
    public void Execute(Interpreter interpreter) {
        callback.run();
        counter.incrementAndGet();
    }
}
