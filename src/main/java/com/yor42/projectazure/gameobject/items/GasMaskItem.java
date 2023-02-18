package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.gameobject.items.materials.ModArmorMaterials.ArmorModMaterials.GASMASK;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getHPColor;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getHPColorInt;

public class GasMaskItem extends GeoArmorItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public GasMaskItem(Properties builder) {
        super(GASMASK, EquipmentSlotType.HEAD, builder);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationEvent<?> animationEvent) {
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {

        CompoundNBT compoundNBT = stack.getOrCreateTag();
        if(!compoundNBT.contains("filters")){
            return 1;
        }

        int damage=0, totaldamage=0;

        ListNBT filters = compoundNBT.getList("filters", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i<filters.size(); i++){
            ItemStack filterstack = ItemStack.of(filters.getCompound(i));
            Item item = filterstack.getItem();
            if(item instanceof GasMaskFilterItem){
                damage+=ItemStackUtils.getCurrentDamage(filterstack);
                totaldamage+=((GasMaskFilterItem) item).getMaxHP();
            }
        }

        if (totaldamage == 0) {
            return 1;
        }

        return (double) damage/(double) totaldamage;
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        super.onArmorTick(stack, world, player);
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        ListNBT filters = compoundNBT.getList("filters", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i<filters.size(); i++){
            ItemStack filterstack = ItemStack.of(filters.getCompound(i));
            if(ItemStackUtils.DamageItem(player.isUnderWater()?5F:1F, filterstack)){
                break;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, p_77624_3_, p_77624_4_);
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        ListNBT filters = compoundNBT.getList("filters", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i<filters.size(); i++){
            ItemStack filterstack = ItemStack.of(filters.getCompound(i));
            Item filteritem = filterstack.getItem();
            if(filteritem instanceof GasMaskFilterItem){
                p_77624_3_.add(new TranslationTextComponent("item.projectazure.gasmask.tooltip.filter", i+1, new StringTextComponent(String.format("%.2f", (double)ItemStackUtils.getCurrentHP(filterstack)/(double) ((GasMaskFilterItem) filteritem).getMaxHP()*100)+"%").withStyle(Style.EMPTY.withColor(getHPColor(filterstack)))).withStyle(TextFormatting.GRAY));
                //p_77624_3_.add(new StringTextComponent(Integer.toHexString(ItemStackUtils.getHPColorInt(filterstack))));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {

        if(!p_77659_2_.isCrouching()){
            return super.use(p_77659_1_, p_77659_2_, p_77659_3_);
        }

        ItemStack stack = p_77659_2_.getItemInHand(p_77659_3_);
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        ListNBT filters = compoundNBT.getList("filters", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i<filters.size(); i++){
            ItemStack filterstack = ItemStack.of(filters.getCompound(i));
            int index = p_77659_2_.inventory.getFreeSlot();

            if(index<0){
                p_77659_2_.drop(filterstack, true, true);
            }
            else{
                p_77659_2_.inventory.setItem(index, filterstack);
            }
        }
        filters.clear();
        compoundNBT.put("filters", filters);
        return ActionResult.success(stack);
    }
}
