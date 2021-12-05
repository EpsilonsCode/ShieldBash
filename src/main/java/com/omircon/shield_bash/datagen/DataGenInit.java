package com.omircon.shield_bash.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenInit
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if(event.includeServer())
        {
            generator.addProvider(new LootProvider(generator));
            //System.out.println("yes");
        }
        if(event.includeClient())
        {
            generator.addProvider(new LangProvider(generator));
        }
        //event.getGenerator().run();
    }
}
