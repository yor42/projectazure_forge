package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.client.gui.GuiTeamFormation;
import com.yor42.projectazure.gameobject.blocks.PantryBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
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
    public InteractionResult useOn(@Nonnull UseOnContext context) {

        Player player = context.getPlayer();
        ItemStack holdingStack = context.getItemInHand();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if(player != null){
            if(player.isShiftKeyDown()){

                if(context.getLevel().isClientSide()){
                    return InteractionResult.CONSUME;
                }

                if(!context.getLevel().isClientSide()) {
                    CompoundTag NBTTag = holdingStack.getOrCreateTag();
                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();
                    ResourceKey<Level> dim = player.getLevel().dimension();
                    if (state.isBed(world, pos, null) && dim == Level.OVERWORLD) {
                        pos = block instanceof BedBlock ? state.getValue(BedBlock.PART) == BedPart.HEAD ? pos : pos.relative(state.getValue(BedBlock.FACING)) : pos;
                        NBTTag.putInt("X", pos.getX());
                        NBTTag.putInt("Y", pos.getY());
                        NBTTag.putInt("Z", pos.getZ());
                        NBTTag.putString("Dim", dim.location().toString());
                        NBTTag.putString("postype", POSITION_TYPE.BED.name());
                        player.displayClientMessage(new TranslatableComponent("message.commandstick.bedpos_saved", "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]"), true);
                    }else if(block instanceof PantryBlock){
                        NBTTag.putInt("X", pos.getX());
                        NBTTag.putInt("Y", pos.getY());
                        NBTTag.putInt("Z", pos.getZ());
                        NBTTag.putString("Dim", dim.location().toString());
                        NBTTag.putString("postype", POSITION_TYPE.PANTRY.name());
                        player.displayClientMessage(new TranslatableComponent("message.commandstick.pantrypos_saved", "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]"), true);
                    } else {
                        NBTTag.remove("X");
                        NBTTag.remove("Y");
                        NBTTag.remove("Z");
                        player.displayClientMessage(new TranslatableComponent("message.commandstick.bedpos_cleared"), true);
                    }
                    holdingStack.setTag(NBTTag);
                }
            }
            else if(world.isClientSide){
                //openGui
                this.openGUI();
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    @OnlyIn(Dist.CLIENT)
    private void openGUI(){
        Minecraft.getInstance().setScreen(new GuiTeamFormation(new TranslatableComponent("gui.teamformation")));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag compound = stack.getOrCreateTag();
        if (compound.contains("X") && compound.contains("Y") && compound.contains("Z")) {
            tooltip.add(new TranslatableComponent("item.tooltip.bed_position", "["+ compound.getInt("X")+", "+ compound.getInt("Y")+", "+ compound.getInt("Z") +"]"));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public enum POSITION_TYPE{
        BED,
        PANTRY
    }
}
