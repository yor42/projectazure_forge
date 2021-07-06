package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemEquipmentPlaneBase extends ItemEquipmentBase{

    public ItemEquipmentPlaneBase(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.PLANE;
    }

    @Override
    public void onUpdate(ItemStack stack) {
        CompoundNBT compound = stack.getOrCreateTag();
        int currentFuel = compound.getInt("fuel");
        if (currentFuel+FuelPerTick()<=this.getMaxOperativeTime()){
            currentFuel+=FuelPerTick();
        }
        else if(currentFuel+FuelPerTick()>this.getMaxOperativeTime()){
            currentFuel = this.getMaxOperativeTime();
        }

        compound.putInt("fuel", currentFuel);

        if(!compound.getBoolean("isArmed")){
            int Armdelay = compound.getInt("armDelay");
            if(Armdelay > 0){
                Armdelay--;
            }
            compound.putInt("armDelay", Armdelay);
        }

    }

    public abstract float getAttackDamage();

    public abstract EntityType<? extends AbstractEntityPlanes> getEntityType();

    public float getMovementSpeed(){
        return (float) this.getEntityType().create(ClientProxy.getClientWorld()).getAttribute(Attributes.MOVEMENT_SPEED).getValue();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.tooltip.plane_type").appendString(": ").mergeStyle(TextFormatting.GRAY).append(new TranslationTextComponent(this.getType().getName()).mergeStyle(TextFormatting.YELLOW)));
    }

    public abstract int getreloadTime();

    public int FuelPerTick(){
        return 2;
    }

    public abstract enums.PLANE_TYPE getType();

    public abstract int getMaxOperativeTime();

}
