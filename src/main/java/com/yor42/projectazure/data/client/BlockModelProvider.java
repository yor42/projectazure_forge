package com.yor42.projectazure.data.client;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Constants.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Main.BAUXITE_ORE.get());
        simpleBlock(Main.COPPER_ORE.get());
        simpleBlock(Main.TIN_ORE.get());
        simpleBlock(Main.LEAD_ORE.get());
        simpleBlock(Main.ZINC_ORE.get());
        simpleBlock(Main.MACHINE_FRAME.get());
        simpleBlock(Main.ORIROCK.get());
        simpleBlock(Main.REENFORCEDCONCRETE.get());
        simpleBlock(Main.REENFORCED_PLANK.get());
    }
}
