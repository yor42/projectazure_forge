package com.yor42.projectazure.intermod.tconstruct;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.data.client.itemModelProvider;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import slimeknights.mantle.registration.ModelFluidAttributes;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

import java.util.function.Supplier;

import static com.yor42.projectazure.intermod.tconstruct.PATconFluidTag.FLUIDLIST;
import static com.yor42.projectazure.setup.register.RegisterBlocks.BLOCKS;
import static com.yor42.projectazure.setup.register.RegisterFluids.FLUIDS;
import static com.yor42.projectazure.setup.register.RegisterItems.ITEMS;

public class TinkersRegistry {
    public static final TinkersFluids MoltenD32 = new TinkersFluids("molten_d32", "Molten D32", 900,15, 3000,6000);

    private static FluidAttributes.Builder hotBuilder() {
        return ModelFluidAttributes.builder().density(2000).viscosity(10000).temperature(1000).sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA);
    }

    public static class TinkersFluids {

        public final ForgeFlowingFluid.Properties PROPERTIES;

        public final RegistryObject<ForgeFlowingFluid.Source> FLUID;
        public final RegistryObject<ForgeFlowingFluid.Flowing> FLUID_FLOW;
        public final FluidObject<ForgeFlowingFluid> OBJECT;

        public final ResourceLocation TEXTURE_STILL;
        public final ResourceLocation TEXTURE_FLOW;

        public final ITag.INamedTag<Fluid> FluidTag;

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
            OBJECT = new FluidObject<>(new ResourceLocation(Constants.MODID, name), name, FLUID, FLUID_FLOW, FLUID_BLOCK);
            FLUID_BUCKET = ITEMS.register(name + "_bucket", () -> new BucketItem(FLUID, new BucketItem.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));
            itemModelProvider.ITEMENTRY.add(new Pair<>(name + "_bucket", name + "_bucket"));
            PROPERTIES.bucket(FLUID_BUCKET).block(FLUID_BLOCK).explosionResistance(1000F).tickRate(9);
            this.FluidTag = forge(name);

            FLUIDLIST.add(this);
        }

        public ForgeFlowingFluid.Properties getFluidProperties() {
            return PROPERTIES;
        }

        private static ITag.INamedTag<Fluid> forge(String path) {
            return FluidTags.bind(new ResourceLocation("forge", path).toString());
        }
    }

    public static void registerTinkers(IEventBus bus){
        //FLUIDS.register(bus);
    }

    public static void register(){}

}
