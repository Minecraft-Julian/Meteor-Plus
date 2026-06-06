package com.meteorplus.scripting.blocks.movement;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Gehe [STEPS] Schritte nach vorne.
 */
public class MoveForwardBlock extends Block {
    public MoveForwardBlock() {
        super("move_forward", "Gehe [STEPS] Schritte nach vorne", "Bewegung", "#4A6FA5");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        double steps = getParameterAsNumber("steps");
        if (steps < 0) {
            steps = 0;
        }
        ctx.moveForward(steps);
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"steps"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.NUMBER;
    }
}
