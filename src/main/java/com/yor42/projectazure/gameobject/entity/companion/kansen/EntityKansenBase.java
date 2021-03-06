package com.yor42.projectazure.gameobject.entity.companion.kansen;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.RiggingInventoryCapability;
import com.yor42.projectazure.gameobject.containers.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.ai.*;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import com.yor42.projectazure.gameobject.items.ItemAmmo;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.enums.AmmoCategory;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.network.packets.spawnParticlePacket;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.*;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.DamageRiggingorEquipment;
import static com.yor42.projectazure.libs.utils.MathUtil.*;

public abstract class EntityKansenBase extends AbstractEntityCompanion {

    Random rand = new Random();
    private static final UUID SAILING_SPEED_MODIFIER = UUID.randomUUID();
    private static final AttributeModifier SAILING_SPEED_BOOST = new AttributeModifier(SAILING_SPEED_MODIFIER, "Rigging Swim speed boost",5F, AttributeModifier.Operation.MULTIPLY_TOTAL);

    private static final DataParameter<CompoundNBT> STORAGE = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.COMPOUND_NBT);
    private static final DataParameter<ItemStack> ITEM_RIGGING = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.ITEMSTACK);

    public ItemStackHandler ShipStorage = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
                EntityKansenBase.this.dataManager.set(ITEM_RIGGING, this.getStackInSlot(0));
        }

        @Override
        protected void onLoad() {
            EntityKansenBase.this.dataManager.set(ITEM_RIGGING, this.getStackInSlot(0));
        }
    };

    public ItemStackHandler AmmoStorage = new ItemStackHandler(8);

    protected enums.shipClass shipclass;

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected EntityKansenBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setAffection(40F);
        this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0F);
    }

    public abstract enums.CompanionRarity getRarity();

    protected void registerData() {
        super.registerData();
        this.dataManager.register(STORAGE, new CompoundNBT());
        this.dataManager.register(ITEM_RIGGING,ItemStack.EMPTY);
    }
    public abstract int getRiggingOffset();

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("rigging",this.ShipStorage.serializeNBT());
        compound.put("ammoStorage", this.AmmoStorage.serializeNBT());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if(compound.contains("rigging"))
            this.ShipStorage.deserializeNBT(compound.getCompound("rigging"));
        if(compound.contains("ammoStorage"))
            this.AmmoStorage.deserializeNBT(compound.getCompound("ammoStorage"));
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

    protected void openGUI(PlayerEntity player){
        NetworkHooks.openGui((ServerPlayerEntity) player, new ContainerKansenInventory.Supplier(this));
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
        this.goalSelector.addGoal(7, new CompanionFollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));

    }



    @Override
    public void livingTick() {
        super.livingTick();

        ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(ForgeMod.SWIM_SPEED.get());
        if (this.isSailing()) {
            this.kansenFloat();
            if (modifiableattributeinstance.getModifier(SAILING_SPEED_MODIFIER) == null) {
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
        else{
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
            if(AmmoItem instanceof ItemAmmo){
                if(category == ((ItemAmmo) AmmoItem).getAmmoProperty().getCategory()){
                    return this.AmmoStorage.getStackInSlot(i);
                };
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStackHandler getAmmoStorage() {
        return this.AmmoStorage;
    }

    public boolean canUseAmmo(enums.AmmoCategory types){
        return findAmmo(types) != ItemStack.EMPTY;
    }

    public boolean isCanonReady(){
        return canUseCannon(this.getRigging());
    }

    public ItemStackHandler getShipRiggingStorage() {
        return this.ShipStorage;
    }

    public AmmoCategory getActiveAmmoCategory(){
        return AmmoCategory.GENERIC;
    }

    public void AttackUsingCannon(LivingEntity target, float distanceFactor){
        boolean shouldFire = this.canUseAmmo(getActiveAmmoCategory()) && this.canUseRigging() && canUseCannon(this.getRigging());
        if(shouldFire) {
            ItemStack Ammostack = this.findAmmo(enums.AmmoCategory.GENERIC);
            if (Ammostack.getItem() instanceof ItemAmmo) {
                Vector3d vector3d = this.getLook(1.0F);
                double d2 = target.getPosX() - this.getPosX();
                double d3 = target.getPosY() - this.getPosY()+0.5F;
                double d4 = target.getPosZ() - this.getPosZ();

                EntityCannonPelllet shell = new EntityCannonPelllet(this.world, this, d2, d3, d4, ((ItemAmmo) Ammostack.getItem()).getAmmoProperty());
                this.playSound(registerSounds.CANON_FIRE_MEDIUM, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                shell.setPosition(this.getPosX() + vector3d.x, this.getPosY()+0.5F, shell.getPosZ() + vector3d.z);
                this.world.addEntity(shell);
                Ammostack.shrink(1);
                Main.NETWORK.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), 50, this.getEntityWorld().getDimensionKey())), new spawnParticlePacket(this, defined.PARTICLE_CANNON_FIRE_ID, vector3d.x, vector3d.y, vector3d.z));

                this.addExp(1.0F);
                ItemStack FiringCannon = getPreparedWeapon(this.getRigging(), enums.SLOTTYPE.GUN, this);
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
            useTorpedoAmmo(FiringTorpedo);
            setEquipmentDelay(FiringTorpedo);
            this.addMorale(-0.15);
        }
    }

    public void ShootArrow(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.findArrow();
        AbstractArrowEntity abstractarrowentity = this.fireArrow(itemstack, distanceFactor, this.getItemStackFromSlot(EquipmentSlotType.MAINHAND));
        if (this.getHeldItemMainhand().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getHeldItemMainhand().getItem()).customArrow(abstractarrowentity);
        double d0 = target.getPosX() - this.getPosX();
        double d1 = target.getPosYHeight(0.3333333333333333D) - abstractarrowentity.getPosY();
        double d2 = target.getPosZ() - this.getPosZ();
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, 3);
        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(abstractarrowentity);
    }

    protected AbstractArrowEntity fireArrow(ItemStack arrowStack, float distanceFactor, ItemStack ItemInHand) {


        return ProjectileHelper.fireArrow(this, arrowStack, distanceFactor);
    }

    protected ItemStack findArrow(){
        for(int i = 0; i<this.getInventory().getSlots(); i++){
            if(this.getInventory().getStackInSlot(i).getItem() instanceof ArrowItem){
                return this.getInventory().getStackInSlot(i);
            }
        }
        return ItemStack.EMPTY;
    };

    private void kansenFloat() {
        Vector3d vec3d = this.getMotion();
        float f = this.getEyeHeight() - 1.25F;
        if(this.func_233571_b_(FluidTags.WATER)>f)
            this.setMotion(vec3d.x * (double)0.99F, vec3d.y + (double)(vec3d.y < (double)0.06F ? 5.0E-4F : 0.0F), vec3d.z * (double)0.99F);
        else if (vec3d.y>0){
            this.setVelocity(vec3d.x, vec3d.y - 0.10, vec3d.z);
        }
    }
}
