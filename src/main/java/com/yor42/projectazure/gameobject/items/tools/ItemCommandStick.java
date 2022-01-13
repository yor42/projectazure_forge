package com.yor42.projectazure.gameobject.items.tools;

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
        super(new Item.Properties().tab(PA_GROUP).stacksTo(1));
    }

    @Nonnull
    @Override
    public ActionResultType useOn(@Nonnull ItemUseContext context) {

        PlayerEntity player = context.getPlayer();
        ItemStack holdingStack = context.getItemInHand();
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if(player != null){
            if(player.isShiftKeyDown()){

                if(context.getLevel().isClientSide()){
                    return ActionResultType.CONSUME;
                }

                if(!context.getLevel().isClientSide()) {
                    CompoundNBT NBTTag = holdingStack.getOrCreateTag();
                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();
                    if (state.isBed(world, pos, null)) {
                        pos = block instanceof BedBlock ? state.getValue(BedBlock.PART) == BedPart.HEAD ? pos : pos.relative(state.getValue(BedBlock.FACING)) : pos;
                        NBTTag.putInt("BedX", pos.getX());
                        NBTTag.putInt("BedY", pos.getY());
                        NBTTag.putInt("BedZ", pos.getZ());
                        player.displayClientMessage(new TranslationTextComponent("message.commandstick.bedpos_saved", "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]"), true);
                    } else {
                        NBTTag.remove("BedX");
                        NBTTag.remove("BedY");
                        NBTTag.remove("BedZ");
                        player.displayClientMessage(new TranslationTextComponent("message.commandstick.bedpos_cleared"), true);
                    }
                    holdingStack.setTag(NBTTag);
                }
            }
        }
        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT compound = stack.getOrCreateTag();
        if (compound.contains("BedX") && compound.contains("BedY") && compound.contains("BedZ")) {
            tooltip.add(new TranslationTextComponent("item.tooltip.bed_position", "["+ compound.getInt("BedX")+", "+ compound.getInt("BedY")+", "+ compound.getInt("BedZ") +"]"));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
