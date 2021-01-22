package com.yor42.projectazure.gameobject.entity;

import com.sun.javafx.geom.Vec3d;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class EntityKansenBase extends TameableEntity {

    /*
   1 = Level
   2 = Morale
    */
    protected int[] stat;
    public boolean hasRigging;
    protected double floatWaterLevel = (this.getPosY() + 0.25F);
    public shipClass shipclass;

    protected EntityKansenBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public void setShipClass(shipClass setclass){
        this.shipclass = setclass;
    }

    public shipClass getShipClass(){
        return this.shipclass;
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean canMateWith(AnimalEntity otherAnimal) {
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    @Override
    public boolean isChild() {
        return false;
    }

    public int getStat(byte id) {
        return stat[id];
    }

    public void setStat(byte id, int value) {
        stat[id] = value;
    }

    @Override
    public void tick() {
        super.tick();

        if(this.isInWater() && this.func_233571_b_(FluidTags.WATER) > floatWaterLevel){
            kansenFloat();
        }
    }

    private void kansenFloat() {
        Vector3d vec3d = this.getMotion();
        this.setVelocity(vec3d.x * 0.9900000095367432D, vec3d.y + (double)(vec3d.y < 0.05999999865889549D ? 5.0E-4F : 0.0F), vec3d.z * 0.9900000095367432D);
    }

    public enum shipClass {
        Destroyer,
        LightCruiser,
        HeavyCruiser,
        LargeCruiser,
        Battleship,
        AircraftCarrier,
        LightAircraftCarrier,
        Submarine,
        SubmarineCarrier,
        MonitorShip,
        Repair
    }

}
