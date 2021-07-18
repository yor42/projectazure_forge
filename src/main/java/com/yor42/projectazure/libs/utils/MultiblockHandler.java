package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockBaseTE;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MultiblockHandler {

    private static final Block[][][][] MULTIBLOCKPATTERN =
            {
                    {
                            {  { registerBlocks.REENFORCEDCONCRETE.get(), registerBlocks.REENFORCEDCONCRETE.get(), registerBlocks.REENFORCEDCONCRETE.get()}, { registerBlocks.REENFORCEDCONCRETE.get(), Blocks.AIR, registerBlocks.REENFORCEDCONCRETE.get()}, { registerBlocks.MACHINE_FRAME.get(), Blocks.AIR, registerBlocks.MACHINE_FRAME.get()}, {registerBlocks.MACHINE_FRAME.get(),Blocks.AIR,registerBlocks.MACHINE_FRAME.get()}  },	//x = 0
                            {  { registerBlocks.REENFORCEDCONCRETE.get(), registerBlocks.MACHINE_FRAME.get(), registerBlocks.REENFORCEDCONCRETE.get()}, { Blocks.AIR, Blocks.AIR, Blocks.AIR}, { Blocks.AIR, Blocks.AIR, Blocks.AIR}, {Blocks.AIR, registerBlocks.DRYDOCKCONTROLLER.get(), Blocks.AIR}  },	//x = 1
                            {  { registerBlocks.REENFORCEDCONCRETE.get(), registerBlocks.REENFORCEDCONCRETE.get(), registerBlocks.REENFORCEDCONCRETE.get()}, { registerBlocks.REENFORCEDCONCRETE.get(), Blocks.AIR, registerBlocks.REENFORCEDCONCRETE.get()}, { registerBlocks.MACHINE_FRAME.get(), Blocks.AIR, registerBlocks.MACHINE_FRAME.get()}, {registerBlocks.MACHINE_FRAME.get(),Blocks.AIR,registerBlocks.MACHINE_FRAME.get()}  }	//x = 2
                    }
            };

    /**CHECK MULTI BLOCK FORM
     * called when RIGHT CLICK heavy grudge block
     * (heavy grudge block is always at TOP-MIDDLE, so check X+-1 Y-2 Z+-1)
     */

    public static int checkMultiBlockForm(World world, BlockPos pos){
        return checkMultiBlockForm(world, pos.getX(), pos.getY(), pos.getZ());
    }

    public static int checkMultiBlockForm(World world, int xCoord, int yCoord, int zCoord)
    {
        BlockState state;
        BlockPos pos;
        Block blockType;
        /** bitwise pattern match
         *  ex: type = 3 (int) = 0011 (bit) = match pattern 0,1
         *      type = 2 (int) = 0010 (bit) = match pattern 1
         *      type = 13(int) = 1101 (bit) = match pattern 0,2,3
         */
        int patternTemp;
        int patternMatch = 1;  //init match pattern = 0001 (bit)

        if (yCoord < 4) return -1;

        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                for (int z = 0; z < 3; z++)
                {
                    pos = new BlockPos(xCoord - 1 + x, yCoord - 3 + y, zCoord - 1 + z);

                    //1. get block
                    state = world.getBlockState(pos);
                    blockType = state.getBlock();

                    Main.LOGGER.debug("DEBUG: multi block check: pos "+pos.getX()+" "+pos.getY()+" "+pos.getZ()+" "+blockType.getTranslatedName()+" "+blockType);

                    //2. match pattern
                    patternTemp = 0;
                    for (int t = 0; t < MULTIBLOCKPATTERN.length; t++)
                    {
                        if (blockType == MULTIBLOCKPATTERN[t][x][y][z])
                        {
                            patternTemp += Math.pow(2, t);		//match pattern t
                        }
                    }
                    patternMatch = (patternMatch & patternTemp);

                    Main.LOGGER.debug("DEBUG: check structure: type "+patternMatch+" "+patternTemp);
                    if (patternMatch == 0) return -1;


                    if (blockType != Blocks.AIR)
                    {
                        TileEntity t = world.getTileEntity(pos);
                        if (t instanceof MultiblockBaseTE && ((MultiblockBaseTE) t).hasMaster())
                        {
                            return -1;
                        }
                    }

                }//end z for
            }//end y for
        }//end x for

        Main.LOGGER.debug("DEBUG: check structure: type "+patternMatch);
        return patternMatch;
    }

    /** setup multi block struct
     *
     *  input: world, masterX, masterY, masterZ, structure type
     *
     *  type: 0:no MBS, 1:large shipyard, 2:-
     */
    public static void setupStructure(World world, BlockPos pos, int type) {
        setupStructure(world, pos.getX(), pos.getY(), pos.getZ(), type);
    }

    public static void setupStructure(World world, int xCoord, int yCoord, int zCoord, int type)
    {
        List<MultiblockBaseTE> tiles = new ArrayList<MultiblockBaseTE>();  //all tile in structure
        BlockPos pos = null;
        BlockPos masterPos = new BlockPos(xCoord, yCoord, zCoord);
        MultiblockBaseTE masterTile = null;  //master tile
        MultiblockBaseTE tile2 = null;
        TileEntity tile = null;
        Main.LOGGER.debug("DEBUG: setup structure type: "+type);

        //get all tile and master tile
        for (int x = xCoord - 1; x < xCoord + 2; x++)
        {
            for (int y = yCoord - 3; y < yCoord + 1; y++)
            {
                for (int z = zCoord - 1; z < zCoord + 2; z++)
                {
                    pos = new BlockPos(x, y, z);
                    tile = world.getTileEntity(pos);

                    // Check if block is master or servant
                    boolean mflag = (x == xCoord && y == yCoord && z == zCoord);

                    if (tile instanceof MultiblockBaseTE)
                    {
                        tile2 = (MultiblockBaseTE) tile;

                        tiles.add(tile2);
                        tile2.setIsMaster(mflag);
                        tile2.setHasMaster(true);
                        tile2.setStructType(type, world);
                        tile2.setMasterCoords(masterPos);

                        if (mflag)
                        {
                            masterTile = tile2;
                        }
                    }
                }//end z loop
            }//end y loop
        }//end x loop

        //set master value
        for (MultiblockBaseTE te : tiles)
        {
            te.setMaster(masterTile);
        }

    }

    //reset(remove) tile multi
    private static void resetTileMulti(MultiblockBaseTE parTile)
    {
        parTile.setMasterCoords(BlockPos.ZERO);
        parTile.setMaster(null);
        parTile.setHasMaster(false);
        parTile.setIsMaster(false);
        parTile.setStructType(0, parTile.getWorld());

    }

    //Reset tile multi, called from master block if struct broken
    public static void resetStructure(World world, int xCoord, int yCoord, int zCoord)
    {
        Main.LOGGER.debug("DEBUG: reset struct: client? "+world.isRemote+" "+xCoord+" "+yCoord+" "+zCoord);

        for (int x = xCoord - 1; x < xCoord + 2; x++)
        {
            for (int y = yCoord - 3; y < yCoord + 1; y++)
            {
                for (int z = zCoord - 1; z < zCoord + 2; z++)
                {
                    TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

                    if (tile instanceof MultiblockBaseTE) resetTileMulti((MultiblockBaseTE)tile);
                }
            }
        }

    }

}