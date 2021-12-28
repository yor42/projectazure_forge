package com.yor42.projectazure.gameobject.entity.companion.kansen;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.containers.entity.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.enums.AmmoCategory;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.network.packets.spawnParticlePacket;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.enchantment.ProtectionEnchantment;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.UUID;

import static com.yor42.projectazure.libs.enums.SLOTTYPE.MAIN_GUN;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.*;
import static com.yor42.projectazure.libs.utils.MathUtil.*;

public abstract class EntityKansenBase extends AbstractEntityCompanion {

    Random rand = new Random();
    private static final UUID SAILING_SPEED_MODIFIER = UUID.randomUUID();
    private static final AttributeModifier SAILING_SPEED_BOOST = new AttributeModifier(SAILING_SPEED_MODIFIER, "Rigging Swim speed boost",5F, AttributeModifier.Operation.MULTIPLY_TOTAL);

    private static final DataParameter<Integer> SHIP_FIRE_TICK = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.VARINT);
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
        this.dataManager.register(SHIP_FIRE_TICK, 0);
        this.dataManager.register(ITEM_RIGGING,ItemStack.EMPTY);
    }

    public boolean isShipOnFire(){
        return this.getShipFireTicks()>0;
    }

    public int getShipFireTicks(){
        return this.dataManager.get(SHIP_FIRE_TICK);
    }

    public void ForceShipFireTicks(int tick){
        this.dataManager.set(SHIP_FIRE_TICK, tick);
    }

    public void setShipFire(int seconds) {
        int i = seconds * 20;
        i = ProtectionEnchantment.getFireTimeForEntity((LivingEntity)this, i);

        if (this.getShipFireTicks() < i) {
            this.ForceShipFireTicks(i);
        }
    }

    public void ExtinguishShipfire(){
        this.ForceShipFireTicks(0);
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("shipfire", this.getShipFireTicks());
        compound.put("rigging",this.RiggingSlot.serializeNBT());
    }

    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.ForceShipFireTicks(compound.getInt("shipfire"));
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
            return true;
        else
            return false;
    }

    public boolean Hasrigging(){
        return this.getRigging().getItem() instanceof ItemRiggingBase;
    }

    public boolean isSailing(){
        if(this.isInWater() && this.canUseRigging()){
            ItemStack rigging = this.getRigging();
            boolean hasfuel = rigging.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map((fluidtank)-> {
                int amount =fluidtank.getFluidInTank(0).getAmount();
                return amount>0;}).orElse(false);
            return hasfuel;
        }
        return false;
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
        return this.getRigging().getItem() instanceof ItemRiggingBase && ((ItemRiggingBase) this.getRigging().getItem()).getValidclass() == this.getShipClass();
    }

    @Nullable
    public IItemHandler getHanger() {
        if (this.hasRigging()) {
            IItemHandler hangar = MultiInvUtil.getCap(this.getRigging()).getInventory(enums.SLOTTYPE.PLANE.ordinal());
            if (hangar.getSlots() > 0) {
                return hangar;
            }
        }
        return null;
    }

    public int getPlanetoLaunch(){
        return 1;
    }

    public boolean attackEntityFromCannon(DamageSource source, AmmoProperties property, double distanceMultiplier) {
        if(rollBooleanRNG((float) (property.getRawhitChance()*distanceMultiplier))) {
            if (this.hasRigging()) {
                DamageItem(property.getDamage(enums.DamageType.RIGGING), this.getRigging());
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
            DamageItem(amount, this.getRigging());
            this.playSound(SoundEvents.ENTITY_IRON_GOLEM_HURT, 1.0F, 1.0f);
        }
        return super.attackEntityFrom(source, amount*DamageMultiplier);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
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
            if(!PAConfig.CONFIG.RiggingInfiniteFuel.get() && this.ticksExisted%2 == 0) {
                this.getRigging().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent((fluidtank) -> {
                    fluidtank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                });
            }
        }
        else if(modifiableattributeinstance != null){
            modifiableattributeinstance.removeModifier(SAILING_SPEED_BOOST);
        }

        if(this.getRigging().getItem() instanceof ItemRiggingBase){
            ((ItemRiggingBase) this.getRigging().getItem()).onUpdate(this.getRigging());
        }

        if(this.isShipOnFire()){
            if (this.getShipFireTicks() % 20 == 0) {
                this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
            }

            this.ForceShipFireTicks(this.getShipFireTicks() - 1);
        }
    }

    @Override
    public boolean canRenderOnFire() {
        return super.canRenderOnFire() || this.isShipOnFire();
    }

    @Override
    public boolean hasNoGravity() {

        return this.isSailing();
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

            enums.SLOTTYPE CannonType = getPreparedWeapon(this.getRigging(), MAIN_GUN, null) != ItemStack.EMPTY? MAIN_GUN : enums.SLOTTYPE.SUB_GUN;

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
                Main.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(()->this), new spawnParticlePacket(this, spawnParticlePacket.Particles.CANNON_SMOKE, vector3d.x, vector3d.y, vector3d.z));

                this.addExp(1.0F);
                ItemStack FiringCannon = getPreparedWeapon(this.getRigging(), CannonType, this);
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
        if(this.func_233571_b_(FluidTags.WATER)>0.3)
            this.setMotion(vec3d.x, vec3d.y + (double)(vec3d.y < (double)0.06F ? 5.0E-3F : 0.0F), vec3d.z);
        else if (vec3d.y>0){
            this.setVelocity(vec3d.x, vec3d.y - 0.0001, vec3d.z);
        }
    }

    @Override
    public boolean canUseSkill() {
        return this.canUseCannonOrTorpedo() && this.isSailing();
    }
}
