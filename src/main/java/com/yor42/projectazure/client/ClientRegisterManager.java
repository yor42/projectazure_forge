package com.yor42.projectazure.client;

import com.yor42.projectazure.client.gui.*;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.gui.ScreenManager;

public class ClientRegisterManager {

    public static void registerScreen(){
        ScreenManager.register(registerManager.SHIP_CONTAINER.get(), guiShipInventory::new);
        ScreenManager.register(registerManager.RIGGING_INVENTORY.get(), guiRiggingInventory::new);
        ScreenManager.register(registerManager.BA_CONTAINER.get(), guiBAInventory::new);
        ScreenManager.register(registerManager.AKN_CONTAINER.get(), GuiAKNInventory::new);

        ScreenManager.register(registerManager.METAL_PRESS_CONTAINER.get(), guiMetalPress::new);
        ScreenManager.register(registerManager.ALLOY_FURNACE_CONTAINER.get(), guiAlloyFurnace::new);
    }

}
