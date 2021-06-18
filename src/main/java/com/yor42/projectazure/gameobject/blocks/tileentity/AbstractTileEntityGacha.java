package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntityType;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractTileEntityGacha extends AbstractAnimateableEnergyTickTE{

    private class Entry {
        double accumulatedWeight;
        AbstractEntityCompanion entity;
    }

    private final List<Entry> entries = new ArrayList<>();
    private double accumulatedWeight;
    protected int ProcessTime, totalProcessTime;
    protected AbstractEntityCompanion entityCompanion;
    protected int powerConsumption;
    private final Random rand = new Random();

    public void addEntry(AbstractEntityCompanion entity, double weight) {
        accumulatedWeight += weight;
        Entry e = new Entry();
        e.entity = entity;
        e.accumulatedWeight = accumulatedWeight;
        entries.add(e);
    }

    public AbstractEntityCompanion getRollResult(){
        double r = rand.nextDouble() * accumulatedWeight;

        for (Entry entry: entries) {
            if (entry.accumulatedWeight >= r - this.getResourceChanceBonus()) {
                return entry.entity;
            }
        }
        return null;
    }

    /*
    Write Algorithm here if you want to give player Bonus for using more items on construction.
    returns 0 for no bonus.
     */
    protected abstract double getResourceChanceBonus();

    public void StartMachine(){
        AbstractEntityCompanion RollResult = this.getRollResult();
        int expectedProcessTime = this.getProcessTimePerRarity(RollResult);
        if(expectedProcessTime>0) {
            //Convert Second to Tick
            this.totalProcessTime = expectedProcessTime*20;
            this.entityCompanion = RollResult;
            this.UseGivenResource();
        }
        else{
            Main.LOGGER.error("Expected COnstruction time is 0! Is entry valid?");
        }

    }
    /*
    Deduct the used item from machine's inventory
     */
    protected abstract void UseGivenResource();

    public void resetMachine(){
        this.totalProcessTime = 0;
        this.entityCompanion = null;
    }

    public int getProcessTimePerRarity(AbstractEntityCompanion entity){
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

        if(maxTime>0 && minTime>0){
            return (int) ((maxTime-minTime)*this.rand.nextFloat())+minTime;
        }

        return 0;
    }

    protected AbstractTileEntityGacha(TileEntityType<?> typeIn) {
        super(typeIn);
        this.registerRollEntry();
    }

    /*
    register Roll Entries here using addEntry(entity, chance);
     */

    public abstract void registerRollEntry();


}
