package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityDrone;
import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getHPColor;

public abstract class AbstractItemPlaceableDrone extends ItemDestroyable implements IAnimatable, ICraftingTableReloadable {
    private final int AmmoCount;
    private final int maxFuel;
    public AbstractItemPlaceableDrone(Properties properties, int MaxHP, int AmmoCount, int maxFuelmb) {
        super(properties, MaxHP);
        this.AmmoCount = AmmoCount;
        this.maxFuel = maxFuelmb;
    }

    @Override
    public void onCraftedBy(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        stack.getOrCreateTag().putString("planeUUID", UUID.randomUUID().toString());
    }


    public abstract EntityType<? extends AbstractEntityDrone> getEntityType();

    public abstract int getreloadDelay();

    @Override
    public int getMaxAmmo() {
        return this.AmmoCount;
    }

    @Override
    public enums.AmmoCalibur getAmmoType() {
        return enums.AmmoCalibur.DRONE_MISSILE;
    }

    public int getFuelCapacity(){
        return this.maxFuel;
    }

    public void AddInfotoDrone(ItemStack droneItem, AbstractEntityDrone drone){
        CompoundNBT stackCompound = droneItem.getOrCreateTag();
        if(stackCompound.contains("planedata")) {
            drone.readAdditionalSaveData(stackCompound.getCompound("planedata"));
        }
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if(group == this.getItemCategory()) {
            ItemStack stack = new ItemStack(this);
            ItemStackUtils.setCurrentHP(stack, this.getMaxHP());
            stack.getOrCreateTag().putInt("fuel", this.getFuelCapacity());
            ItemStackUtils.setAmmoFull(stack);
            items.add(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int currentFuel = stack.getOrCreateTag().getInt("fuel");
        float fuelPercent = (float) currentFuel/this.getFuelCapacity();
        TextFormatting color = TextFormatting.DARK_GREEN;
        if(fuelPercent<0.6){
            color = TextFormatting.GOLD;
        }
        else if(fuelPercent<0.3){
            color = TextFormatting.DARK_RED;
        }
        tooltip.add(new StringTextComponent("HP: "+ getCurrentHP(stack)+"/"+this.getMaxHP()).setStyle(Style.EMPTY.withColor(getHPColor(stack))));
        tooltip.add(new TranslationTextComponent("item.tooltip.remainingfuel").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(currentFuel+"/"+this.getFuelCapacity()).withStyle(color)));

    }

    @Nullable
    public AbstractEntityDrone CreateDrone(World world, ItemStack stack, LivingEntity owner){
        AbstractEntityDrone DroneEntity = this.getEntityType().create(world);
        if(DroneEntity != null) {
            this.AddInfotoDrone(stack, DroneEntity);
            DroneEntity.setOwner(owner);
            DroneEntity.setPos(owner.getX(), owner.getY(), owner.getZ());
            DroneEntity.setHealth(ItemStackUtils.getCurrentHP(stack));
            DroneEntity.setAmmo(ItemStackUtils.getRemainingAmmo(stack));
            DroneEntity.setRemainingFuel(stack.getOrCreateTag().getInt("fuel"));
            return DroneEntity;
        }
        return null;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {

        if(!context.getLevel().isClientSide() && context.getPlayer() != null && context.getPlayer().isShiftKeyDown()){
            AbstractEntityDrone DroneEntity = this.CreateDrone(context.getLevel(), context.getItemInHand(), context.getPlayer());
            if(DroneEntity != null) {
                ItemStack stack = context.getItemInHand();
                context.getLevel().addFreshEntity(DroneEntity);
                if (!context.getPlayer().isCreative()) {
                    stack.shrink(1);
                }
            }
            return ActionResultType.CONSUME;
        }

        return super.useOn(context);
    }
}
