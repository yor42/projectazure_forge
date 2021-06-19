package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRandomBlockposInRadius2D;

public class TileEntityRecruitBeacon extends AbstractTileEntityGacha {

    protected TileEntityRecruitBeacon(TileEntityType<?> typeIn) {
        super(typeIn);
        this.inventory.setSize(5);
    }

    @Override
    protected void SpawnResultEntity() {
        if(this.world != null && this.world.isRemote() && this.entityCompanion != null) {
            BlockPos pos = getRandomBlockposInRadius2D(this.getWorld(), this.getPos(), 40, 5);
            if(pos != null) {
                AbstractEntityCompanion entityCompanion = this.entityCompanion.create(this.world);
                if (entityCompanion != null) {
                    entityCompanion.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    entityCompanion.setMovingtoRecruitStation(pos);
                    this.world.addEntity(entityCompanion);
                }
            }
        }
    }

    @Override
    protected boolean canProcess() {
        return !this.inventory.getStackInSlot(0).isEmpty();
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
    protected double getResourceChanceBonus() {
        return 0;
    }

    @Override
    protected void UseGivenResource() {

    }

    @Override
    public void registerRollEntry() {
        addEntry(registerManager.ENTITYTYPE_CHEN);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("recruit_beacon");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }
}
