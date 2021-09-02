package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.ai.goals.DroneRangedAttackGoal;
import com.yor42.projectazure.gameobject.entity.ai.targetAI.DroneOwnerAttackedTargetGoal;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityMissileDroneMissile;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileBullet;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.passive.TameableEntity;
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
import static com.yor42.projectazure.setup.register.registerManager.PROJECTILE_DRONE_MISSILE;

public class EntityMissileDrone extends AbstractEntityDrone{

    protected static final DataParameter<Integer> FireTick = EntityDataManager.createKey(EntityMissileDrone.class, DataSerializers.VARINT);

    private static final int MAX_AMMO = 8;

    public EntityMissileDrone(EntityType<? extends EntityMissileDrone> type, World worldIn) {

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

    public int getFiredelay(){
        return 40;
    }

    public void setFiringTick(int value){
        this.getDataManager().set(FireTick, value);
    }

    public void FireMissile(LivingEntity target){
        double x = target.getPosX() - (this.getPosX());
        double y = target.getPosY() - (0.5D + this.getPosYHeight(0.5D));
        double z = target.getPosZ() - (this.getPosZ());

        EntityMissileDroneMissile droneMissile = PROJECTILE_DRONE_MISSILE.create(this.getEntityWorld());
        if(droneMissile != null){
            droneMissile.shoot(this, target, x,y,z);
            this.setFiringTick(this.getFireingTick());
            this.setAmmo(this.getammo()-1);
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FireTick, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new DroneRangedAttackGoal(this));
        super.registerGoals();

        this.targetSelector.addGoal(1, new DroneOwnerAttackedTargetGoal(this));
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
