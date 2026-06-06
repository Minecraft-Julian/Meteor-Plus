package com.meteorplus.scripting.blocks.variables;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: Setze [NAME] auf [VALUE].
 */
public class SetVariableBlock extends Block {
    public SetVariableBlock() {
        super("set_variable", "Setze [NAME] auf [VALUE]", "Variablen", "#CC6600");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        String name = getParameterAsString("name");
        Object value = getParameter("value");
        if (name != null && !name.isBlank()) {
            Variable.VariableType type = Variable.VariableType.TEXT;
            if (value instanceof Number) {
                type = Variable.VariableType.NUMBER;
            } else if (value instanceof Boolean) {
                type = Variable.VariableType.BOOLEAN;
            }
            ctx.setVariable(name, new Variable(name, value, type));
        }
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"name", "value"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        switch (slotName) {
            case "value":
                return Variable.VariableType.TEXT;
            default:
                return Variable.VariableType.TEXT;
        }
    }
}
