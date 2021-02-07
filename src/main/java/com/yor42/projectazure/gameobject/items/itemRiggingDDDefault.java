package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.libs.defined;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class itemRiggingDDDefault extends ItemRiggingDD implements IAnimatable {

    public itemRiggingDDDefault(Properties properties) {
        super(properties);
    }

    @Override
    public AnimatedGeoModel getModel() {
        return new modelDDRiggingDefault();
    }

    @Override
    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event)
    {
        return PlayState.STOP;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote()) {
            if (playerIn.isSneaking()) {
                InventoryRiggingDefaultDD.openGUI((ServerPlayerEntity)playerIn, playerIn.inventory.getCurrentItem());
                return ActionResult.resultSuccess(itemstack);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
