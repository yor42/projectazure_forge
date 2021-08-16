package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.enums;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractTileEntityGacha extends AbstractAnimateableEnergyTickTE{

    private class Entry {
        double accumulatedWeight;
        EntityType<? extends AbstractEntityCompanion> EntityType;
    }
    private final List<Entry> entries = new ArrayList<>();
    private Entry currentEntry = null;
    private double accumulatedWeight;
    protected int ProcessTime, totalProcessTime;
    protected EntityType<? extends AbstractEntityCompanion> RollResult;
    protected PlayerEntity nextTaskStarter;
    protected int powerConsumption;
    protected boolean shouldProcess;
    private final Random rand = new Random();

    public void addEntryCustomWeight(EntityType<? extends AbstractEntityCompanion> entity, double weight) {
        if(weight<0){
            this.addEntry(entity);
        }
        else {
            accumulatedWeight += weight;
            Entry e = new Entry();
            e.EntityType = entity;
            e.accumulatedWeight = accumulatedWeight;
            entries.add(e);
        }
    }

    public void addEntry(EntityType<? extends AbstractEntityCompanion> entityType) {
        if(this.world != null) {
            AbstractEntityCompanion companion = entityType.create(this.world);
            if(companion!= null) {
                double weight = this.getWeightFromRarity(companion);
                accumulatedWeight += weight;
                Entry e = new Entry();
                e.EntityType = entityType;
                e.accumulatedWeight = accumulatedWeight;
                entries.add(e);
            }
        }
    }

    public int getPowerConsumption(){
        return this.powerConsumption;
    }

    private double getWeightFromRarity(AbstractEntityCompanion entity) {
        switch (entity.getRarity()){
            default: return 0;
            case STAR_1:
                return PAConfig.CONFIG.Star_1_Chance.get();
            case STAR_2:
                return PAConfig.CONFIG.Star_2_Chance.get();
            case STAR_3:
                return PAConfig.CONFIG.Star_3_Chance.get();
            case STAR_4:
                return PAConfig.CONFIG.Star_4_Chance.get();
            case STAR_5:
                return PAConfig.CONFIG.Star_5_Chance.get();
            case STAR_6:
                return PAConfig.CONFIG.Star_6_Chance.get();
        }
    }

    public EntityType<? extends AbstractEntityCompanion> getRollResult(){
        if(entries.isEmpty()){
            this.registerRollEntry();
        }
        double r = rand.nextDouble() * accumulatedWeight;

        for (Entry entry: entries) {
            if (entry.accumulatedWeight >= r - this.getResourceChanceBonus()) {
                this.currentEntry = entry;
                return entry.EntityType;
            }
        }
        return null;
    }

    /*
    Write Algorithm here if you want to give player Bonus for using more items on construction.
    returns 0 for no bonus.
     */
    protected abstract double getResourceChanceBonus();

    protected abstract boolean canStartProcess();

    public void StartMachine(ServerPlayerEntity starter) {
        if (starter != null) {
            if (this.canStartProcess() || starter.isCreative()) {

                EntityType<? extends AbstractEntityCompanion> result = this.getRollResult();
                AbstractEntityCompanion Entity = result.create(this.world);
                if (Entity != null) {
                    int expectedProcessTime = this.getProcessTimePerEntity(Entity);
                    if (expectedProcessTime > 0) {
                        //Convert Second to Tick
                        this.totalProcessTime = expectedProcessTime * 20;
                        this.RollResult = result;
                        Main.LOGGER.debug("Roll Result:" + Entity.getName().toString() + " with " + expectedProcessTime + "second Recruit Time");
                        this.shouldProcess = true;
                        this.nextTaskStarter = starter;
                        this.UseGivenResource();
                    } else {
                        Main.LOGGER.error("Next Spawn Delay time is 0! Is entry valid?");
                    }
                }
            } else {
                starter.sendMessage(new TranslationTextComponent("machine.notenoughresource"), starter.getUniqueID());
            }
        }
    }

    public boolean isPowered(){
        return this.getEnergyStorage().getEnergyStored()>=this.getPowerConsumption();
    }


    /*
        Deduct the used item from machine's inventory
         */
    protected abstract void UseGivenResource();

    public void resetMachine(){
        this.totalProcessTime = 0;
        this.ProcessTime = 0;
        this.currentEntry = null;
        this.RollResult = null;
        this.shouldProcess = false;
        this.nextTaskStarter = null;
    }

    public int getProcessTimePerEntity(AbstractEntityCompanion entity){
        enums.CompanionRarity rarity = entity.getRarity();
        int minTime = 0;
        int maxTime = 0;

        switch (rarity){
            case STAR_1:
                minTime = PAConfig.CONFIG.Star_1_MinTime.get();
                maxTime = PAConfig.CONFIG.Star_1_MaxTime.get();
                break;
            case STAR_2:
                minTime = PAConfig.CONFIG.Star_2_MinTime.get();
                maxTime = PAConfig.CONFIG.Star_2_MaxTime.get();
                break;
            case STAR_3:
                minTime = PAConfig.CONFIG.Star_3_MinTime.get();
                maxTime = PAConfig.CONFIG.Star_3_MaxTime.get();
                break;
            case STAR_4:
                minTime = PAConfig.CONFIG.Star_4_MinTime.get();
                maxTime = PAConfig.CONFIG.Star_4_MaxTime.get();
                break;
            case STAR_5:
                minTime = PAConfig.CONFIG.Star_5_MinTime.get();
                maxTime = PAConfig.CONFIG.Star_5_MaxTime.get();
                break;
            case STAR_6:
                minTime = PAConfig.CONFIG.Star_6_MinTime.get();
                maxTime = PAConfig.CONFIG.Star_6_MaxTime.get();
                break;
        }

        if(maxTime-minTime>0){
            return (int) ((maxTime-minTime)*this.rand.nextFloat())+minTime;
        }
        else{
            Main.LOGGER.error("Min Construction Time for "+rarity+" rarity is larger than Max Construction time!");
        }

        return 0;
    }

    protected AbstractTileEntityGacha(TileEntityType<?> typeIn) {
        super(typeIn);
        this.registerRollEntry();
    }

    @Override
    public void tick() {
        super.tick();
        boolean isActive = this.isActive();
        boolean shouldsave = false;
        if (this.world != null && !this.world.isRemote) {
            boolean flag1 = this.energyStorage.getEnergyStored() >= this.powerConsumption;
            boolean flag2 = canProcess() && this.shouldProcess && this.totalProcessTime>0;
            if(flag1 && flag2){
                shouldsave = true;
                this.ProcessTime++;
                this.energyStorage.extractEnergy(this.powerConsumption, false);
                if(this.ProcessTime >= this.totalProcessTime){
                    this.SpawnResultEntity((ServerPlayerEntity) this.nextTaskStarter);
                    this.resetMachine();
                }
            }
        }
        if(shouldsave){
            this.markDirty();
        }

        if(!isActive && this.isActive()){
            this.playsound();
        }


        if(this.getWorld() != null && isActive != this.isActive()) {
            this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    protected abstract void SpawnResultEntity(ServerPlayerEntity owner);

    /*
    For Future usage of additional processing criteria
     */
    protected boolean canProcess(){return true;};

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("processtime", this.ProcessTime);
        compound.putInt("totalprocesstime", this.totalProcessTime);
        compound.putDouble("accumulatedWeight", this.accumulatedWeight);
        compound.putString("taskresult", this.RollResult == null? "null": EntityType.getKey(this.RollResult).toString());
        if(this.nextTaskStarter != null) {
            compound.putUniqueId("taskOwner", this.nextTaskStarter.getUniqueID());
        }
        compound.putBoolean("shouldProcess", this.shouldProcess);
        return compound;
    }

    /*
    I know What I'm doing java :concern:
     */
    @SuppressWarnings("unchecked")
    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.ProcessTime = nbt.getInt("processtime");
        this.totalProcessTime = nbt.getInt("totalprocesstime");
        this.accumulatedWeight = nbt.getDouble("accumulatedWeight");
        this.shouldProcess = nbt.getBoolean("shouldProcess");
        String key = nbt.getString("taskresult");
        if(key.equals("null") || !EntityType.byKey(key).isPresent()){
            this.RollResult = null;
        }
        else {
            //WARNING: Because of this line DO NOT ADD NON COMPANION ENTITY IN POOL
            this.RollResult = (EntityType<? extends AbstractEntityCompanion>) EntityType.byKey(key).orElse(null);
        }
        this.nextTaskStarter = this.world == null || !nbt.contains("taskOwner")? null:this.world.getPlayerByUuid(nbt.getUniqueId("taskOwner"));
    }

    /*
    register Roll Entries here using addEntry(entity);
     */

    public abstract void registerRollEntry();

    public boolean isActive(){
        return this.ProcessTime>0 && this.totalProcessTime >0;
    }
}
