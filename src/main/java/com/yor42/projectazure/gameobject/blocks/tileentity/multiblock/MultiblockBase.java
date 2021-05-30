/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of Immersive Engineering.
 */
package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.interfaces.IMultiBlock;
import com.yor42.projectazure.libs.utils.BlockMatcher;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.libs.utils.SetRestrictedField;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.gen.feature.template.Template.Palette;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;


public abstract class MultiblockBase implements IMultiBlock {
    private static final SetRestrictedField<Function<BlockState, ItemStack>> PICK_BLOCK = SetRestrictedField.common();
    private static final SetRestrictedField<BiFunction<ResourceLocation, MinecraftServer, Template>>
            LOAD_TEMPLATE = SetRestrictedField.common();
    private static final SetRestrictedField<Function<Template, List<Template.Palette>>>
            GET_PALETTES = SetRestrictedField.common();
    private static final Logger LOGGER = LogManager.getLogger();
    private final ResourceLocation loc;
    protected final BlockPos masterFromOrigin;
    protected final BlockPos triggerFromOrigin;
    protected final BlockPos size;
    protected final List<BlockMatcher.MatcherPredicate> additionalPredicates;
    @Nullable
    private Template template;
    @Nullable
    private ItemStack[] materials;
    private BlockState trigger = Blocks.AIR.getDefaultState();

    public MultiblockBase(ResourceLocation loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin, BlockPos size,
                              List<BlockMatcher.MatcherPredicate> additionalPredicates)
    {
        this.loc = loc;
        this.masterFromOrigin = masterFromOrigin;
        this.triggerFromOrigin = triggerFromOrigin;
        this.size = size;
        this.additionalPredicates = additionalPredicates;
    }

    public MultiblockBase(ResourceLocation loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin, BlockPos size)
    {
        this(loc, masterFromOrigin, triggerFromOrigin, size, ImmutableMap.of());
    }

    public MultiblockBase(ResourceLocation loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin, BlockPos size, Map<Block, ITag<Block>> tags)
    {
        this(loc, masterFromOrigin, triggerFromOrigin, size, ImmutableList.of(
                (expected, found, world, pos) -> {
                    ITag<Block> tag = tags.get(expected.getBlock());
                    if(tag!=null)
                    {
                        if(found.isIn(tag))
                            return BlockMatcher.Result.allow(2);
                        else
                            return BlockMatcher.Result.deny(2);
                    }
                    else
                        return BlockMatcher.Result.DEFAULT;
                }
        ));
    }

    @Nonnull
    protected Template getTemplate(@Nullable World world)
    {
        return getTemplate(world==null?null: world.getServer());
    }

    public ResourceLocation getTemplateLocation()
    {
        return loc;
    }

    @Nonnull
    public Template getTemplate(@Nullable MinecraftServer server)
    {
        if(template==null)
        {
            template = LOAD_TEMPLATE.getValue().apply(loc, server);
            List<BlockInfo> blocks = getStructureFromTemplate(template);
            for(int i = 0; i < blocks.size(); i++)
            {
                BlockInfo info = blocks.get(i);
                if(info.pos.equals(triggerFromOrigin))
                    trigger = info.state;
                if(info.state==Blocks.AIR.getDefaultState())
                {
                    blocks.remove(i);
                    i--;
                }
                else if(info.state.isAir())
                    // Usually means it contains a block that has been renamed
                    LOGGER.error("Found non-default air block in template {}", loc);
            }
            materials = null;
        }
        return Objects.requireNonNull(template);
    }

    public void reset()
    {
        template = null;
    }

    public ResourceLocation getUniqueName()
    {
        return loc;
    }

    public boolean isBlockTrigger(BlockState state, Direction d, @Nullable World world)
    {
        getTemplate(world);
        Rotation rot = MathUtil.getRotationBetweenFacings(Direction.NORTH, d.getOpposite());
        if(rot==null)
            return false;
        for(Mirror mirror : getPossibleMirrorStates())
        {
            BlockState modifiedTrigger = applyToState(trigger, mirror, rot);
            if(BlockMatcher.matches(modifiedTrigger, state, null, null, additionalPredicates).isAllow())
                return true;
        }
        return false;
    }

    public boolean createStructure(World world, BlockPos pos, Direction side, PlayerEntity player)
    {
        Rotation rot = MathUtil.getRotationBetweenFacings(Direction.NORTH, side.getOpposite());
        if(rot==null)
            return false;
        List<BlockInfo> structure = getStructure(world);
        mirrorLoop:
        for(Mirror mirror : getPossibleMirrorStates())
        {
            PlacementSettings placeSet = new PlacementSettings().setMirror(mirror).setRotation(rot);
            BlockPos origin = pos.subtract(Template.transformedBlockPos(placeSet, triggerFromOrigin));
            for(BlockInfo info : structure)
            {
                BlockPos realRelPos = Template.transformedBlockPos(placeSet, info.pos);
                BlockPos here = origin.add(realRelPos);

                BlockState expected = applyToState(info.state, mirror, rot);
                BlockState inWorld = world.getBlockState(here);
                if(!BlockMatcher.matches(expected, inWorld, world, here, additionalPredicates).isAllow())
                    continue mirrorLoop;
            }
            form(world, origin, rot, mirror, side);
            return true;
        }
        return false;
    }

    private BlockState applyToState(BlockState in, Mirror m, Rotation r)
    {
        return in.mirror(m).rotate(r);
    }

    private List<Mirror> getPossibleMirrorStates()
    {
        if(canBeMirrored())
            return ImmutableList.of(Mirror.NONE, Mirror.FRONT_BACK);
        else
            return ImmutableList.of(Mirror.NONE);
    }

    protected void form(World world, BlockPos pos, Rotation rot, Mirror mirror, Direction sideHit)
    {
        BlockPos masterPos = withSettingsAndOffset(pos, masterFromOrigin, mirror, rot);
        for(BlockInfo block : getStructure(world))
        {
            BlockPos actualPos = withSettingsAndOffset(pos, block.pos, mirror, rot);
            replaceStructureBlock(block, world, actualPos, mirror!=Mirror.NONE, sideHit,
                    actualPos.subtract(new Vector3i(masterPos.getX(), masterPos.getY(), masterPos.getZ())));
        }
    }

    public BlockPos getMasterFromOriginOffset()
    {
        return masterFromOrigin;
    }

    protected abstract void replaceStructureBlock(BlockInfo info, World world, BlockPos actualPos, boolean mirrored, Direction clickDirection, Vector3i offsetFromMaster);

    public List<BlockInfo> getStructure(@Nullable World world)
    {
        return getStructureFromTemplate(getTemplate(world));
    }

    private static List<BlockInfo> getStructureFromTemplate(Template template)
    {
        return GET_PALETTES.getValue().apply(template).get(0).func_237157_a_();
    }

    public Vector3i getSize(@Nullable World world)
    {
        return getTemplate(world).getSize();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean overwriteBlockRender(BlockState state, int iterator)
    {
        return false;
    }

    public static BlockPos withSettingsAndOffset(BlockPos origin, BlockPos relative, Mirror mirror, Rotation rot)
    {
        PlacementSettings settings = new PlacementSettings().setMirror(mirror).setRotation(rot);
        return origin.add(Template.transformedBlockPos(settings, relative));
    }

    public static BlockPos withSettingsAndOffset(BlockPos origin, BlockPos relative, boolean mirrored, Direction facing)
    {
        Rotation rot = MathUtil.getRotationBetweenFacings(Direction.NORTH, facing);
        if(rot==null)
            return origin;
        return withSettingsAndOffset(origin, relative, mirrored?Mirror.FRONT_BACK: Mirror.NONE,
                rot);
    }

    public ItemStack[] getTotalMaterials()
    {
        if(materials==null)
        {
            List<BlockInfo> structure = getStructure(null);
            List<ItemStack> ret = new ArrayList<>(structure.size());
            for(BlockInfo info : structure)
            {
                ItemStack picked = PICK_BLOCK.getValue().apply(info.state);
                boolean added = false;
                for(ItemStack existing : ret)
                    if(ItemStack.areItemsEqual(existing, picked))
                    {
                        existing.grow(1);
                        added = true;
                        break;
                    }
                if(!added)
                    ret.add(picked.copy());
            }
            materials = ret.toArray(new ItemStack[0]);
        }
        return materials;
    }

    public void disassemble(World world, BlockPos origin, boolean mirrored, Direction clickDirectionAtCreation)
    {
        Mirror mirror = mirrored?Mirror.FRONT_BACK: Mirror.NONE;
        Rotation rot = MathUtil.getRotationBetweenFacings(Direction.NORTH, clickDirectionAtCreation);
        Preconditions.checkNotNull(rot);
        for(BlockInfo block : getStructure(world))
        {
            BlockPos actualPos = withSettingsAndOffset(origin, block.pos, mirror, rot);
            prepareBlockForDisassembly(world, actualPos);
            world.setBlockState(actualPos, block.state.mirror(mirror).rotate(rot));
        }
    }

    protected void prepareBlockForDisassembly(World world, BlockPos pos)
    {
    }

    public BlockPos getTriggerOffset()
    {
        return triggerFromOrigin;
    }

    public boolean canBeMirrored()
    {
        return true;
    }

    public static void setCallbacks(
            Function<BlockState, ItemStack> pickBlock,
            BiFunction<ResourceLocation, MinecraftServer, Template> loadTemplate,
            Function<Template, List<Palette>> getPalettes
    )
    {
        PICK_BLOCK.setValue(pickBlock);
        LOAD_TEMPLATE.setValue(loadTemplate);
        GET_PALETTES.setValue(getPalettes);
    }

}
