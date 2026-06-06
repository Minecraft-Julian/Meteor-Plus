package com.meteorplus.scripting.blocks.actions;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Schreibe [MESSAGE] in den Chat.
 */
public class SayBlock extends Block {
    public SayBlock() {
        super("say", "Schreibe [MESSAGE] in den Chat", "Aussehen", "#FF6B6B");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        String message = getParameterAsString("message");
        if (!message.isBlank()) {
            ctx.sendChat(message);
        }
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"message"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.TEXT;
    }
}
