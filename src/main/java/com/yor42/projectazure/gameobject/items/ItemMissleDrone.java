package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityDrone;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMissleDrone extends AbstractItemPlaceableDrone{

    public AnimationFactory factory = new AnimationFactory(this);

    public ItemMissleDrone(Properties properties, int MaxHP, int maxFuelmb) {
        super(properties, MaxHP, 8, maxFuelmb);
    }

    @Override
    public EntityType<? extends AbstractEntityDrone> getEntityType() {
        return registerManager.ENTITYTYPE_MISSILEDRONE;
    }

    @Override
    public int getreloadDelay() {
        return 200;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    private PlayState predicate(AnimationEvent animationEvent) {
        return PlayState.CONTINUE;
    }



    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(group == this.getGroup()) {
            ItemStack stack = new ItemStack(this);
            ItemStackUtils.setCurrentHP(stack, this.getMaxHP());
            ItemStackUtils.setAmmoFull(stack);
            items.add(stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent(ItemStackUtils.getRemainingAmmo(stack)+"/"+this.getMaxAmmo()));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
