package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.gui.guiStarterSpawn;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class itemRainbowWisdomCube extends Item {
    public itemRainbowWisdomCube(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {

        ItemStack cube = playerIn.getItemInHand(handIn);

        UUID PlayerUUID = playerIn.getUUID();

        //openGui
        if(cube.getOrCreateTag().hasUUID("owner")){
            UUID OwnerUUID = cube.getOrCreateTag().getUUID("owner");
            if(!PlayerUUID.equals(OwnerUUID)) {
                playerIn.sendMessage(new TranslatableComponent("message.rainbow_cube.notowner"), UUID.randomUUID());
                return InteractionResultHolder.fail(cube);
            }
        }
        if(worldIn.isClientSide){
            //openGui
            this.openGUI();
            return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
        }
        return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
    }

    @OnlyIn(Dist.CLIENT)
    private void openGUI(){
        Minecraft.getInstance().setScreen(new guiStarterSpawn(new TranslatableComponent("gui.StarterSelection")));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (worldIn != null && worldIn.isClientSide) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if(stack.getOrCreateTag().hasUUID("owner")&& worldIn != null) {
            UUID OwnerID = stack.getOrCreateTag().getUUID("owner");
            @Nullable
            Player owner = worldIn.getPlayerByUUID(OwnerID);
            if (owner != null) {
                tooltip.add(new TranslatableComponent("item.projectazure.rainbowcube.owner").withStyle(ChatFormatting.GRAY).append(new TextComponent(": ")).append(new TextComponent(owner.getDisplayName().getString()).withStyle(ChatFormatting.YELLOW)));
            }
            else{
                tooltip.add(new TranslatableComponent("item.projectazure.rainbowcube.owner").withStyle(ChatFormatting.GRAY).append(new TextComponent(": ")).append(new TextComponent(OwnerID.toString()).withStyle(ChatFormatting.RED)));
            }
        }
        tooltip.add(new TranslatableComponent("item.projectazure.rainbowcube.tooltip1").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("item.projectazure.rainbowcube.tooltip2").withStyle(ChatFormatting.GRAY));
    }
}
