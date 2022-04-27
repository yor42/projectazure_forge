package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.client.renderer.items.ItemClaymoreRenderer;
import com.yor42.projectazure.client.renderer.items.ItemDefibChargerRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.yor42.projectazure.Main.PA_WEAPONS;
import static com.yor42.projectazure.gameobject.items.materials.ModMaterials.CLAYMORE;

public class ItemClaymore extends SwordItem implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);

    public ItemClaymore() {
        //Haha unstackable claymore
        super(CLAYMORE,1,-3.9F,new Item.Properties().stacksTo(1));
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new ItemClaymoreRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_77624_2_, List<Component> tooltip, TooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, tooltip, p_77624_4_);
        tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level p_77663_2_, @Nonnull Entity entityIn, int p_77663_4_, boolean isSelected) {
        super.inventoryTick(stack, p_77663_2_, entityIn, p_77663_4_, isSelected);
        if(entityIn instanceof Player){
            int claymorecount = 0;
            for(int i = 0; i<((Player) entityIn).getInventory().getContainerSize(); i++){
                if(((Player) entityIn).getInventory().getItem(i).getItem() == this && ((Player) entityIn).getInventory().getItem(i) != stack){
                    claymorecount+=1;
                }
            }
            int level = Math.min(claymorecount+(isSelected?2:1), 10);
            ((Player) entityIn).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, level, true, false));
        }
    }
}
