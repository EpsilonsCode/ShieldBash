package com.omircon.shield_bash.datagen;

import com.omircon.shield_bash.Registries;
import com.omircon.shield_bash.ShieldBash;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static com.omircon.shield_bash.ShieldBash.MODID;

public class LangProvider extends LanguageProvider {
    public LangProvider(DataGenerator gen) {
        super(gen, MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        //add(Registries.SMELTING_TOUCH.get(), "Smelting Touch");
        add(Registries.GUARD.get(), "Guard");
        add(Registries.SPIKES.get(), "Spikes");
    }
}
