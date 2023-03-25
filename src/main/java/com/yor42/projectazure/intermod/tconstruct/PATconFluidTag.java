package com.yor42.projectazure.intermod.tconstruct;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class PATconFluidTag extends FluidTagsProvider {

    public static final ArrayList<TinkersRegistry.TinkersFluids> FLUIDLIST = new ArrayList<>();

    public PATconFluidTag(DataGenerator p_i49156_1_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_i49156_1_, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for(TinkersRegistry.TinkersFluids fluid: FLUIDLIST){
            this.tag(fluid.FluidTag).add(fluid.FLUID.get(), fluid.FLUID_FLOW.get());
        }
    }
}
