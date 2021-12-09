package com.omircon.shield_bash.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.omircon.shield_bash.ShieldBash.MODID;

public class Network {

    private static SimpleChannel INSTANCE;

    private static int ID = 0;

    private static int nextID()
    {
        return ID++;
    }

    public static void registerMessages()
    {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "enchantconfig"),
                () -> "1.0",
                s -> true,
                s -> true);

        INSTANCE.messageBuilder(ShieldBashPacket.class, nextID())
                .encoder(ShieldBashPacket::toBytes)
                .decoder(ShieldBashPacket::new)
                .consumer(ShieldBashPacket::handle)
                .add();

    }

    public static void sendToClient(Object packet, ServerPlayer player)
    {
        INSTANCE.sendTo(packet, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet)
    {
        INSTANCE.sendToServer(packet);
    }
}