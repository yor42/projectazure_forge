/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of Immersive Engineering
 */
package com.yor42.projectazure.interfaces;

import com.mojang.blaze3d.matrix.MatrixStack;
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

import javax.annotation.Nullable;
import java.util.List;

public interface IMultiBlock {

    /**
     * @return name of the Multiblock. This is used for the interdiction NBT system on the hammer, so this name /must/ be unique.
     */
    ResourceLocation getUniqueName();

    /**
     * Check whether the given block can be used to trigger the structure creation of the multiblock.<br>
     * Basically, a less resource-intensive preliminary check to avoid checking every structure.
     */
    boolean isBlockTrigger(BlockState state, Direction side, @Nullable World world);

    /**
     * This method checks the structure and sets the new one.
     *
     * @return if the structure was valid and transformed
     */
    boolean createStructure(World world, BlockPos pos, Direction side, PlayerEntity player);

    List<BlockInfo> getStructure(@Nullable World world);

    /**
     * An array of ItemStacks that summarizes the total amount of materials needed for the structure. Will be rendered in the Engineer's Manual
     *
     * @return
     */
    @OnlyIn(Dist.CLIENT)
    ItemStack[] getTotalMaterials();

    /**
     * Use this to overwrite the rendering of a Multiblock's Component
     */
    @OnlyIn(Dist.CLIENT)
    boolean overwriteBlockRender(BlockState state, int iterator);

    /**
     * returns the scale modifier to be applied when rendering the structure in the IE manual
     */
    float getManualScale();

    /**
     * returns true to add a button that will switch between the assembly of multiblocks and the finished render
     */
    @OnlyIn(Dist.CLIENT)
    boolean canRenderFormedStructure();

    /**
     * use this function to render the complete multiblock
     *
     * @param transform Matrixstack of formed structure
     * @param buffer IrenderTypeBuffer of formed structure
     */
    @OnlyIn(Dist.CLIENT)
    void renderFormedStructure(MatrixStack transform, IRenderTypeBuffer buffer);

    Vector3i getSize(@Nullable World world);

    void disassemble(World world, BlockPos startPos, boolean mirrored, Direction clickDirectionAtCreation);

    BlockPos getTriggerOffset();

}
