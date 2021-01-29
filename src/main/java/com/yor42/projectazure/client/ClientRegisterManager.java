package com.yor42.projectazure.client;

import com.yor42.projectazure.client.gui.guiRiggingInventory;
import com.yor42.projectazure.client.gui.guiShipInventory;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.gui.ScreenManager;

public class ClientRegisterManager {

    public static void registerScreen(){
        ScreenManager.registerFactory(registerManager.SHIP_CONTAINER.get(), guiShipInventory::new);
        ScreenManager.registerFactory(registerManager.DD_DEFAULT_RIGGING_INVENTORY.get(), guiRiggingInventory::new);
    }

}
