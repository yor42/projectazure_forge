package com.yor42.projectazure.gameobject.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
    public ITextComponent getName(ItemStack p_200295_1_) {

        if(p_200295_1_.getOrCreateTag().contains("vaildentity")){
            return new TranslationTextComponent(this.getDescriptionId(p_200295_1_)+"_with_validentity", new TranslationTextComponent(p_200295_1_.getOrCreateTag().getString("vaildentity")));
        }

        return super.getName(p_200295_1_);


    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World p_77624_2_, @Nonnull List<ITextComponent> p_77624_3_, @Nonnull ITooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, p_77624_3_, p_77624_4_);

        if(stack.getOrCreateTag().contains("vaildentity")){
            String indiv_token_explanation = this.getDescriptionId(stack)+".tooltip."+stack.getOrCreateTag().getString("vaildentity");
            if(I18n.exists(indiv_token_explanation+"_0")){
                int i = 0;
                while(I18n.exists(indiv_token_explanation+"_"+i)){
                    p_77624_3_.add(new TranslationTextComponent(indiv_token_explanation+"_"+i).withStyle(TextFormatting.GRAY));
                }
            }
            else {
                p_77624_3_.add(new TranslationTextComponent(this.getDescriptionId(stack) + ".tooltip.with_validentity", new TranslationTextComponent(stack.getOrCreateTag().getString("vaildentity"))).withStyle(TextFormatting.GRAY));
            }
        }
        else{
            p_77624_3_.add(new TranslationTextComponent(this.getDescriptionId(stack)+".tooltip", stack.getOrCreateTag().getString("vaildentity")).withStyle(TextFormatting.GRAY));
        }
    }
}
