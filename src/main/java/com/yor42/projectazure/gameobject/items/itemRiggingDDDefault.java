package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.renderer.items.DDDefaultRiggingRenderer;
import net.minecraft.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class itemRiggingDDDefault extends ItemRiggingDD implements IAnimatable {
    public itemRiggingDDDefault(Properties properties) {
        super(properties);
        equipments.setSize(6);
    }

    @Override
    public IAnimatableModel getModel() {
        return new DDDefaultRiggingRenderer().getGeoModelProvider();
    }

    @Override
    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event)
    {
        return PlayState.STOP;
    }
}
