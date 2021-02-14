package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
    protected int firedelay;
    protected int MaxHP;

    public ItemEquipmentBase(Properties properties) {
        super(properties);
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

    public int getFiredelay() {
        return this.firedelay;
    }

    public abstract void onUpdate(ItemStack stack);

    public void setMaxHP(int maxHP) {
        this.MaxHP = maxHP;
    }

    public int getMaxHP() {
        return this.MaxHP;
    }

    public abstract boolean canUseCanon(ItemStack stack);
    public abstract boolean canUseTorpedo(ItemStack stack);

    public void checkSlotAndFire(ItemStack equipmentStack, enums.SLOTTYPE slot){
        ItemEquipmentBase equipment = (ItemEquipmentBase) equipmentStack.getItem();
        if(slot == equipment.getSlot()){
            onFire(equipmentStack);
        }
    }

    public abstract void onFire(ItemStack equipmentStack);

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

    public void DamageEquipment(double damage, ItemStack equipmentStack){
        CompoundNBT compound = equipmentStack.getOrCreateTag();
        int currentHP = compound.getInt("HP");
        if(currentHP-damage >= 0){
            compound.putInt("HP", (int) (currentHP-damage));
        }
        else
            compound.putInt("HP", 0);
    }
}
