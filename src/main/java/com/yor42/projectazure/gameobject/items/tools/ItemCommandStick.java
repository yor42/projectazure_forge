package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.client.gui.GuiTeamFormation;
import com.yor42.projectazure.client.gui.guiStarterSpawn;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.Main.PA_GROUP;

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
            else if(world.isClientSide){
                //openGui
                this.openGUI();
                return ActionResultType.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    @OnlyIn(Dist.CLIENT)
    private void openGUI(){
        Minecraft.getInstance().setScreen(new GuiTeamFormation(new TranslationTextComponent("gui.teamformation")));
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
