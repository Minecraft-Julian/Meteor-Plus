package com.meteorplus.scripting.blocks.sensing;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Wird Taste [KEY] gedrückt?
 */
public class KeyPressedBlock extends Block {
    public KeyPressedBlock() {
        super("key_pressed", "Wird Taste [KEY] gedrückt?", "Fühlen", "#0099CC");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        String key = getParameterAsString("key");
        boolean result = ctx.isKeyPressed(key);
        ctx.setVariable("lastCondition", new Variable("lastCondition", result, Variable.VariableType.BOOLEAN));
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"key"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.TEXT;
    }
}
