package com.yor42.projectazure.gameobject.items;

import com.tac.guns.GunMod;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.util.Process;
import net.minecraft.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class GeoGunItem extends TimelessGunItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public GeoGunItem(Process<Properties> properties, IGunModifier... modifiers) {
        super(properties, modifiers);
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationEvent<?> animationEvent) {
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
