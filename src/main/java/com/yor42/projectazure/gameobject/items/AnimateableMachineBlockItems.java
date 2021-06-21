package com.yor42.projectazure.gameobject.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AnimateableMachineBlockItems extends BlockItem implements IAnimatable {

    protected final String controllerName = "Controller";
    public AnimationFactory factory = new AnimationFactory(this);

    public AnimateableMachineBlockItems(Block blockIn, Properties builder) {
        super(blockIn, builder);
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
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
