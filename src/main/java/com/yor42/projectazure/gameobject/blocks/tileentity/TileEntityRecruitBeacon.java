package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.blocks.machines.RecruitBeaconBlock;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.containers.machine.ContainerRecruitBeacon;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import com.yor42.projectazure.intermod.SolarApocalypse;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerEntity;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;
import java.util.Random;

import static com.yor42.projectazure.gameobject.blocks.AbstractElectricMachineBlock.FACING;
import static com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock.ACTIVE;
import static com.yor42.projectazure.gameobject.blocks.machines.RecruitBeaconBlock.POWERED;
import static com.yor42.projectazure.libs.utils.MathUtil.getRandomBlockposInRadius2D;

public class TileEntityRecruitBeacon extends AbstractTileEntityGacha {
    private final ContainerData fields = new ContainerData() {
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
                    return TileEntityRecruitBeacon.this.getBlockPos().getX();
                case 5:
                    return TileEntityRecruitBeacon.this.getBlockPos().getY();
                case 6:
                    return TileEntityRecruitBeacon.this.getBlockPos().getZ();
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
        public int getCount() {
            return 7;
        }
    };

    @Override
    public void tick() {
        boolean isPowered = this.isPowered();
        boolean isActive = this.isActive();
        boolean shouldSave = false;
        super.tick();
        if(!(this.level != null && this.level.isClientSide)){
            if(isPowered!=this.isPowered() || this.isPowered() && !this.level.getBlockState(this.worldPosition).getValue(POWERED) || !this.isPowered() && this.level.getBlockState(this.worldPosition).getValue(POWERED)) {
                shouldSave = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(POWERED, this.isPowered()), 2);
            }
            boolean flag = this.getLevel().getBlockState(this.getBlockPos()).hasProperty(ACTIVE) && this.getLevel().getBlockState(this.getBlockPos()).getValue(ACTIVE);
            if(flag){
                shouldSave = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(RecruitBeaconBlock.ACTIVE, this.isActive()), 2);
            }
        }

        if(shouldSave){this.setChanged();}
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
    protected void SpawnResultEntity(ServerPlayer owner) {

        boolean worldReady = this.level != null && !this.level.isClientSide();
        boolean EntityTypeNotNull = this.RollResult != null;
        boolean spawn_sitting = true;

        if(!EntityTypeNotNull){
            Main.LOGGER.error("Spawn FAILED: EntityType is NULL");
        }
        if(owner == null){
            Main.LOGGER.error("Spawn FAILED: OWNER is NULL");
        }
        else if(worldReady) {
            boolean isDupe = false;
            if(!PAConfig.CONFIG.ALLOW_DUPLICATE.get()){
                ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(owner);
                for(AbstractEntityCompanion companion:capability.companionList){
                    if(companion.getType() == this.RollResult){
                        isDupe = true;
                        break;
                    }
                }
            }
            BlockPos blockpos;
            if(!isDupe) {
                //Special spawn mechanism for when sunlight is lava. probably Spawning In cave
                if (SolarApocalypse.isSunlightDangerous((ServerLevel) this.level)) {
                    BlockPos.MutableBlockPos CandidatePos = this.worldPosition.mutable();
                    CandidatePos = CandidatePos.move(this.level.getBlockState(this.worldPosition).getValue(FACING));
                    blockpos = CandidatePos;
                } else {
                    blockpos = getRandomBlockposInRadius2D(this.getLevel(), this.getBlockPos(), 20, 10);
                    spawn_sitting = false;
                }
                this.spawnEntity(blockpos, owner, spawn_sitting);
            }
            else{
                blockpos = this.getBlockPos().relative(this.getLevel().getBlockState(this.getBlockPos()).getValue(FACING), 1);
                ItemEntity entity = new ItemEntity(this.level, blockpos.getX(), blockpos.getY(), blockpos.getZ(), new ItemStack(RegisterItems.ORIGINIUM_PRIME.get(), 5));
                this.level.addFreshEntity(entity);
            }
        }
    }

    public void spawnEntity(BlockPos pos, ServerPlayer owner, boolean spawn_sitting){
        if(this.level != null) {
            AbstractEntityCompanion entityCompanion = this.RollResult.create(this.level);
            if (entityCompanion != null) {
                Random rand = new Random();
                for(int i = 0; i < 32; ++i) {
                    this.level.addParticle(ParticleTypes.PORTAL, pos.getX(), pos.getY() + rand.nextDouble() * 2.0D, pos.getZ(), rand.nextGaussian(), 0.0D, rand.nextGaussian());
                }
                entityCompanion.setPos(pos.getX(), pos.getY(), pos.getZ());
                entityCompanion.getNavigation().moveTo((double) this.getBlockPos().getX()+0.5, this.getBlockPos().getY(), (double) this.getBlockPos().getZ()+0.5, 1.0F);
                entityCompanion.setMovingtoRecruitStation(this.getBlockPos());
                entityCompanion.tame(owner);
                if(spawn_sitting) {
                    entityCompanion.setOrderedToSit(true);
                }
                entityCompanion.setMorale(150);
                entityCompanion.setAffection(40F);
                entityCompanion.MaxFillHunger();
                this.level.addFreshEntity(entityCompanion);

                Main.LOGGER.debug("Entity is Spawned at:" + pos);
            }
        }
    }

    @Override
    protected <P extends BlockEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    protected void playsound() {

    }

    @Override
    public void encodeExtraData(FriendlyByteBuf buffer) {
        int[] var = {this.ProcessTime, this.totalProcessTime, this.energyStorage.getEnergyStored(), this.energyStorage.getMaxEnergyStored(), this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()};
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
        addEntry(registerEntity.CHEN.get());
        addEntry(registerEntity.AMIYA.get());
        addEntry(registerEntity.ROSMONTIS.get());
        addEntry(registerEntity.MUDROCK.get());
        addEntry(registerEntity.TEXAS.get());
        addEntry(registerEntity.M4A1.get());
        addEntry(registerEntity.SHIROKO.get());
        if(PAConfig.CONFIG.shouldRecruitBeaconSpawnAllCompanions.get()){
            addEntry(registerEntity.ENTERPRISE.get());
            addEntry(registerEntity.AYANAMI.get());
            addEntry(registerEntity.JAVELIN.get());
            addEntry(registerEntity.GANGWON.get());
            addEntry(registerEntity.NAGATO.get());
        }
    }

    public ContainerData getFields(){
        return this.fields;
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("recruit_beacon");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new ContainerRecruitBeacon(id, player, this.inventory, this.getFields());
    }
}
