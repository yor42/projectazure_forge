package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.blocks.fluid.CrudeOilFluid;
import com.yor42.projectazure.gameobject.blocks.fluid.DieselFluid;
import com.yor42.projectazure.gameobject.blocks.fluid.GasolineFluid;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.RegistryObject;

public class registerFluids {

    public static final FlowingFluid CRUDE_OIL_SOURCE = new CrudeOilFluid.Source();
    public static final RegistryObject<Fluid> CRUDE_OIL_SOURCE_REGISTRY = registerManager.FLUIDS.register("crude_oil_source", ()->CRUDE_OIL_SOURCE);
    public static final FlowingFluid CRUDE_OIL_FLOWING = new CrudeOilFluid.Flowing();
    public static final RegistryObject<Fluid> CRUDE_OIL_FLOWING_REGISTRY = registerManager.FLUIDS.register("crude_oil_flowing", ()->CRUDE_OIL_FLOWING);

    public static final FlowingFluid GASOLINE_SOURCE = new GasolineFluid.Source();
    public static final RegistryObject<Fluid> GASOLINE_SOURCE_REGISTRY = registerManager.FLUIDS.register("gasoline_source", ()->GASOLINE_SOURCE);
    public static final FlowingFluid GASOLINE_FLOWING = new GasolineFluid.Flowing();
    public static final RegistryObject<Fluid> GASOLINE_FLOWING_REGISTRY = registerManager.FLUIDS.register("gasoline_flowing", ()->GASOLINE_FLOWING);

    public static final FlowingFluid DIESEL_SOURCE = new DieselFluid.Source();
    public static final RegistryObject<Fluid> DIESEL_SOURCE_REGISTRY = registerManager.FLUIDS.register("diesel_source", ()->DIESEL_SOURCE);
    public static final FlowingFluid DIESEL_FLOWING = new DieselFluid.Flowing();
    public static final RegistryObject<Fluid> DIESEL_FLOWING_REGISTRY = registerManager.FLUIDS.register("diesel_flowing", ()->DIESEL_FLOWING);


    public static void register(){
    }
}
