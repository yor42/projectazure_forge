package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.client.gui.GuiTeamFormation;
import com.yor42.projectazure.gameobject.blocks.PantryBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DimensionType;
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
                    RegistryKey<World> dim = player.getCommandSenderWorld().dimension();
                    if (state.isBed(world, pos, null) && dim == World.OVERWORLD) {
                        pos = block instanceof BedBlock ? state.getValue(BedBlock.PART) == BedPart.HEAD ? pos : pos.relative(state.getValue(BedBlock.FACING)) : pos;
                        NBTTag.putInt("X", pos.getX());
                        NBTTag.putInt("Y", pos.getY());
                        NBTTag.putInt("Z", pos.getZ());
                        NBTTag.putString("Dim", dim.location().toString());
                        NBTTag.putString("postype", POSITION_TYPE.BED.name());
                        player.displayClientMessage(new TranslationTextComponent("message.commandstick.bedpos_saved", "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]"), true);
                    }else if(block instanceof PantryBlock){
                        NBTTag.putInt("X", pos.getX());
                        NBTTag.putInt("Y", pos.getY());
                        NBTTag.putInt("Z", pos.getZ());
                        NBTTag.putString("Dim", dim.location().toString());
                        NBTTag.putString("postype", POSITION_TYPE.PANTRY.name());
                        player.displayClientMessage(new TranslationTextComponent("message.commandstick.pantrypos_saved", "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]"), true);
                    } else {
                        NBTTag.remove("X");
                        NBTTag.remove("Y");
                        NBTTag.remove("Z");
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
        if (compound.contains("X") && compound.contains("Y") && compound.contains("Z")) {
            tooltip.add(new TranslationTextComponent("item.tooltip.bed_position", "["+ compound.getInt("X")+", "+ compound.getInt("Y")+", "+ compound.getInt("Z") +"]"));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public enum POSITION_TYPE{
        BED,
        PANTRY
    }
}
