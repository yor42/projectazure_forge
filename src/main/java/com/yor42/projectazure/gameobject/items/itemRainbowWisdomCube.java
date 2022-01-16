package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.gui.guiStarterSpawn;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {

        ItemStack cube = playerIn.getItemInHand(handIn);

        UUID PlayerUUID = playerIn.getUUID();

        //openGui
        if(cube.getOrCreateTag().hasUUID("owner")){
            UUID OwnerUUID = cube.getOrCreateTag().getUUID("owner");
            if(!PlayerUUID.equals(OwnerUUID)) {
                playerIn.sendMessage(new TranslationTextComponent("message.rainbow_cube.notowner"), UUID.randomUUID());
                return ActionResult.fail(cube);
            }
        }
        if(worldIn.isClientSide){
            //openGui
            this.openGUI();
            return ActionResult.success(playerIn.getItemInHand(handIn));
        }
        return ActionResult.pass(playerIn.getItemInHand(handIn));
    }

    @OnlyIn(Dist.CLIENT)
    private void openGUI(){
        Minecraft.getInstance().setScreen(new guiStarterSpawn(new TranslationTextComponent("gui.StarterSelection")));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (worldIn != null && worldIn.isClientSide) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(stack.getOrCreateTag().hasUUID("owner")&& worldIn != null) {
            UUID OwnerID = stack.getOrCreateTag().getUUID("owner");
            @Nullable
            PlayerEntity owner = worldIn.getPlayerByUUID(OwnerID);
            if (owner != null) {
                tooltip.add(new TranslationTextComponent("item.projectazure.rainbowcube.owner").withStyle(TextFormatting.GRAY).append(new StringTextComponent(": ")).append(new StringTextComponent(owner.getDisplayName().getString()).withStyle(TextFormatting.YELLOW)));
            }
            else{
                tooltip.add(new TranslationTextComponent("item.projectazure.rainbowcube.owner").withStyle(TextFormatting.GRAY).append(new StringTextComponent(": ")).append(new StringTextComponent(OwnerID.toString()).withStyle(TextFormatting.RED)));
            }
        }
        tooltip.add(new TranslationTextComponent("item.projectazure.rainbowcube.tooltip1").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.projectazure.rainbowcube.tooltip2").withStyle(TextFormatting.GRAY));
    }
}
