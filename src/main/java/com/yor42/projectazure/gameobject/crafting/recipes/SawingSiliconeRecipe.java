package com.yor42.projectazure.gameobject.crafting.recipes;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.items.ItemCraftTool;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.SAW_SILICON;

public class SawingSiliconeRecipe extends CustomRecipe {

    private ItemStack silicone = ItemStack.EMPTY;
    private ItemStack saw = ItemStack.EMPTY;
    private int sawtier =-1;

    public SawingSiliconeRecipe(ResourceLocation pId) {
        super(pId);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level pLevel) {
        this.silicone=ItemStack.EMPTY;
        this.saw = ItemStack.EMPTY;
        for(int i=0;i<inv.getContainerSize(); i++){
            ItemStack stack  = inv.getItem(i);
            Item item = stack.getItem();
            if(this.silicone.isEmpty() && stack.getItem() == RegisterItems.SILICONE.get()){
                this.silicone = stack;
            }
            else if(item instanceof ItemCraftTool && stack.is(ModTags.Items.SAW) && ((ItemCraftTool) item).getTier()>=2){
                this.saw =stack;
                this.sawtier = i;
            }
        }

        return !this.saw.isEmpty() && !this.silicone.isEmpty() && this.sawtier>=0;

    }

    @Override
    public ItemStack assemble(CraftingContainer pInv) {
        return new ItemStack(RegisterItems.SILICONE_WAFER.get(), (this.sawtier*4));
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth*pHeight>=2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SAW_SILICON.get();
    }
}
