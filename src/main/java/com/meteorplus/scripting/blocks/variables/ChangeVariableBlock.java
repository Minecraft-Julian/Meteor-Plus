package com.meteorplus.scripting.blocks.variables;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Ändere [NAME] um [AMOUNT].
 */
public class ChangeVariableBlock extends Block {
    public ChangeVariableBlock() {
        super("change_variable", "Ändere [NAME] um [AMOUNT]", "Variablen", "#CC6600");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        String name = getParameterAsString("name");
        double amount = getParameterAsNumber("amount");
        if (name != null && !name.isBlank()) {
            Variable current = ctx.getVariable(name);
            double base = current != null ? current.getAsNumber() : 0;
            ctx.setVariable(name, new Variable(name, base + amount, Variable.VariableType.NUMBER));
        }
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"name", "amount"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        if ("amount".equals(slotName)) {
            return Variable.VariableType.NUMBER;
        }
        return Variable.VariableType.TEXT;
    }
}
