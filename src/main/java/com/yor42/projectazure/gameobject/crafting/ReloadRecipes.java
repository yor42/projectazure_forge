package com.yor42.projectazure.gameobject.crafting;

import com.google.common.collect.Lists;
import com.yor42.projectazure.gameobject.items.ItemAmmo;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.addAmmo;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;
import static com.yor42.projectazure.setup.register.registerRecipes.Serializers.RELOADING;

public class ReloadRecipes extends SpecialRecipe {
    public ReloadRecipes(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {

        List<ItemStack> AmmoList = Lists.newArrayList();
        ItemStack ReloadTarget = ItemStack.EMPTY;
        ICraftingTableReloadable ReloadTargetItem = null;
        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                if(itemstack.getItem() instanceof ICraftingTableReloadable){
                    ReloadTarget = itemstack;
                    ReloadTargetItem = (ICraftingTableReloadable) itemstack.getItem();
                    break;
                }
            }
        }

        int remaingAmmo = getRemainingAmmo(ReloadTarget);
        //No Reloadable Item
        if(ReloadTarget == ItemStack.EMPTY || remaingAmmo<0 || ReloadTargetItem.getMaxAmmo() - remaingAmmo<=0){
            return false;
        }
        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                if(itemstack.getItem() instanceof ItemAmmo){
                    if(((ItemAmmo) itemstack.getItem()).getCalibur() == ReloadTargetItem.getCalibur()){
                        AmmoList.add(itemstack);
                    }
                }
            }
        }

        return !AmmoList.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        List<ItemStack> AmmoList = Lists.newArrayList();
        ItemStack ReloadTarget = ItemStack.EMPTY;
        ICraftingTableReloadable ReloadTargetItem = null;
        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                if(itemstack.getItem() instanceof ICraftingTableReloadable){
                    ReloadTarget = itemstack;
                    ReloadTargetItem = (ICraftingTableReloadable) itemstack.getItem();
                    break;
                }
            }
        }
        int remaingAmmo = getRemainingAmmo(ReloadTarget);
        //No Reloadable Item or Magazine is Full
        if(ReloadTarget == ItemStack.EMPTY || remaingAmmo<0 || ReloadTargetItem.getMaxAmmo() - remaingAmmo<=0){
            return ItemStack.EMPTY;
        }
        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                if(itemstack.getItem() instanceof ItemAmmo){
                    if(((ItemAmmo) itemstack.getItem()).getCalibur() == ReloadTargetItem.getCalibur()){
                        AmmoList.add(itemstack);
                    }
                }
            }
        }

        ItemStack stackToReturn = ReloadTarget.copy();

        int AmmoToAdd = 0;
        for (ItemStack stack : AmmoList) {
            if (stack.getItem() instanceof ItemAmmo) {
                int ammocount = ((ItemAmmo) stack.getItem()).getAmmoCount()*stack.getCount();
                AmmoToAdd = AmmoToAdd+ammocount;
            }
        }

        return addAmmo(stackToReturn, AmmoToAdd);
    }

    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RELOADING.get();
    }
}
