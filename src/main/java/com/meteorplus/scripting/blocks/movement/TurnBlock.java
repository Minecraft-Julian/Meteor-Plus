package com.meteorplus.scripting.blocks.movement;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Drehe dich um [DEGREES] Grad.
 */
public class TurnBlock extends Block {
    public TurnBlock() {
        super("turn", "Drehe dich um [DEGREES] Grad", "Bewegung", "#4A6FA5");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        double degrees = getParameterAsNumber("degrees");
        ctx.turn(degrees);
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"degrees"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.NUMBER;
    }
}
