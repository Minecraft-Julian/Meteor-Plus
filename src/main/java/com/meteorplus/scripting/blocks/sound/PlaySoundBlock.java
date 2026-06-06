package com.meteorplus.scripting.blocks.sound;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Block: Spiele Klang [SOUND].
 */
public class PlaySoundBlock extends Block {
    public PlaySoundBlock() {
        super("play_sound", "Spiele Klang [SOUND]", "Klang", "#FF66FF");
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        String soundName = getParameterAsString("sound");
        if (!soundName.isBlank() && ctx.getClient() != null && ctx.getClient().player != null) {
            Identifier id = Identifier.tryParse(soundName);
            if (id != null) {
                SoundEvent soundEvent = Registry.SOUND_EVENT.get(id);
                if (soundEvent != null && ctx.getClient().world != null) {
                    ctx.getClient().world.playSound(
                        null,
                        ctx.getClient().player.getBlockPos(),
                        soundEvent,
                        SoundCategory.PLAYERS,
                        1.0f,
                        1.0f
                    );
                }
            }
        }
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[] {"sound"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.TEXT;
    }
}
