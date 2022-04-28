package com.yor42.projectazure.gameobject.blocks.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class TileEntitySiliconeCrucible extends AbstractAnimatedTileEntityMachines implements MenuProvider {

    protected TileEntitySiliconeCrucible(BlockEntityType<?> typeIn, BlockPos p_155229_, BlockState p_155230_) {
        super(typeIn, p_155229_, p_155230_);
    }

    @Override
    protected <P extends BlockEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    protected void playsound() {

    }

    @Override
    public void encodeExtraData(FriendlyByteBuf  buffer) {

    }

    @Override
    protected int getTargetProcessTime() {
        return 600;
    }

    @Override
    protected void process(Recipe<?> irecipe) {

    }

    @Override
    protected boolean canProcess(Recipe<?> irecipe) {
        return false;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.projectazure.silicone_crucible");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player playerentity) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int p_18941_) {
        return null;
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return null;
    }
}
