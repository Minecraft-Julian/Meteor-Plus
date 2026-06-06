package com.meteorplus.scripting.blocks.control;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Falls [CONDITION] dann.
 */
public class IfBlock extends Block {
    public IfBlock() {
        super("if", "Falls [CONDITION] dann", "Steuerung", "#FF6633");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        boolean condition = getParameterAsBoolean("condition");
        if (condition && !getChildBlocks().isEmpty()) {
            getChildBlocks().get(0).execute(ctx);
        }
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"condition"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.BOOLEAN;
    }
}
