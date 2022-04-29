package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.renderer.items.ItemClaymoreRenderer;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class AnimateableMachineBlockItems extends BlockItem implements IAnimatable {

    protected final String controllerName = "Controller";
    public AnimationFactory factory = new AnimationFactory(this);
    private final boolean shouldAddShiftToolTip;
    BlockEntityWithoutLevelRenderer render;

    public AnimateableMachineBlockItems(Block blockIn, Properties builder, boolean shouldAddShiftToolTip, BlockEntityWithoutLevelRenderer render) {
        super(blockIn, builder);
        this.shouldAddShiftToolTip = shouldAddShiftToolTip;
        this.render = render;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = AnimateableMachineBlockItems.this.render;

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
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
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (worldIn != null && worldIn.isClientSide && this.shouldAddShiftToolTip) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
