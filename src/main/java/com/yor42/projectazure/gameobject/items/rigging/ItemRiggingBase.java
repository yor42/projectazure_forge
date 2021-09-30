package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.gameobject.capability.RiggingInventoryCapability;
import com.yor42.projectazure.gameobject.capability.RiggintFuelTank;
import com.yor42.projectazure.gameobject.items.ItemDestroyable;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentHP;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getHPColor;

public abstract class ItemRiggingBase extends ItemDestroyable implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);

    protected enums.shipClass validclass;

    protected <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return null;
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    public ItemRiggingBase(Properties properties, int HP) {
        super(properties, HP);
        this.addHardPoints();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack,worldIn,tooltip,flagIn);
        RiggingInventoryCapability capability = new RiggingInventoryCapability(stack);
        RiggintFuelTank fueltank = capability.getFuelTank();
        TextFormatting color;
        if(((float)fueltank.getFluidAmount()/fueltank.getTankCapacity(0))<0.3F){
            color = TextFormatting.RED;
        }
        else if(((float)fueltank.getFluidAmount()/fueltank.getTankCapacity(0))<0.3F){
            color = TextFormatting.YELLOW;
        }
        else{
            color = TextFormatting.GREEN;
        }
        tooltip.add(new StringTextComponent("HP: "+ getCurrentHP(stack)+"/"+this.getMaxHP()).setStyle(Style.EMPTY.setColor(getHPColor(stack))));
        tooltip.add(new TranslationTextComponent("item.tooltip.remainingfuel").appendString(": ").mergeStyle(TextFormatting.GRAY).append(new StringTextComponent(fueltank.getFluidAmount()+"/"+fueltank.getTankCapacity(0)).appendString("mb").mergeStyle(color)));
        tooltip.add(new TranslationTextComponent("rigging_valid_on.tooltip").appendString(" ").append(new TranslationTextComponent(this.validclass.getName())).setStyle(Style.EMPTY.setColor(Color.fromInt(8900331))));
        if(worldIn != null && worldIn.isRemote) {
            TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, worldIn, tooltip, flagIn, capability));
        }
    }

    public void addHardPoints(){

    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, RiggingInventoryCapability capability){
        ItemStackHandler Equipments = capability.getEquipments();
        Color CategoryColor = Color.fromHex("#6bb82d");
        for(int i = 0; i<Equipments.getSlots(); i++){
            if(this.getMainGunSlotCount()>0) {
                if (i == 0)
                    tooltip.add((new StringTextComponent("===").append(new TranslationTextComponent("equiment.main_gun").append(new StringTextComponent("==="))).setStyle(Style.EMPTY.setColor(CategoryColor))));
            }
            if(this.getSubGunSlotCount()>0) {
                if (i == this.getMainGunSlotCount())
                    tooltip.add((new StringTextComponent("===").append(new TranslationTextComponent("equiment.sub_gun").append(new StringTextComponent("==="))).setStyle(Style.EMPTY.setColor(CategoryColor))));
            }
            if(this.getAASlotCount()>0) {
                if (i == this.getMainGunSlotCount()+this.getSubGunSlotCount())
                    tooltip.add((new StringTextComponent("===").append(new TranslationTextComponent("equiment.anti_air").append(new StringTextComponent("==="))).setStyle(Style.EMPTY.setColor(CategoryColor))));
            }
            if(this.getTorpedoSlotCount()>0) {
                if (i == this.getMainGunSlotCount()+this.getSubGunSlotCount() + this.getAASlotCount())
                    tooltip.add((new StringTextComponent("===").append(new TranslationTextComponent("equiment.torpedo").append(new StringTextComponent("==="))).setStyle(Style.EMPTY.setColor(CategoryColor))));
            }
            ItemStack currentstack = Equipments.getStackInSlot(i);
            if(currentstack != ItemStack.EMPTY && currentstack.getItem() instanceof ItemEquipmentBase)
                tooltip.add(currentstack.getDisplayName().copyRaw().appendString("("+getCurrentHP(currentstack)+"/"+((ItemEquipmentBase)currentstack.getItem()).getMaxHP()+")").setStyle(Style.EMPTY.setColor(getHPColor(currentstack))));
            else {
                tooltip.add((new StringTextComponent("-").append(new TranslationTextComponent("equiment.empty")).appendString("-")).setStyle(Style.EMPTY.setItalic(true).setColor(Color.fromInt(7829367))));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote()) {
            if (playerIn.isSneaking()) {
                RiggingInventoryCapability.openGUI((ServerPlayerEntity)playerIn, playerIn.inventory.getCurrentItem());
                return ActionResult.resultSuccess(itemstack);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public enums.shipClass getValidclass() {
        return validclass;
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    public int getMainGunSlotCount(){return 0;}
    public int getSubGunSlotCount(){return 0;}
    public int getAASlotCount(){return 0;}

    public int getTorpedoSlotCount(){return 0;}

    public int getHangerSlots(){
        return 0;
    }

    public int getFuelTankCapacity(){return 50000;}

    public int getTotalSlotCount(){
        return this.getAASlotCount()+ this.getMainGunSlotCount()+this.getSubGunSlotCount()+this.getTorpedoSlotCount();
    }

    public abstract AnimatedGeoModel getModel();

    public ItemStackHandler getEquipments(ItemStack riggingStack){
        return new RiggingInventoryCapability(riggingStack).getEquipments();
    };

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new RiggingInventoryCapability(stack);
    }

    @Nullable
    public ItemStackHandler getHangers(ItemStack riggingStack){
        return this.getHangerSlots()==0? null:new RiggingInventoryCapability(riggingStack).getHangar();
    }

    public void onUpdate(ItemStack stack) {
        if(stack.getItem() instanceof ItemRiggingBase){

            RiggingInventoryCapability rigginginv = new RiggingInventoryCapability(stack);

            ItemStackHandler equipment = rigginginv.getEquipments();
            ItemStackHandler hanger = rigginginv.getHangar();

            for(int j = 0; j<equipment.getSlots(); j++){
                if(equipment.getStackInSlot(j).getItem() instanceof ItemEquipmentBase) {
                    ItemEquipmentBase item = (ItemEquipmentBase) equipment.getStackInSlot(j).getItem();
                    item.onUpdate(equipment.getStackInSlot(j), stack);
                }
            }
            if(hanger!=null){
                for(int i = 0; i<hanger.getSlots(); i++) {
                    if (hanger.getStackInSlot(i).getItem() instanceof ItemEquipmentBase) {
                        ItemEquipmentBase item = (ItemEquipmentBase) hanger.getStackInSlot(i).getItem();
                        item.onUpdate(hanger.getStackInSlot(i), stack);
                    }
                }
            }
        }
    }

    public ResourceLocation getTexture(){
            return this.getModel().getTextureLocation(null);
    }
}
