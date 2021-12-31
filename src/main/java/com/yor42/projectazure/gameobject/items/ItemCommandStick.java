package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.renderer.items.BBDefaultRiggingRenderer;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.UUID;

import static com.yor42.projectazure.Main.PA_GROUP;
import static com.yor42.projectazure.Main.PA_WEAPONS;

public class ItemCommandStick extends Item {
    public ItemCommandStick() {
        super(new Item.Properties().group(PA_GROUP).maxStackSize(1));
    }

    @Nonnull
    @Override
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {

        PlayerEntity player = context.getPlayer();
        ItemStack holdingStack = context.getItem();
        World world = context.getWorld();
        BlockPos pos = context.getPos();

        if(player != null){
            if(player.isSneaking()){

                if(context.getWorld().isRemote()){
                    return ActionResultType.CONSUME;
                }

                if(!context.getWorld().isRemote()) {
                    CompoundNBT NBTTag = holdingStack.getOrCreateTag();
                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();
                    if (state.isBed(world, pos, null)) {
                        pos = block instanceof BedBlock ? state.get(BedBlock.PART) == BedPart.HEAD ? pos : pos.offset(state.get(BedBlock.HORIZONTAL_FACING)) : pos;
                        NBTTag.putInt("BedX", pos.getX());
                        NBTTag.putInt("BedY", pos.getY());
                        NBTTag.putInt("BedZ", pos.getZ());
                        player.sendStatusMessage(new TranslationTextComponent("message.commandstick.bedpos_saved", "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]"), true);
                    } else {
                        NBTTag.remove("BedX");
                        NBTTag.remove("BedY");
                        NBTTag.remove("BedZ");
                        player.sendStatusMessage(new TranslationTextComponent("message.commandstick.bedpos_cleared"), true);
                    }
                    holdingStack.setTag(NBTTag);
                }
            }
        }
        return super.onItemUse(context);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT compound = stack.getOrCreateTag();
        if (compound.contains("BedX") && compound.contains("BedY") && compound.contains("BedZ")) {
            tooltip.add(new TranslationTextComponent("item.tooltip.bed_position", "["+ compound.getInt("BedX")+", "+ compound.getInt("BedY")+", "+ compound.getInt("BedZ") +"]"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
