package com.meteorplus.scripting.blocks.events;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Wenn Script gestartet.
 */
public class OnStartBlock extends Block {
    public OnStartBlock() {
        super("on_start", "Wenn Script gestartet", "Ereignisse", "#FFDD33");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
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
