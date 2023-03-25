package com.yor42.projectazure.gameobject.crafting.recipes;

import com.yor42.projectazure.interfaces.IItemDestroyable;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;
import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.REPAIRING;

public class RepairRecipe extends CustomRecipe {

    private int repairvalue = 0;
    private ItemStack RepairTarget = ItemStack.EMPTY;

    public RepairRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        IItemDestroyable RepairTargetItem = null;
        this.RepairTarget = ItemStack.EMPTY;
        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack = inv.getItem(i);
            if(itemstack.getItem() instanceof IItemDestroyable){
                this.RepairTarget = itemstack;
                RepairTargetItem = (IItemDestroyable) itemstack.getItem();
                break;
            }
        }

        int remaingHP = getCurrentHP(this.RepairTarget);
        //No Reloadable Item
        if(this.RepairTarget == ItemStack.EMPTY || RepairTargetItem == null || remaingHP<0 || RepairTargetItem.getMaxHP() <= remaingHP){
            return false;
        }

        if(this.repairvalue>0){
            this.repairvalue = 0;
        }

        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack = inv.getItem(i);
            if (!itemstack.isEmpty()) {
                this.repairvalue+=RepairTargetItem.getRepairAmount(itemstack);
                if(this.repairvalue>=RepairTargetItem.getMaxHP()){
                    break;
                }
            }
        }

        return this.repairvalue>0;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {

        ItemStack OutputStack = this.RepairTarget.copy();
        if(this.repairvalue>0) {
            ItemStackUtils.RepairItem(OutputStack, this.repairvalue);
        }

        return OutputStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return REPAIRING.get();
    }
}
