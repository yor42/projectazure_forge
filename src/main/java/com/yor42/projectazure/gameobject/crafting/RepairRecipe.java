package com.yor42.projectazure.gameobject.crafting;

import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.interfaces.IItemDestroyable;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;
import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.REPAIRING;

public class RepairRecipe extends SpecialRecipe {

    private int repairvalue = 0;
    private ItemStack RepairTarget = ItemStack.EMPTY;

    public RepairRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        IItemDestroyable ReloadTargetItem = null;
        this.RepairTarget = ItemStack.EMPTY;
        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                if(itemstack.getItem() instanceof IItemDestroyable){
                    this.RepairTarget = itemstack;
                    ReloadTargetItem = (IItemDestroyable) itemstack.getItem();
                    break;
                }
            }
        }

        int remaingHP = getCurrentHP(this.RepairTarget);
        //No Reloadable Item
        if(this.RepairTarget == ItemStack.EMPTY || ReloadTargetItem == null || remaingHP<0 || ReloadTargetItem.getMaxHP() <= remaingHP){
            return false;
        }

        if(this.repairvalue>0){
            this.repairvalue = 0;
        }

        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                this.repairvalue+=ReloadTargetItem.getRepairAmount(itemstack);
            }
        }

        return this.repairvalue>0;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {

        ItemStack OutputStack = this.RepairTarget.copy();
        if(this.repairvalue>0) {
            ItemStackUtils.RepairItem(OutputStack, this.repairvalue);
        }

        return OutputStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return REPAIRING.get();
    }
}
