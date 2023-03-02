package com.yor42.projectazure.gameobject.crafting.recipes;

import com.yor42.projectazure.gameobject.items.tools.ItemSyringe;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.TRANSFERPOTION;

public class TransferPotiontoSyringeRecipe extends SpecialRecipe {

    private ItemStack syringetofill = ItemStack.EMPTY;
    private ItemStack targetpotion = ItemStack.EMPTY;
    private int potionindex = -1;

    public TransferPotiontoSyringeRecipe(ResourceLocation p_i48169_1_) {
        super(p_i48169_1_);
    }

    @Override
    public boolean matches(CraftingInventory inv, World p_77569_2_) {
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
    public ItemStack assemble(CraftingInventory inv) {
        ItemStack syringe = this.syringetofill.copy();
        PotionUtils.setPotion(syringe, PotionUtils.getPotion(this.targetpotion));
        return syringe;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ * p_194133_2_ >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return TRANSFERPOTION.get();
    }
}
