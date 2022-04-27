package com.yor42.projectazure.gameobject.items.gun;

import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileBullet;
import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.*;
import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.libs.utils.MathUtil.rand;

public abstract class ItemGunBase extends Item implements IAnimatable, ISyncable,  ICraftingTableReloadable {

    private final boolean isSemiAuto;
    private final boolean isTwoHanded;
    private final int minFireDelay;
    private final int reloadDelay;
    private final enums.GunClass GunClass;

    private final Item MagItem;

    private final SoundEvent fireSound;
    private final SoundEvent reloadSound;
    private final float damage;
    private final float accuracy;

    private final int magCap;
    private final int roundsPerReload;
    protected final String controllerName = "gunController";

    private float recoil = 1.0F;

    public static final int FIRING = 1;
    public static final int RELOADING = 2;

    public AnimationFactory factory = new AnimationFactory(this);

    public ItemGunBase(boolean semiAuto, int minFiretime, int clipsize, int reloadtime, float damage, SoundEvent firesound, SoundEvent reloadsound, int roundsPerReload, float accuracy, Properties properties, boolean isTwohanded, Item MagItem, enums.GunClass gunclass) {
        super(properties);
        this.isSemiAuto = semiAuto;
        this.minFireDelay = minFiretime;
        this.reloadDelay = reloadtime;
        this.fireSound = firesound;
        this.reloadSound = reloadsound;
        this.damage = damage;
        this.accuracy = accuracy;
        this.magCap = clipsize;
        this.roundsPerReload = roundsPerReload;
        this.isTwoHanded = isTwohanded;
        this.MagItem = MagItem;
        this.GunClass = gunclass;
        GeckoLibNetwork.registerSyncable(this);
    }

    public void setRecoil(float recoil){
        this.recoil = recoil;
    }

    public float getRecoil(){
        return 5F;
    }

    public SoundEvent getFireSound() {
        return this.fireSound;
    }

    public SoundEvent getReloadSound() {
        return this.reloadSound;
    }

    public boolean isTwoHanded(){
        return this.isTwoHanded;
    }

    public enums.GunClass getGunClass(){
        return this.GunClass;
    }

    public int getMinFireDelay() {
        return this.minFireDelay;
    }

    public boolean ShouldFireWithLeftClick(){
        return true;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {

        this.SecondaryAction(playerIn, playerIn.getItemInHand(handIn));
        return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(handIn));
    }

    public int getMaxAmmo(){
        return this.magCap;
    }

    public int getRoundsPerReload() {
        return this.roundsPerReload;
    }

    public boolean isSemiAuto() {
        return this.isSemiAuto;
    }

    public String getFactoryName(){
        return this.controllerName;
    }

    public boolean ShouldDoBowPose(){
        return true;
    }

    public Item getMagItem(){
        return this.MagItem;
    }

    protected abstract void SecondaryAction(Player playerIn, ItemStack heldItem);

    public boolean shootGun(ItemStack gun, Level world, Player entity, boolean zooming, InteractionHand hand, @Nullable Entity target) {
        // Gets the item that the player is holding, should be this item.
        final int id = GeckoLibUtil.guaranteeIDForStack(gun, (ServerLevel) world);
        // Tell all nearby clients to trigger this item to animate
        final PacketDistributor.PacketTarget receiver = PacketDistributor.TRACKING_ENTITY_AND_SELF
                .with(() -> entity);
        if (gun.getItem() instanceof ItemGunBase) {
            ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(entity);
            //AnimationController controller = GeckoLibUtil.getControllerForStack(this.getFactory(), gun, this.getFactoryName());
            int ammo = getRemainingAmmo(gun);
            if (ammo > 0) {
                if (capability.getDelay(hand) <= 0) {
                    if (!entity.isCreative()) {
                        useAmmo(gun);
                    }
                    if (!world.isClientSide()) {
                        world.playSound(null, entity.getX(), entity.getY(),
                                entity.getZ(), this.fireSound, SoundSource.PLAYERS, 1.0F,
                                1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 0.25F * 0.5F);

                        GeckoLibNetwork.syncAnimation(receiver, this, id, FIRING);

                        this.spawnProjectile(entity, world, gun, this.accuracy, this.damage, target, hand);
                        capability.setDelay(hand, this.getMinFireDelay());
                    }

                }
                return false;
            } else {
                GeckoLibNetwork.syncAnimation(receiver, this, id, RELOADING);
                if (!world.isClientSide()) {
                    capability.setDelay(hand, this.reloadDelay - this.minFireDelay);
                }

                ItemStack AmmoStack = ItemStack.EMPTY;

                for (int i = 0; i < entity.getInventory().getContainerSize(); i++) {
                    Item MagCandidate = entity.getInventory().getItem(i).getItem();

                    if (MagCandidate == ((ItemGunBase) gun.getItem()).getMagItem()) {
                        if (getRemainingAmmo(entity.getInventory().getItem(i)) > 0) {
                            AmmoStack = entity.getInventory().getItem(i);
                        }
                    }
                }

                if (!AmmoStack.isEmpty() || entity.isCreative()) {

                    int i;
                    if(entity.isCreative()){
                        if(this.roundsPerReload > 0){
                            i=this.roundsPerReload;
                        }
                        else{
                            i=this.magCap;
                        }
                    }else if (this.roundsPerReload > 0) {
                        i = Math.min(this.roundsPerReload, getRemainingAmmo(AmmoStack));
                    } else {
                        i = Math.min(this.magCap, getRemainingAmmo(AmmoStack));
                    }
                    addAmmo(gun, i);
                    if(!entity.isCreative()){
                        AmmoStack.shrink(1);
                        ItemStack EmptyMag = new ItemStack(((ItemGunBase) gun.getItem()).getMagItem());
                        emptyAmmo(EmptyMag);
                        entity.getInventory().add(EmptyMag);
                    }
                    return true;
                }
                world.playSound(null, entity.getX(), entity.getY(),
                        entity.getZ(), registerSounds.GUN_CLICK, SoundSource.PLAYERS, 1.0F,
                        1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 0.25F * 0.5F);
                capability.setDelay(hand, 30);
            }
        }
        return false;
    }

    public boolean shootGunLivingEntity(ItemStack gun, Level world, LivingEntity entity, boolean zooming, InteractionHand hand, @Nullable Entity target) {
        entity.playSound(this.fireSound, 1.0F, (getRand().nextFloat() - getRand().nextFloat()) * 0.2F + 1.0F);
        if (!world.isClientSide()) {
            this.spawnProjectile(entity, world, gun, this.accuracy, this.damage, target, hand);
            return true;
        }
        return false;
    }

    public void shootGunCompanion(ItemStack gun, Level world, AbstractEntityCompanion entity, boolean zooming, InteractionHand hand, @Nullable Entity target) {
        entity.playSound(this.fireSound, 1.0F, (getRand().nextFloat() - getRand().nextFloat()) * 0.2F + 1.0F);
        if (!world.isClientSide()) {

            float inaccuracymultiplier;
            float damagemultiplier;
            world.playSound(null, entity.getX(), entity.getY(),
                    entity.getZ(), this.fireSound, SoundSource.NEUTRAL, 1.0F,
                    1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 0.25F * 0.5F);
            if(entity.getGunSpecialty() != enums.GunClass.NONE) {
                inaccuracymultiplier = entity.getGunSpecialty() == this.getGunClass() ? 0.9F : 1.2F;
                damagemultiplier = entity.getGunSpecialty() == this.getGunClass() ? 1.1F : 0.85F;
            }
            else{
                inaccuracymultiplier = 1.25F;
                damagemultiplier = 0.7F;
            }

            this.spawnProjectile(entity, world, gun, this.accuracy*inaccuracymultiplier, this.damage*damagemultiplier, target, hand);
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);
        if (this.allowdedIn(group)) {
            ItemStack stack = new ItemStack(this);
            ItemStackUtils.setAmmoFull(stack);
            items.add(stack);
        }
    }

    public abstract enums.AmmoCalibur getAmmoType();

    private void spawnProjectile(LivingEntity Shooter, Level worldIn, ItemStack gunStack, float Accuracy, float Damage, Entity target, InteractionHand hand) {
        EntityProjectileBullet entity = new EntityProjectileBullet(Shooter, worldIn, Damage);
        if(target!=null){
            double d0 = target.getEyeY() - (double)1.1F;
            double d1 = target.getX() - Shooter.getX();
            double d2 = d0 - entity.getY();
            double d3 = target.getZ() - Shooter.getZ();
            float f = Mth.sqrt((float) (d1 * d1 + d3 * d3)) * 0.2F;
            entity.shoot(d1, d2 + (double)f, d3, 10.0f, Accuracy);
        }
        else{
            entity.ShootFromPlayer(Shooter, Shooter.getXRot(), Shooter.getYRot(), 0.0f, 5.0F, Accuracy, hand);

        }
        worldIn.addFreshEntity(entity);


    }

    @Override
    public abstract void onAnimationSync(int id, int state);

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.tooltip.gunclass").append(": ").withStyle(ChatFormatting.GRAY).append(new TranslatableComponent(this.getGunClass().getName()).withStyle(ChatFormatting.BLUE)));
        if (worldIn != null && worldIn.isClientSide) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        ChatFormatting color;

        if(((float)getRemainingAmmo(stack)/this.magCap)<0.3F){
            color = ChatFormatting.RED;
        }
        else if(((float)getRemainingAmmo(stack)/this.magCap)<0.6F){
            color = ChatFormatting.YELLOW;
        }
        else{
            color = ChatFormatting.GREEN;
        }

        tooltip.add(new TranslatableComponent("item.tooltip.remainingammo").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(getRemainingAmmo(stack)+"/"+this.magCap).withStyle(color)));
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<?> controller = new AnimationController(this, this.controllerName, 1, this::predicate);
        animationData.addAnimationController(controller);
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        // Not setting an animation here as that's handled in shootGun()
        return PlayState.CONTINUE;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) ((1-((float)getRemainingAmmo(stack)/this.magCap))*13);
    }



    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
