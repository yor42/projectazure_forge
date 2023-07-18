package com.yor42.projectazure.events;

import com.tac.guns.client.InputHandler;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.interfaces.IChargeFire;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.network.packets.PlaySoundPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.glfw.GLFW;

import static com.tac.guns.client.handler.ShootingHandler.get;
@Mod.EventBusSubscriber(modid = Constants.MODID)
public class ChargeFireHandler {

    public ChargeFireHandler(){}

    private int chargemaxtime=0;
    private int chargeprogress=0;
    private boolean charging = false;
    private boolean shouldfire = false;

    private static ChargeFireHandler instance;
    @NonNull
    public static ChargeFireHandler getInstance(){
        if(instance == null){
            instance = new ChargeFireHandler();
        }
        return instance;
    }
    @OnlyIn(Dist.CLIENT)
    private boolean isInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            if (mc.getOverlay() != null) {
                return false;
            } else if (mc.screen != null) {
                return false;
            } else {
                return mc.mouseHandler.isMouseGrabbed() && mc.isWindowActive();
            }
        } else {
            return false;
        }
    }

    public boolean shouldfire() {
        return this.shouldfire;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onKeyPressed(InputEvent.RawMouseEvent event) {
        if (this.isInGame()) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) {
                return;
            }
            ItemStack heldItem = player.getMainHandItem();
            if (event.getAction() == GLFW.GLFW_PRESS) {
                if (heldItem.getItem() instanceof GunItem) {
                    int button = event.getButton();
                    if (button == 0 || button == 1 && AimingHandler.get().isLookingAtInteractableBlock()) {
                        event.setCanceled(true);
                    }

                    if (InputHandler.PULL_TRIGGER.down) {
                        if (get().getShootTickGapLeft() == 0&& heldItem.getItem() instanceof IChargeFire chargeFire) {
                            this.charging = true;
                            if(chargeFire.getChargeSound() != null) {
                                float pitch = MathUtil.getRand().nextFloat()*0.4F+0.8F;
                                float vol = MathUtil.getRand().nextFloat()*0.4F+0.8F;
                                Main.NETWORK.sendToServer(new PlaySoundPacket(chargeFire.getChargeSound(), SoundSource.PLAYERS, player, pitch, vol));
                                player.playSound(chargeFire.getChargeSound(), pitch, vol);
                            }
                            this.chargemaxtime = chargeFire.getMaxChargeTime();
                        }
                    }
                }
            }
            else if (event.getAction() == GLFW.GLFW_RELEASE && event.getButton() == InputHandler.PULL_TRIGGER.keyCode().getValue()) {
                if (heldItem.getItem() instanceof GunItem) {
                    int button = event.getButton();
                    if (button == 0 || button == 1 && AimingHandler.get().isLookingAtInteractableBlock()) {
                        event.setCanceled(true);
                    }
                    if (heldItem.getItem() instanceof IChargeFire) {
                        if(get().getShootTickGapLeft() == 0&& get().getshootMsGap() <= 0.0F && this.chargeprogress>=this.chargemaxtime) {
                            this.shouldfire = true;
                            get().fire(player, heldItem);
                            this.shouldfire = false;
                        }
                        this.chargeprogress=0;
                        this.charging = false;
                        this.display_percentage();
                    }

                }
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.isInGame()) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player != null && player.getMainHandItem().getItem() instanceof IChargeFire && this.charging) {
                this.chargeprogress = Math.min(this.chargemaxtime, ++this.chargeprogress);
                this.display_percentage();
            }

        }
    }

    private void display_percentage() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) {
            float prog = (float) this.chargeprogress / this.chargemaxtime;
            ChatFormatting color = ChatFormatting.DARK_RED;
            int percentage = (int) (prog * 100);
            BaseComponent text = new TranslatableComponent("gun.desc.charging", percentage + "%");
            if(percentage == 100){
                color = ChatFormatting.BLUE;
                text = new TranslatableComponent("gun.desc.ready");
            }
            else if (percentage > 60) {
                color = ChatFormatting.GREEN;
            } else if (percentage > 30) {
                color = ChatFormatting.YELLOW;
            }
            player.displayClientMessage(text.withStyle(ChatFormatting.BOLD).withStyle(color), true);
        }
    }
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (this.isInGame()) {
            this.chargemaxtime = 0;
            this.chargeprogress = 0;
            this.charging = false;
        }
    }
}
