package com.yor42.projectazure.gameobject.blocks.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class TileEntitySiliconeCrucible extends AbstractAnimatedTileEntityMachines{
    protected TileEntitySiliconeCrucible(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    @Override
    protected <P extends TileEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    protected void playsound() {

    }

    @Override
    public void encodeExtraData(PacketBuffer buffer) {

    }

    @Override
    protected int getTargetProcessTime() {
        return 600;
    }

    @Override
    protected void process(IRecipe<?> irecipe) {

    }

    @Override
    protected boolean canProcess(IRecipe<?> irecipe) {
        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.projectazure.silicone_crucible");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }
}
