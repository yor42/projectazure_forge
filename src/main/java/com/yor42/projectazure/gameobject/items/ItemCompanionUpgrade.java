package com.yor42.projectazure.gameobject.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCompanionUpgrade extends Item {
    public ItemCompanionUpgrade(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack p_77636_1_) {
        return true;
    }

    @Nonnull
    @Override
    public Component getName(ItemStack p_200295_1_) {

        if(p_200295_1_.getOrCreateTag().contains("vaildentity")){
            return new TranslatableComponent(this.getDescriptionId(p_200295_1_)+"_with_validentity", new TranslatableComponent(p_200295_1_.getOrCreateTag().getString("vaildentity")));
        }

        return super.getName(p_200295_1_);


    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level p_77624_2_, @Nonnull List<Component> p_77624_3_, @Nonnull TooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, p_77624_3_, p_77624_4_);

        if(stack.getOrCreateTag().contains("vaildentity")){
            String indiv_token_explanation = this.getDescriptionId(stack)+".tooltip."+stack.getOrCreateTag().getString("vaildentity");
            if(I18n.exists(indiv_token_explanation+"_0")){
                int i = 0;
                while(I18n.exists(indiv_token_explanation+"_"+i)){
                    p_77624_3_.add(new TranslatableComponent(indiv_token_explanation+"_"+i).withStyle(ChatFormatting.GRAY));
                }
            }
            else {
                p_77624_3_.add(new TranslatableComponent(this.getDescriptionId(stack) + ".tooltip.with_validentity", new TranslatableComponent(stack.getOrCreateTag().getString("vaildentity"))).withStyle(ChatFormatting.GRAY));
            }
        }
        else{
            p_77624_3_.add(new TranslatableComponent(this.getDescriptionId(stack)+".tooltip", stack.getOrCreateTag().getString("vaildentity")).withStyle(ChatFormatting.GRAY));
        }
    }
}
