package com.meteorplus.scripting.blocks.movement;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Schaue auf Koordinaten [X] [Y] [Z].
 */
public class LookAtBlock extends Block {
    public LookAtBlock() {
        super("look_at", "Schaue auf Koordinaten [X] [Y] [Z]", "Bewegung", "#4A6FA5");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        double x = getParameterAsNumber("x");
        double y = getParameterAsNumber("y");
        double z = getParameterAsNumber("z");
        ctx.lookAtCoordinates(x, y, z);
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"x", "y", "z"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.NUMBER;
    }
}
