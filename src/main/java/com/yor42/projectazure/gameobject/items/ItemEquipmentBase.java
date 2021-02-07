package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

public abstract class ItemEquipmentBase extends Item implements IAnimatable {


    public AnimationFactory factory = new AnimationFactory(this);

    protected enums.SLOTTYPE slot;
    //remaining ammo
    protected int ammo;
    //ammo capacity
    protected int maxAmmo;
    protected int reloadTime;
    protected int remainingReloadTime;

    protected int durability;

    public ItemEquipmentBase(Properties properties) {
        super(properties);
    }

    public boolean isBroken(){
        return this.durability == 0;
    }

    public boolean canfire(){
        return this.ammo != 0 && !this.isBroken();
    }

    public enums.SLOTTYPE getSlot() {
        return slot;
    }

    @Override
    public boolean shouldSyncTag() {
        return true;
    }

    public abstract AnimatedGeoModel getEquipmentModel();

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    public ResourceLocation getTexture(){
        return this.getEquipmentModel().getTextureLocation(null);
    };
}
