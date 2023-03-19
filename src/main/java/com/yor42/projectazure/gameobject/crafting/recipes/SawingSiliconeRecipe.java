package com.yor42.projectazure.gameobject.crafting.recipes;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.items.ItemCraftTool;
import com.yor42.projectazure.gameobject.items.tools.ItemSyringe;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.SAW_SILICON;

public class SawingSiliconeRecipe extends SpecialRecipe {

    private ItemStack silicone = ItemStack.EMPTY;
    private ItemStack saw = ItemStack.EMPTY;
    private int sawtier =-1;

    public SawingSiliconeRecipe(ResourceLocation pId) {
        super(pId);
    }

    @Override
    public boolean matches(CraftingInventory inv, World pLevel) {
        this.silicone=ItemStack.EMPTY;
        this.saw = ItemStack.EMPTY;
        for(int i=0;i<inv.getContainerSize(); i++){
            ItemStack stack  = inv.getItem(i);
            Item item = stack.getItem();
            if(this.silicone.isEmpty() && stack.getItem() == RegisterItems.SILICONE.get()){
                this.silicone = stack;
            }
            else if(item instanceof ItemCraftTool && ModTags.Items.SAW.contains(item) && ((ItemCraftTool) item).getTier()>=2){
                this.saw =stack;
                this.sawtier = i;
            }
        }

        return !this.saw.isEmpty() && !this.silicone.isEmpty() && this.sawtier>=0;

    }

    @Override
    public ItemStack assemble(CraftingInventory pInv) {
        return new ItemStack(RegisterItems.SILICONE_WAFER.get(), (this.sawtier*4));
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth*pHeight>=2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SAW_SILICON.get();
    }
}