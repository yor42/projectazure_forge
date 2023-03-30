package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.gameobject.entity.ai.goals.DroneRangedAttackGoal;
import com.yor42.projectazure.gameobject.entity.ai.targetAI.DroneOwnerAttackedTargetGoal;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityMissileDroneMissile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import static com.yor42.projectazure.setup.register.RegisterItems.DRONE_BAMISSILE;
import static com.yor42.projectazure.setup.register.registerEntity.DRONE_MISSILE;

public class EntityMissileDrone extends AbstractEntityFollowingDrone {

    protected static final EntityDataAccessor<Integer> FireTick = SynchedEntityData.defineId(EntityMissileDrone.class, EntityDataSerializers.INT);

    private static final int MAX_AMMO = 8;

    public EntityMissileDrone(EntityType<? extends EntityMissileDrone> type, Level worldIn) {

        super(type, worldIn);
    }

    @Override
    protected <T extends IAnimatable> PlayState body_predicate(AnimationEvent<T> event) {
        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isFiring()){
            event.getController().setAnimation(builder.addAnimation("body_fire", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(!(this.animationSpeed > -0.1F && this.animationSpeed < 0.1F)){
            event.getController().setAnimation(builder.addAnimation("body_moveforward", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;

    }

    @Override
    protected PlayState propeller_predicate(AnimationEvent<AbstractEntityFollowingDrone> event) {
        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        if(!this.isOnGround()){
            event.getController().setAnimation(builder.addAnimation("propeller_fly", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public Item getDroneItem() {
        return DRONE_BAMISSILE.get();
    }

    public boolean isFiring(){
        return this.getEntityData().get(FireTick)>0;
    }

    public int getFireingTick(){
        return this.getEntityData().get(FireTick);
    }

    public int getFiredelay(){
        return 40;
    }

    public void setFiringTick(int value){
        this.getEntityData().set(FireTick, value);
    }

    public void FireMissile(LivingEntity target){
        double x = target.getX() - (this.getX());
        double y = target.getY() - (0.5D + this.getY(0.5D));
        double z = target.getZ() - (this.getZ());

        EntityMissileDroneMissile droneMissile = DRONE_MISSILE.get().create(this.getLevel());
        if(droneMissile != null){
            droneMissile.shoot(this, target, x,y,z);
            this.setFiringTick(this.getFireingTick());
            this.setAmmo(this.getammo()-1);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FireTick, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new DroneRangedAttackGoal(this));
        super.registerGoals();

        this.targetSelector.addGoal(1, new DroneOwnerAttackedTargetGoal(this));
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, 0.5F)
                .add(ForgeMod.SWIM_SPEED.get(), 0.0F)
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.FLYING_SPEED, 0.5F)
                .add(Attributes.ATTACK_DAMAGE, 2F)
                ;
    }
}
