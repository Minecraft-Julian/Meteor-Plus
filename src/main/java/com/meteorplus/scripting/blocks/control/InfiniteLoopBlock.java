package com.meteorplus.scripting.blocks.control;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: "Wiederhole dauerhaft"
 * Loops content forever until script is stopped.
 */
public class InfiniteLoopBlock extends Block {
    private Block loopContent;

    public InfiniteLoopBlock() {
        super(
            "infinite_loop",
            "Wiederhole dauerhaft",
            "Steuerung",
            "#FF6633"  // Orange
        );
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        while (ctx.isRunning()) {
            if (loopContent != null) {
                loopContent.execute(ctx);
            } else {
                // Prevent infinite loop without content
                ctx.waitTicks(1);
            }
        }
        
        executeNext(ctx);
    }

    public void setLoopContent(Block block) {
        this.loopContent = block;
    }

    public Block getLoopContent() {
        return loopContent;
    }

    @Override
    public String[] getInputSlots() {
        return new String[]{};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.TEXT;
    }
}
