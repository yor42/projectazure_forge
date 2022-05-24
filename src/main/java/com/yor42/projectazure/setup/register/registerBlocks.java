package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.renderer.items.ItemRecruitBeaconRenderer;
import com.yor42.projectazure.gameobject.blocks.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityEnergyHatch;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityFluidHatch;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityItemHatch;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockSteelFrame;
import com.yor42.projectazure.gameobject.items.AnimateableMachineBlockItems;
import com.yor42.projectazure.gameobject.items.PAOreBlockItem;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class registerBlocks {

    public static final RegistryObject<Block> BAUXITE_ORE = registerMetalOre_Stone("aluminium");
    public static final RegistryObject<Block> COPPER_ORE = registerMetalOre_Stone("copper");
    public static final RegistryObject<Block> TIN_ORE = registerMetalOre_Stone("tin");
    public static final RegistryObject<Block> LEAD_ORE = registerMetalOre_Stone("lead");
    public static final RegistryObject<Block> ZINC_ORE = registerMetalOre_Stone("zinc");
    public static final RegistryObject<Block> ORIROCK = register("orirock",() -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).harvestLevel(0).strength(1.5F, 3F)), Main.PA_RESOURCES);

    public static final RegistryObject<Block> CRUDE_OIL = register_noItem("crude_oil", ()->new FlowingFluidBlock(()->registerFluids.CRUDE_OIL_SOURCE, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> GASOLINE = register_noItem("gasoline", ()->new FlowingFluidBlock(()->registerFluids.GASOLINE_SOURCE, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> DIESEL = register_noItem("diesel", ()->new FlowingFluidBlock(()->registerFluids.DIESEL_SOURCE, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> FUEL_OIL = register_noItem("fuel_oil", ()->new FlowingFluidBlock(()->registerFluids.FUEL_OIL_SOURCE, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> BOUNDING_BOX = register_noItem("boundingbox", ()->new BoundingBoxBlock(AbstractBlock.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL).noOcclusion()));


    public static final RegistryObject<Block> REENFORCED_PLANK = register("reenforced_plank",() -> new Block(AbstractBlock.Properties.of(Material.WOOD).harvestLevel(2).strength(4F, 6F)), Main.PA_RESOURCES);


    public static final RegistryObject<Block> MACHINE_FRAME = register("machine_frame", () ->
            new MultiblockStructureBlocks((AbstractBlock.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL))){
                @Override
                public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
                    return new MultiblockSteelFrame();
                }
            }, Main.PA_RESOURCES);


    public static final RegistryObject<Block> METAL_PRESS = register_blockWithToolTiponItem("metal_press", MetalPressBlock::new, Main.PA_MACHINES);
    public static final RegistryObject<Block> ALLOY_FURNACE = register_blockWithToolTiponItem("alloy_furnace", AlloyFurnaceBlock::new, Main.PA_MACHINES);
    public static final RegistryObject<Block> BASIC_REFINERY = register_blockWithToolTiponItem("basic_refinery", ()->new BasicRefineryBlock(AbstractBlock.Properties.of(Material.STONE).strength(3, 10).lightLevel(registerBlocks.getLightValueLit(8)).harvestLevel(2).sound(SoundType.STONE).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> CRYSTAL_GROWTH_CHAMBER = register_blockWithToolTiponItem("crystal_growth_chamber", ()->new CrystalGrowthChamberBlock(AbstractBlock.Properties.of(Material.STONE).strength(3, 10).lightLevel((block)->0).harvestLevel(2).sound(SoundType.STONE).noOcclusion()), Main.PA_MACHINES);

    public static final RegistryObject<Block> DRYDOCKCONTROLLER = register_blockWithToolTiponItem("drydock_controller",()-> new blockMultiblockDryDockController(AbstractBlock.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> REENFORCEDCONCRETE = register_blockWithToolTiponItem("reenforced_concrete",()-> new MultiblockStructureBlocks(AbstractBlock.Properties.of(Material.STONE).strength(3, 10).harvestLevel(2).sound(SoundType.STONE).noOcclusion()), Main.PA_MACHINES);

    public static final RegistryObject<Block> RECRUIT_BEACON = registerAnimatedMachines("recruit_beacon", RecruitBeaconBlock::new, Main.PA_MACHINES, new Item.Properties().setISTER(()-> ItemRecruitBeaconRenderer::new));


    public static final RegistryObject<Block> ITEM_HATCH = register("item_hatch", ()->new HatchBlock((AbstractBlock.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL))){
        @Nonnull
        @Override
        public TileEntity createTileEntity(BlockState state, IBlockReader world) {
            return new TileEntityItemHatch();
        }
    }, Main.PA_MACHINES);

    public static final RegistryObject<Block> FLUID_HATCH = register("fluid_hatch", ()->new HatchBlock((AbstractBlock.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL))){
        @Nonnull
        @Override
        public TileEntity createTileEntity(BlockState state, IBlockReader world) {
            return new TileEntityFluidHatch();
        }
    }, Main.PA_MACHINES);

    public static final RegistryObject<Block> ENERGY_HATCH = register("energy_hatch", ()->new HatchBlock((AbstractBlock.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL))){
        @Nonnull
        @Override
        public TileEntity createTileEntity(BlockState state, IBlockReader world) {
            return new TileEntityEnergyHatch();
        }
    }, Main.PA_MACHINES);

    public static final RegistryObject<Block> ORIGINIUM_GENERATOR_CONTROLLER = register("originium_generator_controller", ()->new MultiblockControllerBlock((AbstractBlock.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL))), Main.PA_MACHINES);

    private static <T extends Block> RegistryObject<T> register_noItem(String name, Supplier<T> block){
        return registerManager.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, ItemGroup group){
        return register(name, block, group, new Item.Properties().tab(group));
    }

    private static <T extends Block> RegistryObject<T> register_blockWithToolTiponItem(String name, Supplier<T> block, ItemGroup group){
        RegistryObject<T> ret = register_noItem(name, block);
        registerManager.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(group)){
            @Override
            public void appendHoverText(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
                super.appendHoverText(stack, worldIn, tooltip, flagIn);
                if (worldIn != null && worldIn.isClientSide) {
                    TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, tooltip));
                }
            }

            @OnlyIn(Dist.CLIENT)
            public void addInformationAfterShift(ItemStack stack, List<ITextComponent> tooltip){
                tooltip.add(new TranslationTextComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(TextFormatting.GRAY));
            }

        });
        return ret;
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, ItemGroup group, Item.Properties properties){
        RegistryObject<T> ret = register_noItem(name, block);
        registerManager.ITEMS.register(name, () -> new BlockItem(ret.get(), properties));
        return ret;
    }


    private static <T extends Block> RegistryObject<T> registerAnimatedMachines(String name, Supplier<T> block, ItemGroup group, Item.Properties properties){
        RegistryObject<T> ret = register_noItem(name, block);
        registerManager.ITEMS.register(name, () -> new AnimateableMachineBlockItems(ret.get(), properties.tab(group), true));
        return ret;
    }

    private static RegistryObject<Block> registerMetalOre_Stone(String materialName){
        return registerMetalOre("ore_stone_"+materialName, materialName);
    }

    private static RegistryObject<Block> registerMetalOre(String registryName, String materialName){
        RegistryObject<Block> ret = register_noItem(registryName, () -> new PAOreBlock(materialName));
        registerManager.ITEMS.register(registryName, () -> new PAOreBlockItem(ret.get(), materialName));
        return ret;
    }

    public static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> {
            return state.getValue(AbstractMachineBlock.ACTIVE) ? lightValue : 0;
        };
    }

    public static void register(){}

}
