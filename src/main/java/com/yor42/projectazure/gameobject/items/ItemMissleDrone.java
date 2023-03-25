package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityFollowingDrone;
import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class ItemMissleDrone extends AbstractItemPlaceableDrone implements ICraftingTableReloadable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public ItemMissleDrone(Properties properties, int MaxHP, int maxFuelmb) {
        super(properties, MaxHP, 8, maxFuelmb);
    }

    @Override
    public EntityType<? extends AbstractEntityFollowingDrone> getEntityType() {
        return registerEntity.MISSILEDRONE.get();
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
    public int getRepairAmount(ItemStack candidateItem) {
        if(candidateItem.getItem() == RegisterItems.PLATE_STEEL.get()){
            return 2;
        }
        else if(candidateItem.getItem() == RegisterItems.ADVANCED_CIRCUIT.get()){
            return 5;
        }
        return super.getRepairAmount(candidateItem);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int ammo = ItemStackUtils.getRemainingAmmo(stack);
        float ammopercent = (float) ammo/this.getMaxAmmo();
        ChatFormatting color = ammopercent>=0.33? ammopercent>= 0.66? ChatFormatting.DARK_GREEN:ChatFormatting.GOLD:ChatFormatting.DARK_RED;
        tooltip.add(new TranslatableComponent("item.tooltip.remainingammo").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(ammo+"/"+this.getMaxAmmo()).withStyle(color)));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
