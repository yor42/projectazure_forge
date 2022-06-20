package com.yor42.projectazure.gameobject.entity.planes;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import static com.yor42.projectazure.gameobject.misc.DamageSources.PLANE_GUN;
import static com.yor42.projectazure.setup.register.registerItems.WildcatHP;

public class EntityF4fWildcat extends AbstractEntityPlanes implements IAnimatable{

    public EntityF4fWildcat(EntityType<? extends AbstractEntityPlanes> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();


        if(this.isAlive()) {
            event.getController().setAnimation(builder.addAnimation("animation.f4fwildcat.fly", true));
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
        return (ItemEquipmentPlaneBase) registerItems.EQUIPMENT_PLANE_F4FWildcat.get();
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, 1.0F)
                .add(ForgeMod.SWIM_SPEED.get(), 0.0F)
                .add(Attributes.MAX_HEALTH, WildcatHP)
                .add(Attributes.FLYING_SPEED, 2F)
                .add(Attributes.ATTACK_DAMAGE, 2F)
                ;
    }
}