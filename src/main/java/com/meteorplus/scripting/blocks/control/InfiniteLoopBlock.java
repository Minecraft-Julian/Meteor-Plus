package com.meteorplus.scripting.blocks.control;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Wiederhole dauerhaft.
 */
public class InfiniteLoopBlock extends Block {
    public InfiniteLoopBlock() {
        super("infinite_loop", "Wiederhole dauerhaft", "Steuerung", "#FF6633");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        while (!ctx.isStopped()) {
            if (!getChildBlocks().isEmpty()) {
                getChildBlocks().get(0).execute(ctx);
            }
            if (ctx.isStopped()) {
                break;
            }
        }
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[0];
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.TEXT;
    }
}
