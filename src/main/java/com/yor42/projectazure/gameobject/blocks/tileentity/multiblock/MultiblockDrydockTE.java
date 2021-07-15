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
import net.minecraft.entity.player.ServerPlayerEntity;
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
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;

import static com.yor42.projectazure.gameobject.blocks.AbstractAnimatedBlockMachines.ACTIVE;
import static com.yor42.projectazure.gameobject.blocks.AbstractAnimatedBlockMachines.POWERED;
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

    public boolean isPowered(){

        boolean flag = this.getEnergyStorage().getEnergyStored()>=this.getPowerConsumption();

        return flag;
    }

    @Override
    public void tick() {
        boolean isActive = this.isActive();
        boolean isPowered = this.isPowered();
        boolean shouldSave = false;
        super.tick();
        if(this.world != null && !this.world.isRemote){
            if(isPowered!=this.isPowered() || this.isPowered() && !this.world.getBlockState(this.pos).get(POWERED) || !this.isPowered() && this.world.getBlockState(this.pos).get(POWERED)) {
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(POWERED, this.isPowered()), 2);
                shouldSave = true;
            }
            if(isActive!=this.isActive()){
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(ACTIVE, this.isActive()), 2);
                shouldSave = true;
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
    protected void SpawnResultEntity(ServerPlayerEntity owner) {
        if(this.getWorld() != null && !this.getWorld().isRemote()){
            AbstractEntityCompanion entity = this.getRollResult().create(this.getWorld());
            if(entity != null && owner != null){
                entity.setTamedBy(owner);
                entity.setPosition(this.pos.getX()+0.5, this.pos.getY()-2, this.pos.getZ()+0.5);
                entity.func_233687_w_(true);
                entity.setMorale(150);
                entity.setAffection(40F);
                entity.MaxFillHunger();
                this.getWorld().addEntity(entity);
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
        AnimationBuilder builder = new AnimationBuilder();
        event.getController().transitionLengthTicks = 20;
        boolean flag = this.getWorld()!= null && this.getWorld().getBlockState(this.getPos()).get(ACTIVE) && this.getWorld().getBlockState(this.getPos()).get(POWERED);
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
        return new TranslationTextComponent("tile.drydock");
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
