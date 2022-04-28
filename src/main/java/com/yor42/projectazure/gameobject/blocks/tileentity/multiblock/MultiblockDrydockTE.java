package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.blocks.tileentity.AbstractAnimatedTileEntityMachines;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityRecruitBeacon;
import com.yor42.projectazure.gameobject.containers.machine.ContainerDryDock;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import static com.yor42.projectazure.gameobject.blocks.AbstractElectricMachineBlock.ACTIVE;
import static com.yor42.projectazure.gameobject.blocks.AbstractElectricMachineBlock.POWERED;
import static com.yor42.projectazure.setup.register.registerTE.DRYDOCK;

public class MultiblockDrydockTE extends MultiblockBaseTE implements MenuProvider, Container {

    private final ContainerData fields = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return MultiblockDrydockTE.this.ProcessTime;
                case 1:
                    return MultiblockDrydockTE.this.totalProcessTime;
                case 2:
                    return MultiblockDrydockTE.this.energyStorage.getEnergyStored();
                case 3:
                    return MultiblockDrydockTE.this.energyStorage.getMaxEnergyStored();
                case 4:
                    return MultiblockDrydockTE.this.getBlockPos().getX();
                case 5:
                    return MultiblockDrydockTE.this.getBlockPos().getY();
                case 6:
                    return MultiblockDrydockTE.this.getBlockPos().getZ();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    MultiblockDrydockTE.this.ProcessTime = value;
                    break;
                case 1:
                    MultiblockDrydockTE.this.totalProcessTime = value;
                    break;
                case 2:
                    MultiblockDrydockTE.this.energyStorage.setEnergy(value);
                    break;
                case 3:
                    MultiblockDrydockTE.this.energyStorage.setMaxEnergy(value);
                    break;
            }
        }

        @Override
        public int getCount() {
            return 7;
        }
    };

    public ContainerData getFields(){
        return this.fields;
    }

    @Override
    protected double getResourceChanceBonus() {
        return 0;
    }

    @Override
    protected boolean canStartProcess() {
        int AluminiumCount = this.inventory.getStackInSlot(1).getCount()+this.inventory.getStackInSlot(2).getCount();
        int SteelCount = this.inventory.getStackInSlot(3).getCount()+this.inventory.getStackInSlot(4).getCount();
        int CopperCount = this.inventory.getStackInSlot(5).getCount()+this.inventory.getStackInSlot(6).getCount();
        int ZincCount = this.inventory.getStackInSlot(7).getCount()+this.inventory.getStackInSlot(8).getCount();

        return this.inventory.getStackInSlot(0) != ItemStack.EMPTY && AluminiumCount>=40 && CopperCount>=30 && SteelCount>=60 && ZincCount>=20;
    }

    @Override
    protected void UseGivenResource() {
        this.inventory.getStackInSlot(0).shrink(1);
        for(int i=1; i<this.inventory.getSlots(); i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public MultiblockDrydockTE(BlockPos pos, BlockState state) {
        super(DRYDOCK.get(), pos, state);
        this.inventory.setSize(9);
        this.energyStorage.setMaxEnergy(30000);
        this.powerConsumption = 2000;
    }

    @Override
    protected void SpawnResultEntity(ServerPlayer owner) {
        if(this.getLevel() != null && !this.getLevel().isClientSide()){
            AbstractEntityCompanion entity = this.getRollResult().create(this.getLevel());
            if(entity != null && owner != null){
                entity.tame(owner);
                entity.setPos(this.worldPosition.getX()+0.5, this.worldPosition.getY()-2, this.worldPosition.getZ()+0.5);
                entity.setOrderedToSit(true);
                entity.setMorale(150);
                entity.setAffection(40F);
                entity.MaxFillHunger();
                this.getLevel().addFreshEntity(entity);
            }
        }
    }

    @Override
    public void registerRollEntry() {
        addEntry(registerManager.ENTITYTYPE_ENTERPRISE);
        addEntry(registerManager.ENTITYTYPE_AYANAMI);
        addEntry(registerManager.ENTITYTYPE_GANGWON);
        addEntry(registerManager.ENTITYTYPE_NAGATO);
        addEntry(registerManager.ENTITYTYPE_JAVELIN);
        addEntry(registerManager.ENTITYTYPE_LAFFEY);
        addEntry(registerManager.ENTITYTYPE_Z23);
    }

    @Override
    protected <P extends BlockEntity & IAnimatable> PlayState predicate_machine(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();
        event.getController().transitionLengthTicks = 20;
        boolean flag = this.getLevel()!= null && this.getLevel().getBlockState(this.getBlockPos()).hasProperty(ACTIVE) && this.getLevel().getBlockState(this.getBlockPos()).hasProperty(POWERED) && this.getLevel().getBlockState(this.getBlockPos()).getValue(ACTIVE) && this.getLevel().getBlockState(this.getBlockPos()).getValue(POWERED);
        if(flag) {
            event.getController().setAnimation(builder.addAnimation("working", true));
            return PlayState.CONTINUE;
        }

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
    public Component getDisplayName() {
        return new TranslatableComponent("tile.drydock");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new ContainerDryDock(id, player, this.inventory, this.fields);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AABB getRenderBoundingBox() {
        BlockPos pos = getBlockPos();
        AABB bb = new AABB(pos.offset(-2, -4, -2), pos.offset(2, 2, 2));
        return bb;
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

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {

    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return true;
    }

    @Override
    public void clearContent() {

    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if (t instanceof MultiblockDrydockTE blockEntity) {
            boolean isActive = blockEntity.isActive();
            gachamachineTick(level, blockPos, blockState,blockEntity);
            ////////////super
            boolean isPowered = blockEntity.isPowered();
            boolean shouldSave = false;
            if(blockEntity.level != null && !blockEntity.level.isClientSide){
                if(isPowered!=blockEntity.isPowered() || blockEntity.isPowered() && !blockEntity.level.getBlockState(blockEntity.worldPosition).getValue(POWERED) || !blockEntity.isPowered() && blockEntity.level.getBlockState(blockEntity.worldPosition).getValue(POWERED)) {
                    blockEntity.level.setBlock(blockEntity.worldPosition, blockEntity.level.getBlockState(blockEntity.worldPosition).setValue(POWERED, blockEntity.isPowered()), 2);
                    shouldSave = true;
                }
                if(isActive!=blockEntity.isActive()){
                    blockEntity.level.setBlock(blockEntity.worldPosition, blockEntity.level.getBlockState(blockEntity.worldPosition).setValue(ACTIVE, blockEntity.isActive()), 2);
                    shouldSave = true;
                }
            }

            if(shouldSave){blockEntity.setChanged();}
        }
    }
}
