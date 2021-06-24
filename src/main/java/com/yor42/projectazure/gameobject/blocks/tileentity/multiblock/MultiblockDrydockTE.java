package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.yor42.projectazure.gameobject.blocks.AbstractMultiBlockBase;
import com.yor42.projectazure.gameobject.blocks.MultiblockStructureBlocks;
import com.yor42.projectazure.gameobject.blocks.RecruitBeaconBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityRecruitBeacon;
import com.yor42.projectazure.gameobject.containers.machine.ContainerDryDock;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;

import static com.yor42.projectazure.gameobject.blocks.blockMultiblockDryDockController.ACTIVE;
import static com.yor42.projectazure.gameobject.blocks.blockMultiblockDryDockController.POWERED;
import static com.yor42.projectazure.setup.register.registerTE.DRYDOCK;

public class MultiblockDrydockTE extends MultiblockBaseTE{

    private final IIntArray fields = new IIntArray() {
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
                    return MultiblockDrydockTE.this.getPos().getX();
                case 5:
                    return MultiblockDrydockTE.this.getPos().getY();
                case 6:
                    return MultiblockDrydockTE.this.getPos().getZ();
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
        public int size() {
            return 7;
        }
    };

    public IIntArray getFields(){
        return this.fields;
    }

    @Override
    protected double getResourceChanceBonus() {
        return 0;
    }

    @Override
    protected boolean canStartProcess() {
        return false;
    }

    @Override
    protected void UseGivenResource() {

    }

    public boolean isPowered(){
        return this.getEnergyStorage().getEnergyStored()>=this.getPowerConsumption();
    }

    @Override
    public void tick() {
        boolean isPowered = this.isPowered();
        boolean isActive = this.isActive();
        boolean shouldSave = false;
        super.tick();
        if(this.world != null && !this.world.isRemote){
            if(isPowered!=this.isPowered()){
                shouldSave = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(POWERED, this.isPowered()), 2);
            }
            if(isActive!=this.isActive()){
                shouldSave = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(ACTIVE, this.isActive()), 2);
            }
        }

        if(shouldSave){this.markDirty();}
    }

    public MultiblockDrydockTE() {
        super(DRYDOCK.get());
        this.inventory.setSize(9);
        this.energyStorage.setMaxEnergy(30000);
        this.powerConsumption = 2000;
    }

    @Override
    protected void SpawnResultEntity() {
        if(this.getWorld() != null && !this.getWorld().isRemote()){
            AbstractEntityCompanion entity = this.getRollResult().create(this.getWorld());
            if(entity != null){
                entity.setTamedBy(this.nextTaskStarter);
                entity.setPosition(this.pos.getX(), this.pos.getY()-2, this.pos.getZ());
                entity.func_233687_w_(true);
            }
        }
    }

    @Override
    public void registerRollEntry() {
        addEntry(registerManager.ENTERPRISE_ENTITY_TYPE);
        addEntry(registerManager.ENTITYAYANAMI);
        addEntry(registerManager.ENTITYGANGWON);
        addEntry(registerManager.ENTITYTYPE_NAGATO);
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
        return new TranslationTextComponent("drydock");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ContainerDryDock(id, player, this.inventory, this.fields);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        BlockPos pos = getPos();
        AxisAlignedBB bb = new AxisAlignedBB(pos.add(-2, -4, -2), pos.add(2, 2, 2));
        return bb;
    }
}
