package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.gameobject.capability.RiggingDefaultDDEquipmentCapability;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;
import java.util.List;

public class itemRiggingDDDefault extends ItemRiggingDD implements IAnimatable {

    public itemRiggingDDDefault(Properties properties, int HP) {
        super(properties, HP);
    }


    @Override
    public AnimatedGeoModel getModel() {
        return new modelDDRiggingDefault();
    }

    @Override
    public ItemStackHandler getEquipments(ItemStack riggingStack) {
        return new RiggingDefaultDDEquipmentCapability(riggingStack).getEquipments();
    }

    @Override
    public int getEquipmentCount(ItemStack riggingStack) {
        return new RiggingDefaultDDEquipmentCapability(riggingStack).getEquipments().getSlots();
    }

    @Override
    public void onTorpedoFire(ItemStack stack) {
        ItemStackHandler equipment = new RiggingDefaultDDEquipmentCapability(stack).getEquipments();
        for (int i = 3; i<6; i++){
            ItemStack TorpedoStack = equipment.getStackInSlot(i);
            ItemEquipmentBase torpedoItem = (ItemEquipmentBase) TorpedoStack.getItem();
            if(torpedoItem.canUseTorpedo(TorpedoStack)) {
                torpedoItem.checkSlotAndFire(TorpedoStack, enums.SLOTTYPE.TORPEDO);
            }
        }
    }

    @Override
    public boolean canUseTorpedo(ItemStack stack) {
        ItemStackHandler equipments = new RiggingDefaultDDEquipmentCapability(stack).getEquipments();
        for (int i = 3; i<6; i++){
            ItemEquipmentBase equipment = (ItemEquipmentBase) equipments.getStackInSlot(i).getItem();
            if(equipment.canUseTorpedo(equipments.getStackInSlot(i))){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canUseCanon(ItemStack stack) {
        ItemStackHandler equipments = new RiggingDefaultDDEquipmentCapability(stack).getEquipments();
        for(int k = 0; k<2; k++){
            ItemEquipmentBase equipment = (ItemEquipmentBase) equipments.getStackInSlot(k).getItem();
            if(equipment.canUseCanon(equipments.getStackInSlot(k))){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCanonFire(ItemStack stack) {
        ItemStackHandler equipment = new RiggingDefaultDDEquipmentCapability(stack).getEquipments();
        for(int j = 0; j<2; j++){
            ItemStack CanonStack = equipment.getStackInSlot(j);
            ItemEquipmentBase CanonItem = (ItemEquipmentBase) CanonStack.getItem();
            if(CanonItem.canUseCanon(CanonStack)){
                CanonItem.checkSlotAndFire(CanonStack, enums.SLOTTYPE.GUN);
            }

        }
    }

    @Override
    public void onUpdate(ItemStack stack) {
        if(stack.getItem() instanceof ItemRiggingDD){
            ItemStackHandler equipment = new RiggingDefaultDDEquipmentCapability(stack).getEquipments();

            for(int j = 0; j<equipment.getSlots(); j++){
                if(equipment.getStackInSlot(j).getItem() instanceof ItemEquipmentBase) {
                    ItemEquipmentBase item = (ItemEquipmentBase) equipment.getStackInSlot(j).getItem();
                    item.onUpdate(equipment.getStackInSlot(j));
                }
            }
        }
    }

    @Override
    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event)
    {
        return PlayState.STOP;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote()) {
            if (playerIn.isSneaking()) {
                RiggingDefaultDDEquipmentCapability.openGUI((ServerPlayerEntity)playerIn, playerIn.inventory.getCurrentItem());
                return ActionResult.resultSuccess(itemstack);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        ItemStackHandler Equipments = new RiggingDefaultDDEquipmentCapability(stack).getEquipments();
        Color CategoryColor = Color.fromHex("#6bb82d");
        tooltip.add(new StringTextComponent(""));
        for(int i = 0; i<Equipments.getSlots(); i++){
                if(i == 0)
                    tooltip.add((new StringTextComponent("===").append(new TranslationTextComponent("rigging.main_gun").append(new StringTextComponent("==="))).setStyle(Style.EMPTY.setColor(CategoryColor))));
                if(i == 2)
                    tooltip.add((new StringTextComponent("===").append(new TranslationTextComponent("rigging.anti_air").append(new StringTextComponent("==="))).setStyle(Style.EMPTY.setColor(CategoryColor))));
                if(i == 3)
                    tooltip.add((new StringTextComponent("===").append(new TranslationTextComponent("rigging.torpedo").append(new StringTextComponent("==="))).setStyle(Style.EMPTY.setColor(CategoryColor))));

                if(Equipments.getStackInSlot(i) != ItemStack.EMPTY)
                    tooltip.add(Equipments.getStackInSlot(i).getDisplayName());
                else {
                    tooltip.add((new StringTextComponent("-").append(new TranslationTextComponent("rigging.empty")).appendString("-")).setStyle(Style.EMPTY.setItalic(true).setColor(Color.fromInt(7829367))));
                }
        }
    }
}
