package com.yor42.projectazure.gameobject.crafting.recipes;

import com.google.common.collect.Lists;
import com.yor42.projectazure.gameobject.items.ItemAmmo;
import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.addAmmo;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;
import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.RELOADING;

public class ReloadRecipes extends SpecialRecipe {

    private final List<ItemStack> AmmoList = Lists.newArrayList();
    private ItemStack ReloadTarget;

    public ReloadRecipes(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        this.ReloadTarget = ItemStack.EMPTY;
        ICraftingTableReloadable ReloadTargetItem = null;
        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack = inv.getItem(i);
            if (itemstack.getItem() instanceof ICraftingTableReloadable) {
                this.ReloadTarget = itemstack;
                ReloadTargetItem = (ICraftingTableReloadable) itemstack.getItem();
                break;
            }
        }

        int remaingAmmo = getRemainingAmmo(this.ReloadTarget);
        //No Reloadable Item
        if(this.ReloadTarget == ItemStack.EMPTY || remaingAmmo<0 || ReloadTargetItem.getMaxAmmo() - remaingAmmo<=0){
            return false;
        }

        if(!this.AmmoList.isEmpty()){
            this.AmmoList.clear();
        }

        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack = inv.getItem(i);
            if (itemstack.getItem() instanceof ItemAmmo) {
                if (((ItemAmmo) itemstack.getItem()).getCalibur() == ReloadTargetItem.getAmmoType()) {
                    this.AmmoList.add(itemstack);
                }
            }
        }

        return !this.AmmoList.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        ItemStack stackToReturn = this.ReloadTarget.copy();
        int AmmoToAdd = 0;
        for (ItemStack stack : this.AmmoList) {
            if (stack.getItem() instanceof ItemAmmo) {
                int ammocount = ((ItemAmmo) stack.getItem()).getAmmoCount()*stack.getCount();
                AmmoToAdd = AmmoToAdd+ammocount;
            }
        }
        return addAmmo(stackToReturn, AmmoToAdd);
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RELOADING.get();
    }
}
