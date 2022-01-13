package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.item.Item.Properties;

public class AnimateableMachineBlockItems extends BlockItem implements IAnimatable {

    protected final String controllerName = "Controller";
    public AnimationFactory factory = new AnimationFactory(this);
    private final boolean shouldAddShiftToolTip;

    public AnimateableMachineBlockItems(Block blockIn, Properties builder, boolean shouldAddShiftToolTip) {
        super(blockIn, builder);
        this.shouldAddShiftToolTip = shouldAddShiftToolTip;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController controller = new AnimationController(this, this.controllerName, 1, this::predicate);
        animationData.addAnimationController(controller);
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (worldIn != null && worldIn.isClientSide && this.shouldAddShiftToolTip) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        tooltip.add(new TranslationTextComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(TextFormatting.GRAY));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
