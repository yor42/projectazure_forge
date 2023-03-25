package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.gameobject.entity.planes.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ClientUtils;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import net.minecraft.world.item.Item.Properties;

public abstract class ItemEquipmentPlaneBase extends ItemEquipmentBase{

    protected float damage;

    public ItemEquipmentPlaneBase(Properties properties, int maxHP, float damage) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.PLANE;
        this.damage = damage;
    }

    @Override
    public void onUpdate(ItemStack EquipmentStack, ItemStack RiggingStack) {
        CompoundTag compound = EquipmentStack.getOrCreateTag();
        int currentFuel = compound.getInt("fuel");
        currentFuel = Math.min(currentFuel+FuelPerTick(), this.getMaxOperativeTime());

        compound.putInt("fuel", currentFuel);

        if(!compound.getBoolean("isArmed")){
            int Armdelay = compound.getInt("armDelay");
            if(Armdelay > 0){
                Armdelay--;
            }
            compound.putInt("armDelay", Armdelay);
        }

    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        stack.getOrCreateTag().putString("planeUUID", UUID.randomUUID().toString());
    }

    public float getAttackDamage(){
        return this.damage;
    }

    public abstract EntityType<? extends AbstractEntityPlanes> getEntityType();

    public float getMovementSpeed(){
        return (float) this.getEntityType().create(ClientUtils.getClientWorld()).getAttribute(Attributes.MOVEMENT_SPEED).getValue();
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        ItemStack stack = new ItemStack(this);
        if (this.allowdedIn(group)) {
            items.add(stack.copy());
            stack.getOrCreateTag().putInt("fuel", this.getMaxOperativeTime());
            stack.getOrCreateTag().putBoolean("isArmed", true);
            ItemStackUtils.setAmmoFull(stack);
            items.add(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int currentFuel = stack.getOrCreateTag().getInt("fuel");
        boolean isArmed = stack.getOrCreateTag().getBoolean("isArmed");
        float fuelPercent = (float) currentFuel/this.getMaxOperativeTime();
        ChatFormatting color = ChatFormatting.DARK_GREEN;
        if(fuelPercent<0.6){
            color = ChatFormatting.GOLD;
        }
        else if(fuelPercent<0.3){
            color = ChatFormatting.DARK_RED;
        }
        tooltip.add(isArmed? new TranslatableComponent("item.tooltip.plane_armed").withStyle(ChatFormatting.DARK_GREEN):new TranslatableComponent("item.tooltip.plane_not_armed").withStyle(ChatFormatting.DARK_RED));
        tooltip.add(new TranslatableComponent("item.tooltip.remainingfuel").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(currentFuel+"/"+this.getMaxOperativeTime()).withStyle(color)));
        tooltip.add(new TranslatableComponent("item.tooltip.plane_type").append(": ").withStyle(ChatFormatting.GRAY).append(new TranslatableComponent(this.getType().getName()).withStyle(ChatFormatting.GOLD)));
    }

    public abstract int getreloadTime();

    public int FuelPerTick(){
        return 2;
    }

    public abstract enums.PLANE_TYPE getType();

    public abstract int getMaxOperativeTime();

}
