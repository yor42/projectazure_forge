package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityFollowingDrone;
import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
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
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int ammo = ItemStackUtils.getRemainingAmmo(stack);
        float ammopercent = (float) ammo/this.getMaxAmmo();
        TextFormatting color = ammopercent>=0.33? ammopercent>= 0.66? TextFormatting.DARK_GREEN:TextFormatting.GOLD:TextFormatting.DARK_RED;
        tooltip.add(new TranslationTextComponent("item.tooltip.remainingammo").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(ammo+"/"+this.getMaxAmmo()).withStyle(color)));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
