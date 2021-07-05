package com.yor42.projectazure.gameobject.entity.companion.kansen;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.RiggingInventoryCapability;
import com.yor42.projectazure.gameobject.containers.entity.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.entity.ai.goals.KansenLaunchPlaneGoal;
import com.yor42.projectazure.gameobject.entity.ai.goals.KansenRangedAttackGoal;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.enums.AmmoCategory;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.network.packets.spawnParticlePacket;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.UUID;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.*;
import static com.yor42.projectazure.libs.utils.MathUtil.*;

public abstract class EntityKansenBase extends AbstractEntityCompanion {

    Random rand = new Random();
    private static final UUID SAILING_SPEED_MODIFIER = UUID.randomUUID();
    private static final AttributeModifier SAILING_SPEED_BOOST = new AttributeModifier(SAILING_SPEED_MODIFIER, "Rigging Swim speed boost",5F, AttributeModifier.Operation.MULTIPLY_TOTAL);

    private static final DataParameter<CompoundNBT> STORAGE = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.COMPOUND_NBT);
    private static final DataParameter<ItemStack> ITEM_RIGGING = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.ITEMSTACK);

    public ItemStackHandler RiggingSlot = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
                EntityKansenBase.this.dataManager.set(ITEM_RIGGING, this.getStackInSlot(0));
        }

        @Override
        protected void onLoad() {
            EntityKansenBase.this.dataManager.set(ITEM_RIGGING, this.getStackInSlot(0));
        }
    };

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.KANSEN;
    }


    protected enums.shipClass shipclass;

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected EntityKansenBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setAffection(40F);
        this.AmmoStorage.setSize(8);
        this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0F);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(STORAGE, new CompoundNBT());
        this.dataManager.register(ITEM_RIGGING,ItemStack.EMPTY);
    }
    public abstract int getRiggingOffset();

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("rigging",this.RiggingSlot.serializeNBT());
    }

    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        if(compound.contains("rigging"))
            this.RiggingSlot.deserializeNBT(compound.getCompound("rigging"));
    }

    public void setShipClass(enums.shipClass setclass){
        this.shipclass = setclass;
    }

    public enums.shipClass getShipClass(){
        return this.shipclass;
    }

    public boolean canUseRigging(){
        if(this.getRigging().getItem() instanceof ItemRiggingBase)
            return !isDestroyed(this.getRigging());
        else
            return false;
    }

    public boolean Hasrigging(){
        return this.getRigging().getItem() instanceof ItemRiggingBase;
    }

    public boolean isSailing(){
        boolean flag = this.isInWater() && this.Hasrigging();
        return flag;
        //return this.isInWater() && this.func_233571_b_(FluidTags.WATER) > (double)f && this.Hasrigging();
    }

    public ItemStack getRigging(){
        if(ITEM_RIGGING != null) {
            return this.dataManager.get(ITEM_RIGGING);
        }
        else{
            return this.getShipRiggingStorage().getStackInSlot(0);
        }
    }

    protected void openGUI(ServerPlayerEntity player){
        NetworkHooks.openGui(player, new ContainerKansenInventory.Supplier(this));
    }


    public boolean hasRigging(){

        return this.getRigging().getItem() instanceof ItemRiggingBase;
    }

    @Nullable
    public ItemStackHandler getHanger(){
        if(this.hasRigging()) {
            if (((ItemRiggingBase) this.getRigging().getItem()).getHangerSlots() > 0)
                return new RiggingInventoryCapability(this.getRigging(), this).getHangar();
        }
        return null;
    }

    public int getPlanetoLaunch(){
        return 1;
    }

    public boolean attackEntityFromCannon(DamageSource source, AmmoProperties property, double distanceMultiplier) {
        if(rollBooleanRNG((float) (property.getRawhitChance()*distanceMultiplier))) {
            if (this.hasRigging()) {
                DamageRiggingorEquipment(property.getDamage(enums.DamageType.RIGGING), this.getRigging());
                DamageComponent(property.getDamage(enums.DamageType.COMPONENT), this.getRigging(), property.ShouldDamageMultipleComponent());
            }
            return super.attackEntityFrom(source, property.getDamage(enums.DamageType.ENTITY));
        }
        return true;
    }

    @ParametersAreNonnullByDefault
    public boolean attackEntityFrom(DamageSource source, float amount) {

        float DamageMultiplier = 1.0F;
        float ignoreArmorChance = RangedFloatRandom(0.05F, 0.1F);
        if(this.hasRigging()&& (!(source==DamageSource.FALL)||!(source == DamageSource.GENERIC && rollBooleanRNG(ignoreArmorChance)))){
            DamageMultiplier = getRiggingedDamageModifier();
            DamageRiggingorEquipment(amount, this.getRigging());
            this.playSound(SoundEvents.ENTITY_IRON_GOLEM_HURT, 1.0F, 1.0f);
        }
        return super.attackEntityFrom(source, amount*DamageMultiplier);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new KansenLaunchPlaneGoal(this, 20, 40, 50));
        this.goalSelector.addGoal(5, new KansenRangedAttackGoal(this, 1.0F, 10,20, 100F, 160F));

    }



    @Override
    public void livingTick() {
        super.livingTick();

        ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(ForgeMod.SWIM_SPEED.get());
        if (this.isSailing()) {
            this.kansenFloat();
            if (modifiableattributeinstance != null && modifiableattributeinstance.getModifier(SAILING_SPEED_MODIFIER) == null) {
                modifiableattributeinstance.applyNonPersistentModifier(SAILING_SPEED_BOOST);
            }
            Vector3d vector3d = this.getMotion();
            double d0 = this.getPosX() + vector3d.x;
            double d1 = this.getPosY() + vector3d.y;
            double d2 = this.getPosZ() + vector3d.z;
            if(!(vector3d.x == 0 || vector3d.z == 0)) {
                this.world.addParticle(ParticleTypes.CLOUD, d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            }
        }
        else if(modifiableattributeinstance != null){
            modifiableattributeinstance.removeModifier(SAILING_SPEED_BOOST);
        }

        if(this.getRigging().getItem() instanceof ItemRiggingBase){
            ((ItemRiggingBase) this.getRigging().getItem()).onUpdate(this.getRigging());
        }
    }

    @Override
    protected boolean canOpenDoor() {
        return !this.isInWater() && !this.isSitting() && !this.isSleeping();
    }

    @Override
    public boolean hasNoGravity() {
        return this.isInWater();
    }

    public ItemStack findAmmo(enums.AmmoCategory category){
        for (int i=0; i<this.AmmoStorage.getSlots();i++){
            Item AmmoItem = this.AmmoStorage.getStackInSlot(i).getItem();
            if(AmmoItem instanceof ItemCannonshell){
                if(category == ((ItemCannonshell) AmmoItem).getAmmoProperty().getCategory()){
                    return this.AmmoStorage.getStackInSlot(i);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUseCannonOrTorpedo() {
        return this.canUseRigging() && (canUseCannon(this.getRigging()) || canUseTorpedo(this.getRigging()));
    }

    public ItemStackHandler getCannonShellStorage() {
        return this.AmmoStorage;
    }

    public boolean canUseShell(enums.AmmoCategory types){
        return findAmmo(types) != ItemStack.EMPTY;
    }

    public ItemStackHandler getShipRiggingStorage() {
        return this.RiggingSlot;
    }

    public AmmoCategory getActiveShellCategory(){
        return AmmoCategory.GENERIC;
    }

    public void AttackUsingCannon(LivingEntity target, float distanceFactor){
        boolean shouldFire = this.canUseShell(getActiveShellCategory()) && this.canUseRigging() && canUseCannon(this.getRigging());
        if(shouldFire) {
            ItemStack Ammostack = this.findAmmo(this.getActiveShellCategory());
            if (Ammostack.getItem() instanceof ItemCannonshell) {
                Vector3d vector3d = this.getLook(1.0F);
                double d2 = target.getPosX() - this.getPosX();
                double d3 = target.getPosY() - this.getPosY()+0.5F;
                double d4 = target.getPosZ() - this.getPosZ();

                EntityCannonPelllet shell = new EntityCannonPelllet(this.world, this, d2, d3, d4, ((ItemCannonshell) Ammostack.getItem()).getAmmoProperty());
                this.playSound(registerSounds.CANON_FIRE_MEDIUM, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                shell.setPosition(this.getPosX() + vector3d.x, this.getPosY()+0.5F, shell.getPosZ() + vector3d.z);
                this.world.addEntity(shell);
                Ammostack.shrink(1);
                Main.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(()->this), new spawnParticlePacket(this, defined.PARTICLE_CANNON_FIRE_ID, vector3d.x, vector3d.y, vector3d.z));

                this.addExp(1.0F);
                ItemStack FiringCannon = getPreparedWeapon(this.getRigging(), enums.SLOTTYPE.MAIN_GUN, this);
                setEquipmentDelay(FiringCannon);
                this.addMorale(-0.1);
            }
        }
    }

    public void AttackUsingTorpedo(LivingEntity target, float distanceFactor){
        boolean shouldFire = this.isSailing() && canUseTorpedo(this.getRigging());
        if(shouldFire){
            Vector3d vector3d = this.getLook(1.0F);
            double d2 = target.getPosX() - (this.getPosX() + vector3d.x * 4.0D);
            double d3 = target.getPosYHeight(0.5D) - this.getPosY() - 0.5D;
            double d4 = target.getPosZ() - (this.getPosZ() + vector3d.z * 4.0D);

            EntityProjectileTorpedo torpedo = new EntityProjectileTorpedo(this, d2, d3, d4, this.world);
            torpedo.setPosition(this.getPosX() + vector3d.x, this.getPosY() - 0.5D, torpedo.getPosZ() + vector3d.z);
            this.world.addEntity(torpedo);
            this.addExp(1.0F);
            ItemStack FiringTorpedo = getPreparedWeapon(this.getRigging(), enums.SLOTTYPE.TORPEDO, this);
            ItemStackUtils.useAmmo(FiringTorpedo);
            setEquipmentDelay(FiringTorpedo);
            this.addMorale(-0.15);
        }
    }

    private void kansenFloat() {
        Vector3d vec3d = this.getMotion();
        float f = this.getEyeHeight() - 1.25F;
        if(this.func_233571_b_(FluidTags.WATER)>f)
            this.setMotion(vec3d.x, vec3d.y + (double)(vec3d.y < (double)0.06F ? 5.0E-4F : 0.0F), vec3d.z);
        else if (vec3d.y>0){
            this.setVelocity(vec3d.x, vec3d.y - 0.10, vec3d.z);
        }
    }
}
