package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.blocks.fluid.CrudeOilFluid;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.RegistryObject;

public class registerFluids {

    public static final FlowingFluid CRUDE_OIL_SOURCE = new CrudeOilFluid.Source();
    public static final RegistryObject<Fluid> CRUDE_OIL_SOURCE_REGISTRY = registerManager.FLUIDS.register("crude_oil_source", ()->CRUDE_OIL_SOURCE);
    public static final FlowingFluid CRUDE_OIL_FLOWING = new CrudeOilFluid.Flowing();
    public static final RegistryObject<Fluid> CRUDE_OIL_FLOWING_REGISTRY = registerManager.FLUIDS.register("crude_oil_flowing", ()->CRUDE_OIL_FLOWING);

    public static void register(){
    }
}
