/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of Immersive Engineering
 */
package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.utils.BlockMatcher;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FourWayBlock;
import net.minecraft.state.Property;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.vector.Vector3i;

import java.util.List;

import static net.minecraft.state.properties.BlockStateProperties.WATERLOGGED;

public class registerMultiBlock {

    public static void init() {
        //Add general matcher predicates
        //Basic blockstate matcher
        BlockMatcher.addPredicate((expected, found, world, pos) -> expected == found ? BlockMatcher.Result.allow(1) : BlockMatcher.Result.deny(1));
        //FourWayBlock (fences etc): allow additional connections
        List<Property<Boolean>> sideProperties = ImmutableList.of(
                FourWayBlock.NORTH,
                FourWayBlock.EAST,
                FourWayBlock.SOUTH,
                FourWayBlock.WEST
        );
        BlockMatcher.addPredicate((expected, found, world, pos) -> {
            if (expected.getBlock() instanceof FourWayBlock && expected.getBlock() == found.getBlock()) {
                for (Property<Boolean> side : sideProperties)
                    if (expected.get(side) && !found.get(side))
                        return BlockMatcher.Result.deny(2);
                return BlockMatcher.Result.allow(2);
            }
            return BlockMatcher.Result.DEFAULT;
        });
        //Tags
        ImmutableList.Builder<ITag<Block>> genericTagsBuilder = ImmutableList.builder();
        /*
        for(EnumMetals metal : EnumMetals.values())
        {
            MetalTags tags = IETags.getTagsFor(metal);
            genericTagsBuilder.add(tags.storage)
                    .add(tags.sheetmetal);
        }
        genericTagsBuilder.add(IETags.scaffoldingAlu);
        genericTagsBuilder.add(IETags.scaffoldingSteel);
        genericTagsBuilder.add(IETags.treatedWoodSlab);
        genericTagsBuilder.add(IETags.treatedWood);
         */
        List<ITag<Block>> genericTags = genericTagsBuilder.build();
        BlockMatcher.addPredicate((expected, found, world, pos) -> {
            if (expected.getBlock() != found.getBlock())
                for (ITag<Block> t : genericTags)
                    if (expected.isIn(t) && found.isIn(t))
                        return BlockMatcher.Result.allow(3);
            return BlockMatcher.Result.DEFAULT;
        });
        //Ignore hopper facing
        BlockMatcher.addPredicate((expected, found, world, pos) -> {
            if (expected.getBlock() == Blocks.HOPPER && found.getBlock() == Blocks.HOPPER)
                return BlockMatcher.Result.allow(4);
            return BlockMatcher.Result.DEFAULT;
        });
        //Allow multiblocks to be formed under water
        BlockMatcher.addPreprocessor((expected, found, world, pos) -> {
            // Un-waterlog if the expected state is dry, but the found one is not
            if (expected.hasProperty(WATERLOGGED) && found.hasProperty(WATERLOGGED)
                    && !expected.get(WATERLOGGED) && found.get(WATERLOGGED)) {
                return found.with(WATERLOGGED, false);
            } else {
                return found;
            }
        });
    }
}
