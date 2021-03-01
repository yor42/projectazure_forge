package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import static com.yor42.projectazure.setup.register.registerItems.WildcatHP;

public class EntityF4fWildcat extends AbstractEntityPlanes implements IAnimatable{

    public EntityF4fWildcat(EntityType<? extends AbstractEntityPlanes> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        if(Minecraft.getInstance().isGamePaused()){
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
    protected void doAttack() {

    }

    @Override
    public ItemEquipmentPlaneBase getPlaneItem() {
        return (ItemEquipmentPlaneBase) registerItems.EQUIPMENT_PLANE_F4FWildcat.get();
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4F)
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), 0.0F)
                .createMutableAttribute(Attributes.MAX_HEALTH, WildcatHP)
                .createMutableAttribute(Attributes.FLYING_SPEED, 0.4F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2F)
                ;
    }
}
