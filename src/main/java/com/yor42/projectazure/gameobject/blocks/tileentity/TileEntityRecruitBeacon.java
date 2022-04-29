package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.blocks.RecruitBeaconBlock;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.containers.machine.ContainerRecruitBeacon;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
import static com.yor42.projectazure.gameobject.blocks.RecruitBeaconBlock.POWERED;
import static com.yor42.projectazure.libs.utils.MathUtil.getRandomBlockposInRadius2D;

public class TileEntityRecruitBeacon extends AbstractTileEntityGacha implements MenuProvider {
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

    public TileEntityRecruitBeacon(BlockPos pos, BlockState state) {
        super(registerTE.RECRUIT_BEACON.get(), pos, state);
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
                /*if (SolarApocalypse.isSunlightDangerous((ServerWorld) this.level)) {
                    BlockPos.MutableBlockPos CandidatePos = this.worldPosition.mutable();
                    CandidatePos = CandidatePos.move(this.level.getBlockState(this.worldPosition).getValue(FACING));
                    blockpos = CandidatePos;
                } else */
                    blockpos = getRandomBlockposInRadius2D(this.getLevel(), this.getBlockPos(), 20, 10);
                    spawn_sitting = false;

                this.spawnEntity(blockpos, owner, spawn_sitting);
            }
            else{
                blockpos = this.getBlockPos().relative(this.getLevel().getBlockState(this.getBlockPos()).getValue(FACING), 1);
                ItemEntity entity = new ItemEntity(this.level, blockpos.getX(), blockpos.getY(), blockpos.getZ(), new ItemStack(registerItems.ORIGINIUM_PRIME.get(), 5));
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
    public void encodeExtraData(FriendlyByteBuf  buffer) {
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
        addEntry(Main.ENTITYTYPE_CHEN);
        addEntry(Main.ENTITYTYPE_AMIYA);
        addEntry(Main.ENTITYTYPE_ROSMONTIS);
        addEntry(Main.ENTITYTYPE_MUDROCK);
        addEntry(Main.ENTITYTYPE_TEXAS);
        addEntry(Main.ENTITYTYPE_M4A1);
        addEntry(Main.ENTITYTYPE_SHIROKO);
        if(PAConfig.CONFIG.shouldRecruitBeaconSpawnAllCompanions.get()){
            addEntry(Main.ENTITYTYPE_ENTERPRISE);
            addEntry(Main.ENTITYTYPE_AYANAMI);
            addEntry(Main.ENTITYTYPE_JAVELIN);
            addEntry(Main.ENTITYTYPE_GANGWON);
            addEntry(Main.ENTITYTYPE_NAGATO);
        }
    }

    public ContainerData getFields(){
        return this.fields;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("recruit_beacon");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new ContainerRecruitBeacon(id, player, this.inventory, this.getFields());
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof TileEntityRecruitBeacon blockEntity) {
            gachamachineTick(level, blockPos, blockState,blockEntity);
            boolean isPowered = blockEntity.isPowered();
            boolean shouldSave = false;
            if (!(blockEntity.level != null && blockEntity.level.isClientSide)) {
                if (isPowered != blockEntity.isPowered() || blockEntity.isPowered() && !blockEntity.level.getBlockState(blockEntity.worldPosition).getValue(POWERED) || !blockEntity.isPowered() && blockEntity.level.getBlockState(blockEntity.worldPosition).getValue(POWERED)) {
                    shouldSave = true;
                    blockEntity.level.setBlock(blockEntity.worldPosition, blockEntity.level.getBlockState(blockEntity.worldPosition).setValue(POWERED, blockEntity.isPowered()), 2);
                }
                boolean flag = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).hasProperty(ACTIVE) && blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getValue(ACTIVE);
                if (flag) {
                    shouldSave = true;
                    blockEntity.level.setBlock(blockEntity.worldPosition, blockEntity.level.getBlockState(blockEntity.worldPosition).setValue(RecruitBeaconBlock.ACTIVE, blockEntity.isActive()), 2);
                }
            }

            if (shouldSave) {
                blockEntity.setChanged();
            }
        }
    }
}
