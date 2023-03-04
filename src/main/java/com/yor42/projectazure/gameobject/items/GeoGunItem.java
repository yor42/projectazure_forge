package com.yor42.projectazure.gameobject.items;

import com.tac.guns.util.Process;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class GeoGunItem extends ItemEnergyGun implements IAnimatable, ISyncable {
    public static final int ANIM_FIRE = 0;
    private static final String ControllerID = "gun_controller";
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public GeoGunItem(int energycapacity, int energypershot, int idleenergyconsumption, boolean ignoreAmmo, Process<Item.Properties> properties) {
        super(energycapacity, energypershot, idleenergyconsumption, ignoreAmmo, properties);
        GeckoLibNetwork.registerSyncable(this);
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, ControllerID, 0, this::predicate));
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks) {
        super.fillItemCategory(group, stacks);
    }

    private PlayState predicate(AnimationEvent<?> animationEvent) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, ControllerID);
        if(state == ANIM_FIRE){
            //controller.markNeedsReload();
            //controller.setAnimation(new AnimationBuilder().addAnimation("fire", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }
    }
}
