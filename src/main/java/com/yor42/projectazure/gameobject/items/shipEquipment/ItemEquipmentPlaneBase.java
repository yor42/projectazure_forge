package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.gameobject.entity.planes.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ClientUtils;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class ItemEquipmentPlaneBase extends ItemEquipmentBase{

    protected float damage;

    public ItemEquipmentPlaneBase(Properties properties, int maxHP, float damage) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.PLANE;
        this.damage = damage;
    }

    @Override
    public void onUpdate(ItemStack EquipmentStack, ItemStack RiggingStack) {
        CompoundNBT compound = EquipmentStack.getOrCreateTag();
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
    public void onCraftedBy(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        stack.getOrCreateTag().putString("planeUUID", UUID.randomUUID().toString());
    }

    public float getAttackDamage(){
        return this.damage;
    };

    public abstract EntityType<? extends AbstractEntityPlanes> getEntityType();

    public float getMovementSpeed(){
        return (float) this.getEntityType().create(ClientUtils.getClientWorld()).getAttribute(Attributes.MOVEMENT_SPEED).getValue();
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int currentFuel = stack.getOrCreateTag().getInt("fuel");
        boolean isArmed = stack.getOrCreateTag().getBoolean("isArmed");
        float fuelPercent = (float) currentFuel/this.getMaxOperativeTime();
        TextFormatting color = TextFormatting.DARK_GREEN;
        if(fuelPercent<0.6){
            color = TextFormatting.GOLD;
        }
        else if(fuelPercent<0.3){
            color = TextFormatting.DARK_RED;
        }
        tooltip.add(isArmed? new TranslationTextComponent("item.tooltip.plane_armed").withStyle(TextFormatting.DARK_GREEN):new TranslationTextComponent("item.tooltip.plane_not_armed").withStyle(TextFormatting.DARK_RED));
        tooltip.add(new TranslationTextComponent("item.tooltip.remainingfuel").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(currentFuel+"/"+this.getMaxOperativeTime()).withStyle(color)));
        tooltip.add(new TranslationTextComponent("item.tooltip.plane_type").append(": ").withStyle(TextFormatting.GRAY).append(new TranslationTextComponent(this.getType().getName()).withStyle(TextFormatting.GOLD)));
    }

    public abstract int getreloadTime();

    public int FuelPerTick(){
        return 2;
    }

    public abstract enums.PLANE_TYPE getType();

    public abstract int getMaxOperativeTime();

}
