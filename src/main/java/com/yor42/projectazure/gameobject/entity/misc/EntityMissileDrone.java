package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import static com.yor42.projectazure.setup.register.registerItems.DRONE_BAMISSILE;
import static com.yor42.projectazure.setup.register.registerItems.WildcatHP;

public class EntityMissileDrone extends AbstractEntityDrone{

    protected static final DataParameter<Integer> FireTick = EntityDataManager.createKey(EntityMissileDrone.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> AMMO = EntityDataManager.createKey(EntityMissileDrone.class, DataSerializers.VARINT);

    private static final int MAX_AMMO = 8;

    public EntityMissileDrone(EntityType<? extends CreatureEntity> type, World worldIn) {

        super(type, worldIn);
    }

    @Override
    protected <T extends IAnimatable> PlayState body_predicate(AnimationEvent<T> event) {
        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isFiring()){
            event.getController().setAnimation(builder.addAnimation("body_fire", true));
            return PlayState.CONTINUE;
        }
        else if(!(this.limbSwingAmount > -0.1F && this.limbSwingAmount < 0.1F)){
            event.getController().setAnimation(builder.addAnimation("body_moveforward", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;

    }

    @Override
    protected PlayState propeller_predicate(AnimationEvent<AbstractEntityDrone> event) {
        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        if(!this.isOnGround()){
            event.getController().setAnimation(builder.addAnimation("propeller_fly", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public Item getDroneItem() {
        return DRONE_BAMISSILE.get();
    }

    public boolean isFiring(){
        return this.getDataManager().get(FireTick)>0;
    }

    public int getFireingTick(){
        return this.getDataManager().get(FireTick);
    }

    public void setFiringTick(int value){
        this.getDataManager().set(FireTick, value);
    }

    public int getammo() {
        return this.dataManager.get(AMMO);
    }

    public void setAmmo(int value){
        this.dataManager.set(AMMO, value);
    }

    public int useAmmo(){
        if(this.getammo()>0) {
            int ammoAfterUse = Math.min(this.getammo() - 1,0);
            this.setAmmo(ammoAfterUse);
        }
        return 0;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FireTick, 0);
        this.dataManager.register(AMMO, 0);
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5F)
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), 0.0F)
                .createMutableAttribute(Attributes.MAX_HEALTH, 10)
                .createMutableAttribute(Attributes.FLYING_SPEED, 0.5F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2F)
                ;
    }
}
