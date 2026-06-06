package com.meteorplus.scripting.blocks.operators;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

/**
 * Block: [A] [OP] [B]
 */
public class MathBlock extends Block {
    public MathBlock() {
        super("math", "[A] [OP] [B]", "Operatoren", "#99FF99");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        double a = getParameterAsNumber("a");
        double b = getParameterAsNumber("b");
        String operator = getParameterAsString("operator");
        double result;

        switch (operator) {
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                result = b == 0 ? 0 : a / b;
                break;
            case "%":
                result = b == 0 ? 0 : a % b;
                break;
            default:
                result = a + b;
                break;
        }

        ctx.setVariable("mathResult", new Variable("mathResult", result, Variable.VariableType.NUMBER));
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"a", "operator", "b"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        if ("operator".equals(slotName)) {
            return Variable.VariableType.TEXT;
        }
        return Variable.VariableType.NUMBER;
    }
}
