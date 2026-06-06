package com.meteorplus.scripting.blocks.sound;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;

/**
 * Block: "Spiele Klang [SOUND]"
 * Plays a sound effect.
 */
public class PlaySoundBlock extends Block {
    public PlaySoundBlock() {
        super(
            "play_sound",
            "Spiele Klang [SOUND]",
            "Klang",
            "#FF66FF"
        );
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        String soundName = getParameterAsString("sound");
        
        if (!ctx.isPlayerValid()) {
            throw new IllegalStateException("Player is not valid");
        }

        MinecraftClient client = ctx.getClient();
        if (client.world != null) {
            double x = ctx.getPlayer().getX();
            double y = ctx.getPlayer().getY();
            double z = ctx.getPlayer().getZ();
            
            client.world.playSound(x, y, z, SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f, false);
        }
        
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[]{"sound"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.TEXT;
    }
}
