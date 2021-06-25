package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.blocks.RecruitBeaconBlock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerRecruitBeacon;
import com.yor42.projectazure.gameobject.energy.CustomEnergyStorage;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.registerManager;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;

import static com.yor42.projectazure.gameobject.blocks.RecruitBeaconBlock.POWERED;
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
                case 4:
                    return TileEntityRecruitBeacon.this.getPos().getX();
                case 5:
                    return TileEntityRecruitBeacon.this.getPos().getY();
                case 6:
                    return TileEntityRecruitBeacon.this.getPos().getZ();
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
            return 7;
        }
    };

    @Override
    public void tick() {
        boolean isPowered = this.isPowered();
        boolean isActive = this.isActive();
        boolean shouldSave = false;
        super.tick();
        if(!(this.world != null && this.world.isRemote)){
            if(isPowered!=this.isPowered() || this.isPowered() && !this.world.getBlockState(this.pos).get(POWERED) || !this.isPowered() && this.world.getBlockState(this.pos).get(POWERED)) {
                shouldSave = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(POWERED, this.isPowered()), 2);
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
    protected void SpawnResultEntity(ServerPlayerEntity owner) {

        boolean worldReady = this.world != null && !this.world.isRemote();
        boolean EntityTypeNotNull = this.RollResult != null;

        if(!EntityTypeNotNull){
            Main.LOGGER.error("Spawn FAILED: EntityType is NULL");
        }
        else if(worldReady) {
            BlockPos pos = getRandomBlockposInRadius2D(this.getWorld(), this.getPos(), 20, 10);
            Main.LOGGER.debug("Entity is Spawned at:" + pos.toString());
            AbstractEntityCompanion entityCompanion = this.RollResult.create(this.world);
            if (entityCompanion != null) {
                entityCompanion.setPosition(pos.getX(), pos.getY(), pos.getZ());
                entityCompanion.getNavigator().tryMoveToXYZ((double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 1.0F);
                entityCompanion.setMovingtoRecruitStation(this.getPos());
                entityCompanion.setTamedBy(owner);
                entityCompanion.setMorale(150);
                this.world.addEntity(entityCompanion);
            }

        }
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
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
        int[] var = {this.ProcessTime, this.totalProcessTime, this.energyStorage.getEnergyStored(), this.energyStorage.getMaxEnergyStored(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()};
        buffer.writeVarIntArray(var);
    }

    @Override
    protected double getResourceChanceBonus() {
        return 0;
    }

    @Override
    protected boolean canStartProcess() {

        int OrundumCount = this.inventory.getStackInSlot(1).getCount()+this.inventory.getStackInSlot(2).getCount();
        int GoldIngotCount = this.inventory.getStackInSlot(3).getCount()+this.inventory.getStackInSlot(4).getCount();

        return this.inventory.getStackInSlot(0) != ItemStack.EMPTY && OrundumCount>40? GoldIngotCount>=20:GoldIngotCount>60 && OrundumCount>=10;
    }

    @Override
    protected void UseGivenResource() {
        this.inventory.getStackInSlot(0).shrink(1);
        for (int i=1; i<5; i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void registerRollEntry() {
        addEntry(registerManager.ENTITYTYPE_CHEN);
        addEntry(registerManager.SHIROKO_ENTITY_TYPE);
        if(PAConfig.CONFIG.shouldRecruitBeaconSpawnAllCompanions.get()){
            addEntry(registerManager.ENTERPRISE_ENTITY_TYPE);
            addEntry(registerManager.ENTITYAYANAMI);
            addEntry(registerManager.ENTITYGANGWON);
            addEntry(registerManager.ENTITYTYPE_NAGATO);
        }
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
