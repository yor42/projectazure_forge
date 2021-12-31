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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        ItemStack cube = playerIn.getHeldItem(handIn);

        UUID PlayerUUID = playerIn.getUniqueID();

        //openGui
        if(cube.getOrCreateTag().hasUniqueId("owner")){
            UUID OwnerUUID = cube.getOrCreateTag().getUniqueId("owner");
            if(!PlayerUUID.equals(OwnerUUID)) {
                playerIn.sendMessage(new TranslationTextComponent("message.rainbow_cube.notowner"), UUID.randomUUID());
                return ActionResult.resultFail(cube);
            }
        }
        if(worldIn.isRemote){
            //openGui
            this.openGUI();
            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
        }
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @OnlyIn(Dist.CLIENT)
    private void openGUI(){
        Minecraft.getInstance().displayGuiScreen(new guiStarterSpawn(new TranslationTextComponent("gui.StarterSelection")));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (worldIn != null && worldIn.isRemote) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(stack.getOrCreateTag().hasUniqueId("owner")&& worldIn != null) {
            UUID OwnerID = stack.getOrCreateTag().getUniqueId("owner");
            @Nullable
            PlayerEntity owner = worldIn.getPlayerByUuid(OwnerID);
            if (owner != null) {
                tooltip.add(new TranslationTextComponent("item.projectazure.rainbowcube.owner").mergeStyle(TextFormatting.GRAY).append(new StringTextComponent(": ")).append(new StringTextComponent(owner.getDisplayName().getString()).mergeStyle(TextFormatting.YELLOW)));
            }
            else{
                tooltip.add(new TranslationTextComponent("item.projectazure.rainbowcube.owner").mergeStyle(TextFormatting.GRAY).append(new StringTextComponent(": ")).append(new StringTextComponent(OwnerID.toString()).mergeStyle(TextFormatting.RED)));
            }
        }
        tooltip.add(new TranslationTextComponent("item.projectazure.rainbowcube.tooltip1").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.projectazure.rainbowcube.tooltip2").mergeStyle(TextFormatting.GRAY));
    }
}
