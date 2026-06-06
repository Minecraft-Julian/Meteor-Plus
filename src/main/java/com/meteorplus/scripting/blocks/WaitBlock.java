package com.meteorplus.scripting.blocks;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: "Warte [SEKUNDEN] Sekunden"
 * Waits for specified seconds before executing next block
 */
public class WaitBlock extends Block {

    public WaitBlock() {
        super(
            "wait",
            "Warte [SEKUNDEN] Sekunden",
            "Kontrolle",
            "#FFE66D"  // Yellow color
        );
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        double seconds = getParameterAsNumber("seconds");

        if (seconds < 0) {
            seconds = 0;
        }

        // Wait for specified seconds
        ctx.waitSeconds(seconds);

        // Execute next block
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[]{"seconds"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        if (slotName.equals("seconds")) {
            return Variable.VariableType.NUMBER;
        }
        return Variable.VariableType.NUMBER;
    }
}
