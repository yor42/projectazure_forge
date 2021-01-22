package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.gui.guiStarterSpawn;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

public class itemRainbowWisdomCube extends Item {
    public itemRainbowWisdomCube(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(worldIn.isRemote){
            //openGui
            Minecraft.getInstance().displayGuiScreen(new guiStarterSpawn(new TranslationTextComponent("gui.StarterSelection")));
            return ActionResult.resultConsume(playerIn.getHeldItem(handIn));
        }
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }
}
