package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.blocks.MetalPressBlock;
import com.yor42.projectazure.gameobject.containers.ContainerMetalPress;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.gameobject.energy.CustomEnergyStorage;
import com.yor42.projectazure.setup.register.registerRecipes;
import com.yor42.projectazure.setup.register.registerTE;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class TileEntityMetalPress extends AbstractTileEntityMachines {

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TileEntityMetalPress.this.ProcessTime;
                case 1:
                    return TileEntityMetalPress.this.totalProcessTime;
                case 2:
                    return TileEntityMetalPress.this.energyStorage.getEnergyStored();
                case 3:
                    return TileEntityMetalPress.this.energyStorage.getMaxEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    TileEntityMetalPress.this.ProcessTime = value;
                    break;
                case 1:
                    TileEntityMetalPress.this.totalProcessTime = value;
                    break;
                case 2:
                    TileEntityMetalPress.this.energyStorage.setEnergy(value);
                    break;
                case 3:
                    TileEntityMetalPress.this.energyStorage.setMaxEnergy(value);
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public TileEntityMetalPress() {
        super(registerTE.METAL_PRESS.get());
        this.recipeType = registerRecipes.Types.PRESSING;
        this.powerConsumption = 100;
        this.inventory.setSize(3);
        this.energyStorage.setMaxEnergy(15000);
    }

    public void encodeExtraData(PacketBuffer buffer){
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0){
            return true;
        }
        else if(index == 1){
            return stack.getItem().isIn(ModTags.Items.EXTRUSION_MOLD);
        }
        else{
            return false;
        }
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("metal_press");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ContainerMetalPress(id, player, this.inventory, this.fields);
    }

    @Override
    protected <P extends TileEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();
        event.getController().transitionLengthTicks = 0;
        if(event.getAnimatable().getTileEntity() instanceof TileEntityMetalPress ){
            boolean flag = ((TileEntityMetalPress) event.getAnimatable().getTileEntity()).getProcessTime()>0;
            if(flag) {
                event.getController().setAnimation(builder.addAnimation("work", true));
                return PlayState.CONTINUE;
            }
        }

        return PlayState.STOP;
    }

    protected int getTargetProcessTime(){
        return this.world.getRecipeManager().getRecipe((IRecipeType<? extends PressingRecipe>)this.recipeType, this, this.world).map(PressingRecipe::getProcessTick).orElse(200);
    }

    protected void process(IRecipe<?> irecipe) {

        if(irecipe != null && this.canProcess(irecipe)){
            ItemStack ingredient = this.inventory.getStackInSlot(0);
            ItemStack mold = this.inventory.getStackInSlot(1);
            ItemStack output = irecipe.getRecipeOutput();
            ItemStack outputslot = this.inventory.getStackInSlot(2);

            if (outputslot.isEmpty()) {
                this.inventory.setStackInSlot(2, output.copy());
            } else if (outputslot.getItem() == outputslot.getItem()) {
                outputslot.grow(output.getCount());
            }

            if (!(this.world != null && this.world.isRemote)) {
                this.setRecipeUsed(irecipe);
            }

            if (ingredient.hasContainerItem()) {
                this.inventory.setStackInSlot(1, ingredient.getContainerItem());
            } else if (!ingredient.isEmpty()) {
                ingredient.shrink(1);
                if (ingredient.isEmpty()) {
                    this.inventory.setStackInSlot(1, ingredient.getContainerItem());
                }
            }

            if (mold.hasContainerItem()) {
                this.inventory.setStackInSlot(1, mold.getContainerItem());
            } else if (!mold.isEmpty()) {
                mold.shrink(1);
                if (mold.isEmpty()) {
                    this.inventory.setStackInSlot(1, mold.getContainerItem());
                }
            }
            
        }
    }

    protected boolean canProcess(@Nullable IRecipe<?> recipeIn) {
        if (!this.inventory.getStackInSlot(0).isEmpty() && !this.inventory.getStackInSlot(1).isEmpty() && recipeIn != null) {
            ItemStack itemstack = recipeIn.getRecipeOutput();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.inventory.getStackInSlot(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isActive() {
        return this.fields.get(0)>0;
    }
}
