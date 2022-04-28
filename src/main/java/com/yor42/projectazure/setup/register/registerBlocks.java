package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.renderer.items.ItemRecruitBeaconRenderer;
import com.yor42.projectazure.gameobject.blocks.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockSteelFrame;
import com.yor42.projectazure.gameobject.items.AnimateableMachineBlockItems;
import com.yor42.projectazure.gameobject.items.PAOreBlockItem;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.logging.Level;

public class registerBlocks {

    public static final RegistryObject<Block> BAUXITE_ORE = registerMetalOre_Stone("aluminium");
    public static final RegistryObject<Block> COPPER_ORE = registerMetalOre_Stone("copper");
    public static final RegistryObject<Block> TIN_ORE = registerMetalOre_Stone("tin");
    public static final RegistryObject<Block> LEAD_ORE = registerMetalOre_Stone("lead");
    public static final RegistryObject<Block> ZINC_ORE = registerMetalOre_Stone("zinc");
    public static final RegistryObject<Block> ORIROCK = register("orirock",() -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 3F)), Main.PA_RESOURCES);

    public static final RegistryObject<Block> CRUDE_OIL = register_noItem("crude_oil", ()->new LiquidBlock(()->registerFluids.CRUDE_OIL_SOURCE, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> GASOLINE = register_noItem("gasoline", ()->new LiquidBlock(()->registerFluids.GASOLINE_SOURCE, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> DIESEL = register_noItem("diesel", ()->new LiquidBlock(()->registerFluids.DIESEL_SOURCE, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> FUEL_OIL = register_noItem("fuel_oil", ()->new LiquidBlock(()->registerFluids.FUEL_OIL_SOURCE, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> BOUNDING_BOX = register_noItem("boundingbox", ()->new BoundingBoxBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).sound(SoundType.METAL).noOcclusion()));


    public static final RegistryObject<Block> REENFORCED_PLANK = register("reenforced_plank",() -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(4F, 6F)), Main.PA_RESOURCES);


    public static final RegistryObject<Block> MACHINE_FRAME = register("machine_frame", () ->
            new MultiblockStructureBlocks((BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).sound(SoundType.METAL))){

                @Nullable
                @Override
                public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
                    return new MultiblockSteelFrame();
                }
            }, Main.PA_RESOURCES);


    public static final RegistryObject<Block> METAL_PRESS = register_blockWithToolTiponItem("metal_press", MetalPressBlock::new, Main.PA_MACHINES);
    public static final RegistryObject<Block> ALLOY_FURNACE = register_blockWithToolTiponItem("alloy_furnace", AlloyFurnaceBlock::new, Main.PA_MACHINES);
    public static final RegistryObject<Block> BASIC_REFINERY = register_blockWithToolTiponItem("basic_refinery", ()->new BasicRefineryBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).lightLevel(registerBlocks.getLightValueLit(8)).harvestLevel(2).sound(SoundType.STONE).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> CRYSTAL_GROWTH_CHAMBER = register_blockWithToolTiponItem("crystal_growth_chamber", ()->new CrystalGrowthChamberBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).lightLevel((block)->0).harvestLevel(2).sound(SoundType.STONE).noOcclusion()), Main.PA_MACHINES);

    public static final RegistryObject<Block> DRYDOCKCONTROLLER = register_blockWithToolTiponItem("drydock_controller",()-> new blockMultiblockDryDockController(BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> REENFORCEDCONCRETE = register_blockWithToolTiponItem("reenforced_concrete",()-> new MultiblockStructureBlocks(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).harvestLevel(2).sound(SoundType.STONE).noOcclusion()), Main.PA_MACHINES);

    public static final RegistryObject<Block> RECRUIT_BEACON = registerAnimatedMachines("recruit_beacon", RecruitBeaconBlock::new, Main.PA_MACHINES, new Item.Properties().setISTER(()-> ItemRecruitBeaconRenderer::new));

    private static <T extends Block> RegistryObject<T> register_noItem(String name, Supplier<T> block){
        return registerManager.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, CreativeModeTab group){
        return register(name, block, group, new Item.Properties().tab(group));
    }

    private static <T extends Block> RegistryObject<T> register_blockWithToolTiponItem(String name, Supplier<T> block, CreativeModeTab group){
        RegistryObject<T> ret = register_noItem(name, block);
        registerManager.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(group)){
            @Override
            public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<MutableComponent> tooltip, @Nonnull TooltipFlag flagIn) {
                super.appendHoverText(stack, worldIn, tooltip, flagIn);
                if (worldIn != null && worldIn.isClientSide) {
                    TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, tooltip));
                }
            }

            @OnlyIn(Dist.CLIENT)
            public void addInformationAfterShift(ItemStack stack, List<Component> tooltip){
                tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
            }

        });
        return ret;
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, CreativeModeTab group, Item.Properties properties){
        RegistryObject<T> ret = register_noItem(name, block);
        registerManager.ITEMS.register(name, () -> new BlockItem(ret.get(), properties));
        return ret;
    }


    private static <T extends Block> RegistryObject<T> registerAnimatedMachines(String name, Supplier<T> block, CreativeModeTab group, Item.Properties properties){
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
