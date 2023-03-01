package com.yor42.projectazure.client;

import com.yor42.projectazure.client.gui.container.*;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterContainer;
import com.yor42.projectazure.setup.register.RegisterItems;
import mekanism.additions.common.item.ItemBalloon;
import mekanism.additions.common.registries.AdditionsBlocks;
import mekanism.additions.common.registries.AdditionsItems;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegisterManager {

    public static void registerScreen(){
        ScreenManager.register(RegisterContainer.SHIP_CONTAINER.get(), GuiALInventory::new);
        ScreenManager.register(RegisterContainer.RIGGING_INVENTORY.get(), guiRiggingInventory::new);
        ScreenManager.register(RegisterContainer.BA_CONTAINER.get(), guiBAInventory::new);
        ScreenManager.register(RegisterContainer.GFL_CONTAINER.get(), GuiGFLInventory::new);
        ScreenManager.register(RegisterContainer.CLS_CONTAINER.get(), GuiCLSInventory::new);
        ScreenManager.register(RegisterContainer.AKN_CONTAINER.get(), GuiAKNInventory::new);
        ScreenManager.register(RegisterContainer.FGO_CONTAINER.get(), GuiFGOInventory::new);
        ScreenManager.register(RegisterContainer.PCR_CONTAINER.get(), GuiPCRInventory::new);
        ScreenManager.register(RegisterContainer.SR_CONTAINER.get(), GuiSRInventory::new);

        ScreenManager.register(RegisterContainer.METAL_PRESS_CONTAINER.get(), guiMetalPress::new);
        ScreenManager.register(RegisterContainer.ALLOY_FURNACE_CONTAINER.get(), guiAlloyFurnace::new);
        ScreenManager.register(RegisterContainer.RECRUIT_BEACON_CONTAINER.get(), guiRecruitBeacon::new);
        ScreenManager.register(RegisterContainer.BASIC_REFINERY_CONTAINER.get(), guiBasicRefinery::new);
        ScreenManager.register(RegisterContainer.GROWTH_CHAMBER_CONTAINER.get(), GuiCrystalGrowthChamber::new);
        ScreenManager.register(RegisterContainer.PANTRY_CONTAINER.get(), GuiPantryInventory::new);
        ScreenManager.register(RegisterContainer.BASICCCHEMICALREACTOR.get(), GuiBasicChemicalReactor::new);
    }

    @SubscribeEvent
    public static void registerItemColorHandlers(ColorHandlerEvent.Item event) {
        ItemColors color = event.getItemColors();
        color.register((stack, tintindex)-> tintindex <= 0 ? -1 : PotionUtils.getColor(stack), RegisterItems.SYRINGE.get());
    }

}
