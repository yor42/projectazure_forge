package com.yor42.projectazure.setup.register;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.data.client.itemModelProvider;
import com.yor42.projectazure.gameobject.blocks.fluid.*;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.yor42.projectazure.setup.register.RegisterBlocks.BLOCKS;
import static com.yor42.projectazure.setup.register.RegisterItems.ITEMS;

public class RegisterFluids {

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

    public static final TinkersFluids MOLTEN_D32 = new TinkersFluids("molten_d32", "Molten D32 Steel", 2500, 15, 3000, 6000);

    public static void register() {
    }

    /*
    Copyright (c) 2021 RCXcrafter
    Code from Materialis mod. licensed under MIT License.
    https://github.com/RCXcrafter/Materialis/blob/1.16/LICENSE.md
     */

    public static class TinkersFluids {

        public final ForgeFlowingFluid.Properties PROPERTIES;

        public final RegistryObject<ForgeFlowingFluid.Source> FLUID;
        public final RegistryObject<ForgeFlowingFluid.Flowing> FLUID_FLOW;

        public final ResourceLocation TEXTURE_STILL;
        public final ResourceLocation TEXTURE_FLOW;

        public final RegistryObject<FlowingFluidBlock> FLUID_BLOCK;

        public final RegistryObject<BucketItem> FLUID_BUCKET;

        public final String name;
        public final String localizedName;
        public final int temperature;
        public final int light;
        public final int density;
        public final int viscosity;

        public TinkersFluids(String name, String localizedName, int temperature, int light, int density, int viscosity) {
            this.name = name;
            this.localizedName = localizedName;
            this.temperature = temperature;
            this.light = light;
            this.density = density;
            this.viscosity = viscosity;

            FLUID = FLUIDS.register(name, () -> new ForgeFlowingFluid.Source(getFluidProperties()));
            FLUID_FLOW = FLUIDS.register("flowing_" + name, () -> new ForgeFlowingFluid.Flowing(getFluidProperties()));

            TEXTURE_STILL = new ResourceLocation(Constants.MODID, "block/" + name + "_still");
            TEXTURE_FLOW = new ResourceLocation(Constants.MODID, "block/" + name + "_flow");

            PROPERTIES = new ForgeFlowingFluid.Properties(FLUID, FLUID_FLOW, FluidAttributes.builder(TEXTURE_STILL, TEXTURE_FLOW).overlay(TEXTURE_STILL).luminosity(light).density(density).viscosity(6000).temperature(temperature).sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA));

            FLUID_BLOCK = BLOCKS.register(name + "_block", () -> new FlowingFluidBlock(FLUID, Block.Properties.of(Material.LAVA).lightLevel((state) -> { return light; }).randomTicks().strength(100.0F).noDrops()));
            FLUID_BUCKET = ITEMS.register(name + "_bucket", () -> new BucketItem(FLUID, new BucketItem.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));
            itemModelProvider.ITEMENTRY.add(new Pair<>(name + "_bucket", name + "_bucket"));
            PROPERTIES.bucket(FLUID_BUCKET).block(FLUID_BLOCK).explosionResistance(1000F).tickRate(9);
        }

        public ForgeFlowingFluid.Properties getFluidProperties() {
            return PROPERTIES;
        }
    }
}
