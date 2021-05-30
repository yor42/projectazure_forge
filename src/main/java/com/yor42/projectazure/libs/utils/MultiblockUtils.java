/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of Immersive Engineering
 */
package com.yor42.projectazure.libs.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.interfaces.IMultiBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiblockUtils {

    private static final Direction[] DIRECTIONS = Direction.values();

    static ArrayList<IMultiBlock> multiblocks = new ArrayList<>();
    static Map<ResourceLocation, IMultiBlock> byUniqueName = new HashMap<>();

    public static void registerMultiblock(IMultiBlock multiblock)
    {
        multiblocks.add(multiblock);
        byUniqueName.put(multiblock.getUniqueName(), multiblock);
    }

    public static ArrayList<IMultiBlock> getMultiblocks()
    {
        return multiblocks;
    }

    @Nullable
    public static IMultiBlock getByUniqueName(ResourceLocation name)
    {
        return byUniqueName.get(name);
    }

    public static MultiblockFormEvent postMultiblockFormationEvent(PlayerEntity player, IMultiBlock multiblock, BlockPos clickedBlock, ItemStack hammer)
    {
        MultiblockFormEvent event = new MultiblockFormEvent(player, multiblock, clickedBlock, hammer);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    /**
     * This event is fired BEFORE the multiblock is attempted to be formed.<br>
     * No checks of the structure have been made. The event simply exists to cancel the formation of the multiblock before it ever happens.
     */
    @Cancelable
    public static class MultiblockFormEvent extends PlayerEvent
    {
        private final IMultiBlock multiblock;
        private final BlockPos clickedBlock;
        private final ItemStack hammer;

        public MultiblockFormEvent(PlayerEntity player, IMultiBlock multiblock, BlockPos clickedBlock, ItemStack hammer)
        {
            super(player);
            this.multiblock = multiblock;
            this.clickedBlock = clickedBlock;
            this.hammer = hammer;
        }

        public IMultiBlock getMultiblock()
        {
            return multiblock;
        }

        public BlockPos getClickedBlock()
        {
            return clickedBlock;
        }

        public ItemStack getHammer()
        {
            return hammer;
        }
    }


}
