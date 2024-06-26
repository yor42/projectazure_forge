package com.yor42.projectazure.gameobject.entity.planes;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import static com.yor42.projectazure.gameobject.misc.DamageSources.PLANE_GUN;
import static com.yor42.projectazure.setup.register.RegisterItems.WildcatHP;

public class EntityF4fWildcat extends AbstractEntityPlanes implements IAnimatable{

    public EntityF4fWildcat(EntityType<? extends AbstractEntityPlanes> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();


        if(this.isAlive()) {
            event.getController().setAnimation(builder.addAnimation("animation.f4fwildcat.fly", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else{
            event.getController().setAnimation(builder.clearAnimations());
        }


        return PlayState.CONTINUE;
    }

    @Override
    protected void doAttack(LivingEntity entity) {

        float damage = this.getAttackDamage();

        if(entity instanceof EntityKansenBase){
            if (((EntityKansenBase) entity).getOwner() == this.getOwner().getOwner()){
                damage = 0;
            }
        }

        boolean isAttackSuccessful = entity.hurt(PLANE_GUN, damage);
        if(isAttackSuccessful){
            this.playSound(registerSounds.PLANE_GUN, this.getSoundVolume(), this.getVoicePitch());
        }
    }

    @Override
    public ItemEquipmentPlaneBase getPlaneItem() {
        return (ItemEquipmentPlaneBase) RegisterItems.EQUIPMENT_PLANE_F4FWildcat.get();
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, 1.0F)
                .add(ForgeMod.SWIM_SPEED.get(), 0.0F)
                .add(Attributes.MAX_HEALTH, WildcatHP)
                .add(Attributes.FLYING_SPEED, 2F)
                .add(Attributes.ATTACK_DAMAGE, 2F)
                ;
    }
}
