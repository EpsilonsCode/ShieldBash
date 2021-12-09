package com.omircon.shield_bash;

import com.mojang.blaze3d.vertex.PoseStack;
import com.omircon.shield_bash.network.Network;
import com.omircon.shield_bash.network.ShieldBashPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ShieldBash.MODID)
public class ClientEvents {

    private static int cooldown = 0;

    public static void setup()
    {

            MinecraftForge.EVENT_BUS.addListener(ClientEvents::clientTick);
            MinecraftForge.EVENT_BUS.addListener(ClientEvents::keyboardEvent);
            MinecraftForge.EVENT_BUS.addListener(ClientEvents::mouseEvent);
            MinecraftForge.EVENT_BUS.addListener(ClientEvents::renderHand);

    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event)
    {
        if(cooldown>0)
            cooldown--;
    }

    @SubscribeEvent
    public static void keyboardEvent(InputEvent.KeyInputEvent event)
    {
        //System.out.println("da");
        if(event.getKey() == Minecraft.getInstance().options.keyAttack.getKey().getValue() && event.getAction() == GLFW.GLFW_PRESS)
        {
            execute();
        }
    }

    @SubscribeEvent
    public static void mouseEvent(InputEvent.MouseInputEvent event)
    {
        if(event.getButton() == Minecraft.getInstance().options.keyAttack.getKey().getValue() && event.getAction() == GLFW.GLFW_PRESS)
        {
            execute();
        }
    }

    @SubscribeEvent
    public static void renderHand(RenderHandEvent event)
    {
        //System.out.println("render");
        LocalPlayer pPlayer = Minecraft.getInstance().player;
        if(pPlayer.attackAnim>0.0F && pPlayer.isUsingItem() && pPlayer.getUseItem().getItem() == Items.SHIELD) {
            event.setCanceled(true);
            InteractionHand pHand = event.getHand();

            PoseStack pMatrixStack = event.getMatrixStack();
            MultiBufferSource pBuffer = event.getBuffers();
            int pCombinedLight = event.getLight();
            float pSwingProgress = event.getSwingProgress();
            float pEquippedProgress = event.getEquipProgress();
            boolean flag = pHand == InteractionHand.MAIN_HAND;

            HumanoidArm handside = flag ? pPlayer.getMainArm() : pPlayer.getMainArm().getOpposite();
            ItemStack pStack = event.getItemStack();
            boolean flag3 = handside == HumanoidArm.RIGHT;
            pMatrixStack.pushPose();

            float f5 = -0.4F * Mth.sin(Mth.sqrt(pSwingProgress) * (float) Math.PI);
            float f6 = 0.2F * Mth.sin(Mth.sqrt(pSwingProgress) * ((float) Math.PI * 2F));
            float f10 = -0.2F * Mth.sin(pSwingProgress * (float) Math.PI);
            int l = flag3 ? 1 : -1;
            pMatrixStack.translate((double) ((float) l * f5), (double) f6, (double) f10);
            applyItemArmTransform(pMatrixStack, handside, pEquippedProgress);
            applyItemArmAttackTransform(pMatrixStack, handside, pSwingProgress);

            Minecraft.getInstance().getItemInHandRenderer().renderItem(pPlayer, pStack, flag3 ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag3, pMatrixStack, pBuffer, pCombinedLight);

            pMatrixStack.popPose();
        }
    }

    public static void execute()
    {
        //System.out.println("execute");
        if(Minecraft.getInstance().screen == null && Minecraft.getInstance().player.isUsingItem() && Minecraft.getInstance().player.getUseItem().getItem() == Items.SHIELD && cooldown <= 0)
        {
            cooldown = 32;
            Network.sendToServer(new ShieldBashPacket());
            Minecraft.getInstance().player.attackAnim = 1.0F;
            Minecraft.getInstance().player.swing(Minecraft.getInstance().player.getUsedItemHand(), true);
        }

    }

    private static void applyItemArmTransform(PoseStack pMatrixStack, HumanoidArm pHand, float pEquippedProg) {
        int i = pHand == HumanoidArm.RIGHT ? 1 : -1;
        pMatrixStack.translate((double)((float)i * 0.56F), (double)(-0.52F + pEquippedProg * -0.6F), (double)-0.72F);
    }

    private static void applyItemArmAttackTransform(PoseStack pMatrixStack, HumanoidArm pHand, float pSwingProgress) {
        int i = pHand == HumanoidArm.RIGHT ? 1 : -1;
        float f = Mth.sin(pSwingProgress * pSwingProgress * (float)Math.PI);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees((float)i * (45.0F + f * -20.0F)));
        float f1 = Mth.sin(Mth.sqrt(pSwingProgress) * (float)Math.PI);
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees((float)i * f1 * -20.0F));
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(f1 * -80.0F));
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees((float)i * -45.0F));
    }
}
