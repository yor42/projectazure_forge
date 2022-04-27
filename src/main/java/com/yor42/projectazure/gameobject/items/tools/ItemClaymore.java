package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.client.renderer.items.ItemClaymoreRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.MobEffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ChatFormatting;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.Level;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.Main.PA_WEAPONS;
import static com.yor42.projectazure.gameobject.items.materials.ModMaterials.CLAYMORE;

public class ItemClaymore extends SwordItem implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);

    public ItemClaymore() {
        //Haha unstackable claymore
        super(CLAYMORE,1,-3.9F,new Item.Properties().stacksTo(1).tab(PA_WEAPONS).setISTER(()->ItemClaymoreRenderer::new));
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_77624_2_, List<Component> tooltip, ITooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, tooltip, p_77624_4_);
        tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level p_77663_2_, @Nonnull Entity entityIn, int p_77663_4_, boolean isSelected) {
        super.inventoryTick(stack, p_77663_2_, entityIn, p_77663_4_, isSelected);
        if(entityIn instanceof PlayerEntity){
            int claymorecount = 0;
            for(int i = 0; i<((PlayerEntity) entityIn).inventory.getContainerSize(); i++){
                if(((PlayerEntity) entityIn).inventory.getItem(i).getItem() == this && ((PlayerEntity) entityIn).inventory.getItem(i) != stack){
                    claymorecount+=1;
                }
            }
            int level = Math.min(claymorecount+(isSelected?2:1), 10);
            ((PlayerEntity) entityIn).addEffect(new MobEffectInstance(Effects.MOVEMENT_SLOWDOWN, 1, level, true, false));
        }
    }
}
