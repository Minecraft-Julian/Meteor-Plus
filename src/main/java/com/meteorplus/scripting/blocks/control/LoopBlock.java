package com.meteorplus.scripting.blocks.control;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Wiederhole [COUNT] mal.
 */
public class LoopBlock extends Block {
    public LoopBlock() {
        super("loop", "Wiederhole [COUNT] mal", "Steuerung", "#FF6633");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        int count = (int) getParameterAsNumber("count");
        for (int i = 0; i < count && !ctx.isStopped(); i++) {
            if (!getChildBlocks().isEmpty()) {
                getChildBlocks().get(0).execute(ctx);
            }
        }
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"count"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.NUMBER;
    }
}
