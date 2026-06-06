package com.meteorplus.scripting;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.meteorplus.scripting.blocks.actions.GiveItemBlock;
import com.meteorplus.scripting.blocks.actions.SayBlock;
import com.meteorplus.scripting.blocks.actions.WaitBlock;
import com.meteorplus.scripting.blocks.control.IfBlock;
import com.meteorplus.scripting.blocks.control.InfiniteLoopBlock;
import com.meteorplus.scripting.blocks.control.LoopBlock;
import com.meteorplus.scripting.blocks.events.OnStartBlock;
import com.meteorplus.scripting.blocks.movement.LookAtBlock;
import com.meteorplus.scripting.blocks.movement.MoveForwardBlock;
import com.meteorplus.scripting.blocks.movement.TurnBlock;
import com.meteorplus.scripting.blocks.operators.MathBlock;
import com.meteorplus.scripting.blocks.sensing.KeyPressedBlock;
import com.meteorplus.scripting.blocks.sensing.TouchingBlock;
import com.meteorplus.scripting.blocks.sound.PlaySoundBlock;
import com.meteorplus.scripting.blocks.variables.ChangeVariableBlock;
import com.meteorplus.scripting.blocks.variables.SetVariableBlock;

/**
 * Registry for block factories used during script creation.
 */
public class BlockRegistry {
    private static final Map<String, Supplier<Block>> REGISTRY = new HashMap<>();

    static {
        registerDefaultBlocks();
    }

    public static void register(String id, Supplier<Block> supplier) {
        if (id == null || supplier == null) {
            return;
        }
        REGISTRY.put(id, supplier);
    }

    public static Block createBlock(String id) {
        Supplier<Block> supplier = REGISTRY.get(id);
        if (supplier == null) {
            return null;
        }
        return supplier.get();
    }

    private static void registerDefaultBlocks() {
        register("on_start", OnStartBlock::new);
        register("wait", WaitBlock::new);
        register("say", SayBlock::new);
        register("give_item", GiveItemBlock::new);
        register("play_sound", PlaySoundBlock::new);
        register("move_forward", MoveForwardBlock::new);
        register("turn", TurnBlock::new);
        register("look_at", LookAtBlock::new);
        register("loop", LoopBlock::new);
        register("infinite_loop", InfiniteLoopBlock::new);
        register("if", IfBlock::new);
        register("set_variable", SetVariableBlock::new);
        register("change_variable", ChangeVariableBlock::new);
        register("math", MathBlock::new);
        register("touching", TouchingBlock::new);
        register("key_pressed", KeyPressedBlock::new);
    }
}
