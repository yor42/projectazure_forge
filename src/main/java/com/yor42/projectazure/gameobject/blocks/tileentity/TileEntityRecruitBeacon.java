package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.blocks.AlloyFurnaceBlock;
import com.yor42.projectazure.gameobject.blocks.RecruitBeaconBlock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerRecruitBeacon;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerManager;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.MathUtil.getRandomBlockposInRadius2D;

public class TileEntityRecruitBeacon extends AbstractTileEntityGacha {
    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TileEntityRecruitBeacon.this.ProcessTime;
                case 1:
                    return TileEntityRecruitBeacon.this.totalProcessTime;
                case 2:
                    return TileEntityRecruitBeacon.this.energyStorage.getEnergyStored();
                case 3:
                    return TileEntityRecruitBeacon.this.energyStorage.getMaxEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    TileEntityRecruitBeacon.this.ProcessTime = value;
                    break;
                case 1:
                    TileEntityRecruitBeacon.this.totalProcessTime = value;
                    break;
                case 2:
                    TileEntityRecruitBeacon.this.energyStorage.setEnergy(value);
                    break;
                case 3:
                    TileEntityRecruitBeacon.this.energyStorage.setMaxEnergy(value);
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    @Override
    public void tick() {
        boolean isPowered = this.isPowered();
        boolean isActive = this.isActive();
        boolean shouldSave = false;
        super.tick();
        if(!(this.world != null && this.world.isRemote)){
            if(isPowered!=this.isPowered()){
                shouldSave = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(RecruitBeaconBlock.POWERED, this.isPowered()), 2);
            }
            if(isActive!=this.isActive()){
                shouldSave = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(RecruitBeaconBlock.ACTIVE, this.isActive()), 2);
            }
        }

        if(shouldSave){this.markDirty();}
    }

    public TileEntityRecruitBeacon() {
        super(registerTE.RECRUIT_BEACON.get());
        this.inventory.setSize(5);
        this.powerConsumption = 1000;
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
                    entityCompanion.setTamedBy(this.nextTaskOwner);
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
        this.inventory.getStackInSlot(0).shrink(1);
        for (int i=1; i<5; i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public boolean isPowered(){
        return this.getEnergyStorage().getEnergyStored()>=this.getPowerConsumption();
    }

    @Override
    public void registerRollEntry() {
        addEntry(registerManager.ENTITYTYPE_CHEN);
    }

    public IIntArray getFields(){
        return this.fields;
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
        return new ContainerRecruitBeacon(id, player, this.inventory, this.getFields());
    }
}
