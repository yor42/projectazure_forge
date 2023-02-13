package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.blocks.fluid.*;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class registerFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Constants.MODID);

    public static final RegistryObject<Fluid> CRUDE_OIL_SOURCE_REGISTRY = FLUIDS.register("crude_oil_source", CrudeOilFluid.Source::new);
    public static final RegistryObject<Fluid> CRUDE_OIL_FLOWING_REGISTRY = FLUIDS.register("crude_oil_flowing", CrudeOilFluid.Flowing::new);

    public static final RegistryObject<Fluid> GASOLINE_SOURCE_REGISTRY = FLUIDS.register("gasoline_source", GasolineFluid.Source::new);
    public static final RegistryObject<Fluid> GASOLINE_FLOWING_REGISTRY = FLUIDS.register("gasoline_flowing", GasolineFluid.Flowing::new);

    public static final RegistryObject<Fluid> DIESEL_SOURCE_REGISTRY = FLUIDS.register("diesel_source", DieselFluid.Source::new);
    public static final RegistryObject<Fluid> DIESEL_FLOWING_REGISTRY = FLUIDS.register("diesel_flowing", DieselFluid.Flowing::new);

    public static final RegistryObject<Fluid> KETON_SOURCE_REGISTRY = FLUIDS.register("keton_source", KetonFluid.Source::new);
    public static final RegistryObject<Fluid> KETON_FLOWING_REGISTRY = FLUIDS.register("keton_flowing", KetonFluid.Flowing::new);

    public static final RegistryObject<Fluid> FUEL_OIL_SOURCE_REGISTRY = FLUIDS.register("fuel_oil_source", FuelOilFluid.Source::new);
    public static final RegistryObject<Fluid> FUEL_OIL_FLOWING_REGISTRY = FLUIDS.register("fuel_oil_flowing", FuelOilFluid.Flowing::new);

    public static final RegistryObject<Fluid> ORIGINIUM_SOLUTION_SOURCE_REGISTRY = FLUIDS.register("originium_solution_source", OriginiumSolutionFluid.Source::new);
    public static final RegistryObject<Fluid> ORIGINIUM_SOLUTION_FLOWING_REGISTRY = FLUIDS.register("originium_solution_flowing", OriginiumSolutionFluid.Flowing::new);

    public static final RegistryObject<Fluid> NETHER_QUARTZ_SOLUTION_SOURCE_REGISTRY = FLUIDS.register("nether_quartz_solution_source", NetherQuartzSolutionFluid.Source::new);
    public static final RegistryObject<Fluid> NETHER_QUARTZ_SOLUTION_FLOWING_REGISTRY = FLUIDS.register("nether_quartz_solution_flowing", NetherQuartzSolutionFluid.Flowing::new);
    public static void register() {
    }
}
