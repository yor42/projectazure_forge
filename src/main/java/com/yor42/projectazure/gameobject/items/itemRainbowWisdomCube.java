package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.gui.guiStarterSpawn;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class itemRainbowWisdomCube extends Item {
    public itemRainbowWisdomCube(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(worldIn.isRemote){
            //openGui
            this.openGUI();
            return ActionResult.resultConsume(playerIn.getHeldItem(handIn));
        }
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @OnlyIn(Dist.CLIENT)
    private void openGUI(){
        Minecraft.getInstance().displayGuiScreen(new guiStarterSpawn(new TranslationTextComponent("gui.StarterSelection")));
    }
}
