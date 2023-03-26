package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.hatches;

import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.multiblocked.api.block.CustomProperties;
import com.lowdragmc.multiblocked.api.capability.MultiblockCapability;
import com.lowdragmc.multiblocked.api.definition.PartDefinition;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.lowdragmc.multiblocked.api.tile.part.PartTileEntity;
import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//We do little bit of multiblock fuckery
public class HatchTE extends PartTileEntity<PartDefinition> {

    public enum HatchType{
        FLUID,
        ENERGY,
        ITEM,
        ENTITY
    }

    public static final PartDefinition ItemHatchDefinition = new PartDefinition(new ResourceLocation(Constants.MODID, "item_hatch"), (definition)->new HatchTE(definition, HatchType.ITEM));
    public static final PartDefinition EnergyHatchDefinition = new PartDefinition(new ResourceLocation(Constants.MODID, "energy_hatch"),(definition)->new HatchTE(definition, HatchType.ENERGY));
    public static final PartDefinition FluidHatchDefinition = new PartDefinition(new ResourceLocation(Constants.MODID, "fluid_hatch"),(definition)->new HatchTE(definition, HatchType.FLUID));
    public static final PartDefinition EntityDefinition = new PartDefinition(new ResourceLocation(Constants.MODID, "entity_hatch"),(definition)->new HatchTE(definition, HatchType.ENTITY));

    private final HatchType type;




    public HatchTE(PartDefinition definition, HatchType type) {
        super(definition);
        this.type = type;
    }

    private final ItemStackHandler inventory = new ItemStackHandler(1);
    private final LazyOptional<ItemStackHandler> INVENTORY = LazyOptional.of(()->this.inventory);
    private final CustomEnergyStorage battery = new CustomEnergyStorage(10000);
    private final LazyOptional<EnergyStorage> BATTERY = LazyOptional.of(()->this.battery);
    private final FluidTank tank = new FluidTank(10000);
    private final LazyOptional<FluidTank> TANK = LazyOptional.of(()->this.tank);

    @Nonnull
    @Override
    public <K> LazyOptional<K> getCapability(@Nonnull Capability<K> capability, @Nullable Direction facing) {

        switch(this.type){
            case ITEM:
                if(capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
                    return INVENTORY.cast();
                }
                break;
            case ENERGY:
                if(capability == CapabilityEnergy.ENERGY){
                    return BATTERY.cast();
                }
                break;
            case FLUID:
                if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
                    return TANK.cast();
                }
                break;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public ModularUI createUI(Player PlayerEntity) {
        TabContainer tabContainer = new TabContainer(0, 0, 256, 166);
        this.initTraitUI(tabContainer, PlayerEntity);
        return (new ModularUI(196, 166, this, PlayerEntity)).widget(tabContainer);
    }

    protected void initTraitUI(TabContainer tabContainer, Player PlayerEntity) {
        WidgetGroup group = new WidgetGroup(20, 0, 176, 256);

        String textureloc;

        switch (this.type){
            case ITEM:
                textureloc = Constants.MODID+":textures/gui/item_hatch.png";
                break;
            default:
                textureloc = Constants.MODID+":textures/gui/hatch_other.png";
                break;
            case FLUID:
                textureloc = Constants.MODID+":textures/gui/hatch_fluid.png";
                break;
        }
        tabContainer.addTab((new TabButton(0, tabContainer.containerGroup.widgets.size() * 20, 20, 20)).setTexture((new ResourceTexture("multiblocked:textures/gui/custom_gui_tab_button.png")).getSubTexture(0.0, 0.0, 1.0, 0.5), (new ResourceTexture("multiblocked:textures/gui/custom_gui_tab_button.png")).getSubTexture(0.0, 0.5, 1.0, 0.5)), group);
        group.addWidget(new ImageWidget(0, 0, 256, 256, new ResourceTexture(textureloc)));

        switch (this.type){
            case ITEM:
                group.addWidget(new SlotWidget(this.inventory, 0, 79, 35, true, true));
                break;
            case ENERGY:
                group.addWidget((new ProgressWidget(this::getProgress, 7, 37, 162, 14, new ResourceTexture(Constants.MODID+":textures/gui/hatch_energybar.png"))).setDynamicHoverTips(this::dynamicEnergyHoverTips));
                break;
            case FLUID:
                group.addWidget((new TankWidget(this.tank, 61, 16,54, 54, true, true)));
                break;
        }
        
        int slot;
        for(slot = 0; slot < 3; ++slot) {
            for(int col = 0; col < 9; ++col) {
                group.addWidget((new SlotWidget(PlayerEntity.inventory, col + (slot + 1) * 9, 7 + col * 18, 83 + slot * 18)).setLocationInfo(true, false));
            }
        }

        for(slot = 0; slot < 9; ++slot) {
            group.addWidget((new SlotWidget(PlayerEntity.inventory, slot, 7 + slot * 18, 141)).setLocationInfo(true, true));
        }

    }

    private double getProgress() {
        return ((float) this.battery.getEnergyStored() / (float)this.battery.getMaxEnergyStored());}

    protected String dynamicEnergyHoverTips(double progress) {
        return LocalizationUtils.format("multiblocked.gui.trait.fe.progress", (int)((double)this.battery.getMaxEnergyStored() * progress), this.battery.getMaxEnergyStored());
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compound) {
        compound = super.save(compound);
        compound.put("inventory", this.inventory.serializeNBT());
        compound.put("tank", this.tank.writeToNBT(new CompoundTag()));
        compound.put("battery", this.battery.serializeNBT());
        return compound;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundTag compound) {
        super.load(state, compound);
        this.inventory.deserializeNBT(compound.getCompound("inventory"));
        this.tank.readFromNBT(compound.getCompound("tank"));
        this.battery.deserializeNBT(compound.getCompound("battery"));
    }

    @Override
    public boolean hasTrait(MultiblockCapability<?> capability) {
        return super.hasTrait(capability);
    }

    public static void registerItemHatch() {
        ItemHatchDefinition.getBaseStatus().setRenderer(()-> ResourceUtils.getMBDBlockModel("item_hatch"));
        ItemHatchDefinition.properties.rotationState = CustomProperties.RotationState.NONE;
        ItemHatchDefinition.properties.isOpaque = true;
        ItemHatchDefinition.properties.tabGroup = "pa_machines";
        MbdComponents.registerComponent(ItemHatchDefinition);
        EnergyHatchDefinition.getBaseStatus().setRenderer(()-> ResourceUtils.getMBDBlockModel("energy_hatch"));
        EnergyHatchDefinition.properties.rotationState = CustomProperties.RotationState.NONE;
        EnergyHatchDefinition.properties.tabGroup = "pa_machines";
        EnergyHatchDefinition.properties.isOpaque = true;
        MbdComponents.registerComponent(EnergyHatchDefinition);
        FluidHatchDefinition.getBaseStatus().setRenderer(()-> ResourceUtils.getMBDBlockModel("fluid_hatch"));
        FluidHatchDefinition.properties.rotationState = CustomProperties.RotationState.NONE;
        FluidHatchDefinition.properties.tabGroup = "pa_machines";
        FluidHatchDefinition.properties.isOpaque = true;
        MbdComponents.registerComponent(FluidHatchDefinition);
        EntityDefinition.getBaseStatus().setRenderer(()-> ResourceUtils.getMBDBlockModel("entity_hatch"));
        EntityDefinition.properties.rotationState = CustomProperties.RotationState.Y_AXIS;
        EntityDefinition.properties.tabGroup = "pa_machines";
        EntityDefinition.properties.isOpaque = true;
        EntityDefinition.traits.add("companion", new JsonObject());
        EntityDefinition.traits.add("entity", new JsonObject());
        MbdComponents.registerComponent(EntityDefinition);
        /*

        FluidHatchDefinition.baseRenderer = ResourceUtils.getMBDBlockModel("fluid_hatch");
        FluidHatchDefinition.allowRotate = false;
        FluidHatchDefinition.properties.isOpaque = true;
        MbdComponents.registerComponent(FluidHatchDefinition);

         */

    }


}
