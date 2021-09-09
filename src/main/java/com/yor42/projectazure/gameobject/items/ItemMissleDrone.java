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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int ammo = ItemStackUtils.getRemainingAmmo(stack);
        float ammopercent = (float) ammo/this.getMaxAmmo();
        TextFormatting color = ammopercent>=0.33? ammopercent>= 0.66? TextFormatting.DARK_GREEN:TextFormatting.GOLD:TextFormatting.DARK_RED;
        tooltip.add(new TranslationTextComponent("item.tooltip.remainingammo").appendString(": ").mergeStyle(TextFormatting.GRAY).append(new StringTextComponent(ammo+"/"+this.getMaxAmmo()).mergeStyle(color)));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
