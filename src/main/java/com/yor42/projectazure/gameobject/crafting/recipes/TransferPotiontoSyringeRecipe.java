package com.yor42.projectazure.gameobject.crafting.recipes;

import com.yor42.projectazure.gameobject.items.tools.ItemSyringe;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.TRANSFERPOTION;

public class TransferPotiontoSyringeRecipe extends CustomRecipe {

    private ItemStack syringetofill = ItemStack.EMPTY;
    private ItemStack targetpotion = ItemStack.EMPTY;
    private int potionindex = -1;

    public TransferPotiontoSyringeRecipe(ResourceLocation p_i48169_1_) {
        super(p_i48169_1_);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level p_77569_2_) {
        this.syringetofill=ItemStack.EMPTY;
        this.targetpotion = ItemStack.EMPTY;
        for(int i=0;i<inv.getContainerSize(); i++){
            ItemStack stack  = inv.getItem(i);
            if(this.syringetofill.isEmpty() && stack.getItem() instanceof ItemSyringe && PotionUtils.getPotion(stack)== Potions.EMPTY && !ItemStackUtils.isDestroyed(stack)){
                this.syringetofill = stack;
            }
            else if(stack.getItem().getClass() == PotionItem.class && PotionUtils.getPotion(stack) != Potions.EMPTY){
                if(!this.targetpotion.isEmpty()){
                    return false;
                }
                this.targetpotion=stack;
                this.potionindex = i;
            }
        }

        return !this.targetpotion.isEmpty() && !this.syringetofill.isEmpty() && this.potionindex>=0;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack syringe = this.syringetofill.copy();
        PotionUtils.setPotion(syringe, PotionUtils.getPotion(this.targetpotion));
        return syringe;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ * p_194133_2_ >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TRANSFERPOTION.get();
    }
}
