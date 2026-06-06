package com.meteorplus.scripting.blocks.sensing;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Wird [BLOCK] berührt?
 */
public class TouchingBlock extends Block {
    public TouchingBlock() {
        super("touching", "Wird [BLOCK] berührt?", "Fühlen", "#0099CC");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        String blockName = getParameterAsString("block");
        boolean result = ctx.isPlayerTouchingBlock(blockName);
        ctx.setVariable("lastCondition", new Variable("lastCondition", result, Variable.VariableType.BOOLEAN));
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"block"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.TEXT;
    }
}
