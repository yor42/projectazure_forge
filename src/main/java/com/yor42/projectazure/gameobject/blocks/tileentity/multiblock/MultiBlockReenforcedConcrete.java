package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.setup.register.registerTE.MULTIBLOCKSTRUCTURE_CONCRETE;

public class MultiBlockReenforcedConcrete extends MultiblockStructuralBlockTE {
    public MultiBlockReenforcedConcrete() {
        super(MULTIBLOCKSTRUCTURE_CONCRETE.get());
    }
}
