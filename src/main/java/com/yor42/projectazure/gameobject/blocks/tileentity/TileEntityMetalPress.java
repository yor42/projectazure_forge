package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.containers.machine.ContainerMetalPress;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import com.yor42.projectazure.setup.register.registerRecipes;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.entity.player.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;

import static com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock.ACTIVE;

public class TileEntityMetalPress extends AbstractAnimatedTileEntityMachines {

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
        public int getCount() {
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
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index == 0){
            return true;
        }
        else if(index == 1){
            return stack.getItem().is(ModTags.Items.EXTRUSION_MOLD);
        }
        else{
            return false;
        }
    }

    private final LazyOptional<ItemStackHandler> Invhandler = LazyOptional.of(this::getInventory);
    private final LazyOptional<CustomEnergyStorage> Energyhandler = LazyOptional.of(this::getEnergyStorage);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return Invhandler.cast();
        }
        else if(capability == CapabilityEnergy.ENERGY)
            return Energyhandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("metal_press");
    }

    @Override
    protected Container createMenu(int id, Inventory player) {
        return new ContainerMetalPress(id, player, this.inventory, this.fields);
    }

    @Override
    protected <P extends BlockEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();
        event.getController().transitionLengthTicks = 0;
        boolean flag = this.getLevel().getBlockState(this.getBlockPos()).hasProperty(ACTIVE) && this.getLevel().getBlockState(this.getBlockPos()).getValue(ACTIVE);
        if(flag) {
            event.getController().setAnimation(builder.addAnimation("work", true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    @Override
    protected void playsound() {
    }

    protected int getTargetProcessTime(){
        return this.level.getRecipeManager().getRecipeFor((IRecipeType<? extends PressingRecipe>)this.recipeType, this, this.level).map(PressingRecipe::getProcessTick).orElse(200);
    }

    protected void process(IRecipe<?> irecipe) {

        if(irecipe != null && this.canProcess(irecipe)){
            ItemStack ingredient = this.inventory.getStackInSlot(0);
            ItemStack mold = this.inventory.getStackInSlot(1);
            ItemStack output = irecipe.getResultItem();
            ItemStack outputslot = this.inventory.getStackInSlot(2);

            if (outputslot.isEmpty()) {
                this.inventory.setStackInSlot(2, output.copy());
            } else if (outputslot.getItem() == outputslot.getItem()) {
                outputslot.grow(output.getCount());
            }

            if (!(this.getLevel() != null && this.getLevel().isClientSide())) {
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
            ItemStack itemstack = recipeIn.getResultItem();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.inventory.getStackInSlot(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.sameItem(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getMaxStackSize() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
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
