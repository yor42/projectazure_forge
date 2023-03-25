package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.renderer.items.ItemRecruitBeaconRenderer;
import com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock;
import com.yor42.projectazure.gameobject.blocks.PAOreBlock;
import com.yor42.projectazure.gameobject.blocks.PantryBlock;
import com.yor42.projectazure.gameobject.blocks.machines.*;
import com.yor42.projectazure.gameobject.items.AnimateableMachineBlockItems;
import com.yor42.projectazure.gameobject.items.PAOreBlockItem;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.yor42.projectazure.data.client.BlockModelProvider.SIMPLEBLOCKLIST;
import static com.yor42.projectazure.data.client.itemModelProvider.SIMPLETEXTUREBBLOCKLIST;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class RegisterBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);

    public static final RegistryObject<Block> BAUXITE_ORE = registerMetalOre_Stone("aluminium");
    public static final RegistryObject<Block> COPPER_ORE = registerMetalOre_Stone("copper");
    public static final RegistryObject<Block> PYROXENE_ORE = registerMetalOre_Stone("pyroxene");
    public static final RegistryObject<Block> TIN_ORE = registerMetalOre_Stone("tin");
    public static final RegistryObject<Block> LEAD_ORE = registerMetalOre_Stone("lead");
    public static final RegistryObject<Block> ZINC_ORE = registerMetalOre_Stone("zinc");
    public static final RegistryObject<Block> RMA_7012_ORE = registerMetalOre_Stone("rma7012");
    public static final RegistryObject<Block> MANGANESE_ORE = registerMetalOre_Stone("manganese");

    public static final RegistryObject<Block> ALUMINIUM_BLOCK = registerMaterialBlock("aluminium");
    public static final RegistryObject<Block> COPPER_BLOCK = registerMaterialBlock("copper");
    public static final RegistryObject<Block> TIN_BLOCK = registerMaterialBlock("tin");
    public static final RegistryObject<Block> LEAD_BLOCK = registerMaterialBlock("lead");
    public static final RegistryObject<Block> ZINC_BLOCK = registerMaterialBlock("zinc");
    public static final RegistryObject<Block> BRASS_BLOCK = registerMaterialBlock("brass");
    public static final RegistryObject<Block> BRONZE_BLOCK = registerMaterialBlock("bronze");
    public static final RegistryObject<Block> RMA_7012_BLOCK = registerMaterialBlock("rma7012");
    public static final RegistryObject<Block> RMA_7024_BLOCK = registerMaterialBlock("rma7024");
    public static final RegistryObject<Block> D32_BLOCK = registerMaterialBlock("d32steel");
    public static final RegistryObject<Block> STEEL_BLOCK = registerMaterialBlock("steel");
    public static final RegistryObject<Block> MANGANESE_BLOCK = registerMaterialBlock("manganese");
    public static final RegistryObject<Block> INCANDESCENT_ALLOY_BLOCK = registerMaterialBlock("incandescent_alloy");

    public static final RegistryObject<Block> ORIROCK = register_simplemodel("orirock",() -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).harvestLevel(0).strength(1.5F, 3F)), Main.PA_RESOURCES);
    public static final RegistryObject<Block> COBBLED_ORIROCK = register_simplemodel("cobbled_orirock",() -> new Block(BlockBehaviour.Properties.of(Material.STONE).harvestLevel(0).strength(1.5F, 3F)), Main.PA_RESOURCES);
    public static final RegistryObject<Block> CRUDE_OIL = register_noItem("crude_oil", ()->new LiquidBlock(()-> (FlowingFluid) RegisterFluids.CRUDE_OIL_FLOWING_REGISTRY.get(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> GASOLINE = register_noItem("gasoline", ()->new LiquidBlock(()-> (FlowingFluid) RegisterFluids.GASOLINE_SOURCE_REGISTRY.get(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> DIESEL = register_noItem("diesel", ()->new LiquidBlock(()-> (FlowingFluid) RegisterFluids.DIESEL_SOURCE_REGISTRY.get(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> KETON = register_noItem("keton", ()->new LiquidBlock(()-> (FlowingFluid) RegisterFluids.KETON_SOURCE_REGISTRY.get(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> FUEL_OIL = register_noItem("fuel_oil", ()->new LiquidBlock(()-> (FlowingFluid) RegisterFluids.FUEL_OIL_SOURCE_REGISTRY.get(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));

    public static final RegistryObject<Block> REENFORCED_PLANK = register_simplemodel("reenforced_plank",() -> new Block(BlockBehaviour.Properties.of(Material.WOOD).harvestLevel(2).strength(4F, 6F)), Main.PA_RESOURCES);


    public static final RegistryObject<Block> MACHINE_FRAME = register_simplemodel("machine_frame", () ->
            new Block((BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL))), Main.PA_MACHINES, REGISTERMODEL.BOTH);
    public static final RegistryObject<Block> MACHINE_FRAME_SLAB = register_simplemodel("machine_frame_slab", () ->
            new SlabBlock((BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL))), Main.PA_MACHINES, REGISTERMODEL.ITEM);

    public static final RegistryObject<Block> MACHINE_COMPONENTBLOCK = register_simplemodel("machine_component", ()->new Block((BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL))), Main.PA_MACHINES, REGISTERMODEL.BOTH);
    public static final RegistryObject<Block> MACHINE_DYNAMO = register_simplemodel("machine_dynamo", ()->new RotatedPillarBlock((BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL))), Main.PA_MACHINES, REGISTERMODEL.ITEM);



    public static final RegistryObject<Block> METAL_PRESS = register_blockWithToolTiponItem("metal_press", MetalPressBlock::new, Main.PA_MACHINES);
    public static final RegistryObject<Block> ALLOY_FURNACE = register_blockWithToolTiponItem("alloy_furnace", AlloyFurnaceBlock::new, Main.PA_MACHINES);
    public static final RegistryObject<Block> BASIC_CHEMICAL_REACTOR = register_blockWithToolTiponItem("basic_chemicalreactor", BasicChemicalReactorBlock::new, Main.PA_MACHINES);
    public static final RegistryObject<Block> BASIC_REFINERY = register_blockWithToolTiponItem("basic_refinery", ()->new BasicRefineryBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).lightLevel(RegisterBlocks.getLightValueLit(8)).harvestLevel(2).sound(SoundType.STONE).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> CRYSTAL_GROWTH_CHAMBER = register_blockWithToolTiponItem("crystal_growth_chamber", ()->new CrystalGrowthChamberBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).lightLevel((block)->0).harvestLevel(2).sound(SoundType.STONE).noOcclusion()), Main.PA_MACHINES);

    public static final RegistryObject<Block> OAK_PANTRY = register_blockWithToolTiponItem("oak_pantry", ()->new PantryBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2, 3).sound(SoundType.WOOD).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> SPRUCE_PANTRY = register_blockWithToolTiponItem("spruce_pantry", ()->new PantryBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2, 3).sound(SoundType.WOOD).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> ACACIA_PANTRY = register_blockWithToolTiponItem("acacia_pantry", ()->new PantryBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2, 3).sound(SoundType.WOOD).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> BIRCH_PANTRY = register_blockWithToolTiponItem("birch_pantry", ()->new PantryBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2, 3).sound(SoundType.WOOD).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> DARK_OAK_PANTRY = register_blockWithToolTiponItem("dark_oak_pantry", ()->new PantryBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2, 3).sound(SoundType.WOOD).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> JUNGLE_PANTRY = register_blockWithToolTiponItem("jungle_pantry", ()->new PantryBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2, 3).sound(SoundType.WOOD).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> WARPED_PANTRY = register_blockWithToolTiponItem("warped_pantry", ()->new PantryBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2, 3).sound(SoundType.WOOD).noOcclusion()), Main.PA_MACHINES);
    public static final RegistryObject<Block> CRIMSON_PANTRY = register_blockWithToolTiponItem("crimson_pantry", ()->new PantryBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2, 3).sound(SoundType.WOOD).noOcclusion()), Main.PA_MACHINES);

    public static final RegistryObject<Block> STEEL_FOUNDATION = register_simplemodel("steel_foundation",()-> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL)), Main.PA_RESOURCES);
    public static final RegistryObject<Block> REENFORCEDCONCRETE = register_simplemodel("reenforced_concrete",()-> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).harvestLevel(2).sound(SoundType.STONE)), Main.PA_MACHINES);
    public static final RegistryObject<Block> ASPHALTCONCRETE = register_simplemodel("asphalt_concrete",()-> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2, 8).harvestLevel(2).sound(SoundType.STONE)){

        @Override
        public void stepOn(Level p_152431_, BlockPos p_152432_, BlockState p_152433_, Entity entity) {
            if(entity instanceof LivingEntity) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2, 0, false, false));
            }
        }
    }, Main.PA_RESOURCES);

    public static final RegistryObject<Block> RECRUIT_BEACON = registerAnimatedMachines("recruit_beacon", RecruitBeaconBlock::new, Main.PA_MACHINES, new Item.Properties().setISTER(()-> ItemRecruitBeaconRenderer::new));

    private static <T extends Block> RegistryObject<T> register_noItem(String name, Supplier<T> block){
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register_blockWithToolTiponItem(String name, Supplier<T> block, CreativeModeTab group){
        RegistryObject<T> ret = register_noItem(name, block);
        RegisterItems.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(group)){
            @Override
            public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
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

    private static RegistryObject<Block> register_simplemodel(String name, Supplier<Block> block, CreativeModeTab itemGroup){
        return register_simplemodel(name, block, new Item.Properties().tab(itemGroup), REGISTERMODEL.BOTH);
    }
    private static RegistryObject<Block> register_simplemodel(String name, Supplier<Block> block, Item.Properties properties){
        return register_simplemodel(name, block, properties, REGISTERMODEL.BOTH);
    }

    private static RegistryObject<Block> register_simplemodel(String name, Supplier<Block> block, CreativeModeTab itemGroup, REGISTERMODEL model){
        return register_simplemodel(name, block, new Item.Properties().tab(itemGroup), model);
    }
    private static RegistryObject<Block> register_simplemodel(String name, Supplier<Block> block, Item.Properties properties, REGISTERMODEL modelregistry){
        RegistryObject<Block> ret = register_noItem(name, block);
        RegisterItems.ITEMS.register(name, () -> new BlockItem(ret.get(), properties));
        if(modelregistry == REGISTERMODEL.BOTH || modelregistry == REGISTERMODEL.BLOCK) {
            SIMPLEBLOCKLIST.add(ret);
        }
        if(modelregistry == REGISTERMODEL.BOTH || modelregistry == REGISTERMODEL.ITEM) {
            SIMPLETEXTUREBBLOCKLIST.add(name);
        }
        return ret;
    }


    private static <T extends Block> RegistryObject<T> registerAnimatedMachines(String name, Supplier<T> block, CreativeModeTab group, Item.Properties properties){
        RegistryObject<T> ret = register_noItem(name, block);
        RegisterItems.ITEMS.register(name, () -> new AnimateableMachineBlockItems(ret.get(), properties.tab(group), true));
        return ret;
    }

    private static RegistryObject<Block> registerMetalOre_Stone(String materialName){
        return registerMetalOre("ore_stone_"+materialName, materialName);
    }

    private static RegistryObject<Block> registerMetalOre(String materialName){
        return registerMetalOre("ore_"+materialName, materialName);
    }

    private static RegistryObject<Block> registerMaterialBlock(String materialName){
        return registerMaterialBlock(materialName+"_block", materialName, enums.ResourceType.BLOCK);
    }

    private static RegistryObject<Block> registerMetalOre(String registryName, String materialName){
        return registerMaterialBlock(registryName, materialName, enums.ResourceType.ORE);
    }

    private static RegistryObject<Block> registerMaterialBlock(String registryName, String materialName, enums.ResourceType type){
        RegistryObject<Block> ret = register_noItem(registryName, () -> new PAOreBlock(materialName, type));
        SIMPLEBLOCKLIST.add(ret);
        RegisterItems.ITEMS.register(registryName, () -> new PAOreBlockItem(ret.get(), materialName, type));
        SIMPLETEXTUREBBLOCKLIST.add(registryName);
        return ret;
    }

    private static RegistryObject<Block> registerMaterialOre(String registryName, String materialName, enums.ResourceType type){
        RegistryObject<Block> ret = register_noItem(registryName, () -> new PAOreBlock(materialName, type));
        SIMPLEBLOCKLIST.add(ret);
        RegisterItems.ITEMS.register(registryName, () -> new PAOreBlockItem(ret.get(), materialName, type));
        SIMPLETEXTUREBBLOCKLIST.add(registryName);
        return ret;
    }

    public static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> state.getValue(AbstractMachineBlock.ACTIVE) ? lightValue : 0;
    }

    public static void register(){}

    public enum REGISTERMODEL{
        NONE,
        ITEM,
        BLOCK,
        BOTH
    }

}
