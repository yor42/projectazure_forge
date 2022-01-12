package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.client.renderer.items.ItemClaymoreRenderer;
import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import net.minecraft.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.yor42.projectazure.Main.PA_WEAPONS;

public class ItemClaymore extends Item implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);

    public ItemClaymore() {
        //Haha unstackable claymore
        super(new Item.Properties().maxStackSize(1).group(PA_WEAPONS).setISTER(()->ItemClaymoreRenderer::new));
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
