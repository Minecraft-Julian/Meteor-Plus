package com.meteorplus.scripting.blocks.actions;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Gib mir [ITEM].
 *
 * In Survival ist diese Aktion nur möglich, wenn Commands aktiv sind.
 */
public class GiveItemBlock extends Block {
    public GiveItemBlock() {
        super("give_item", "Gib mir [ITEM]", "Aussehen", "#FF6B6B");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        String item = getParameterAsString("item");
        if (!item.isBlank() && ctx.getClient() != null && ctx.getClient().player != null) {
            String playerName = ctx.getClient().player.getName().getString();
            ctx.executeCommand("give " + playerName + " " + item);
        }
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"item"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.ITEM;
    }
}
