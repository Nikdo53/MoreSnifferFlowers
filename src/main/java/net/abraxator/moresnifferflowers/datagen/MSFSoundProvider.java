package net.abraxator.moresnifferflowers.datagen;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFSounds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class MSFSoundProvider extends SoundDefinitionsProvider {
    protected MSFSoundProvider(PackOutput output) {
        super(output, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    public void registerSounds() {
        add(MSFSounds.CROPRESSOR_BELT, definition()
                .subtitle("sound.moresnifferflowers." + MSFSounds.CROPRESSOR_BELT.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("cropressor_sound_1")))
                .with(sound(MoreSnifferFlowers.loc("cropressor_sound_2"))));
        add(MSFSounds.DYESPRIA_PAINT, definition()
                .subtitle("sound.moresnifferflowers." + MSFSounds.DYESPRIA_PAINT.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("dyespria_paint"))));

        add(MSFSounds.BOBLING_BATTLE_DISC, definition()
                .subtitle("sound.moresnifferflowers." + MSFSounds.BOBLING_BATTLE_DISC.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("battle_music_disc")).stream()));

    }
}
