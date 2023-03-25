package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.gameobject.containers.slots.DroneDockingStationInvSlot;
import com.yor42.projectazure.gameobject.containers.slots.DummySlot;
import com.yor42.projectazure.libs.ItemFilterEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerDroneDockingStation extends AbstractContainerMenu {

    private ScreenMode screenMode;
    private String Filter_ID = "";
    private ItemFilterEntry.ItemTypes FilterType = ItemFilterEntry.ItemTypes.ITEM;

    protected ContainerDroneDockingStation(@Nullable MenuType<?> p_i50105_1_, Inventory inventory, int p_i50105_2_) {
        this(p_i50105_1_, inventory, new ItemStackHandler(18), p_i50105_2_);
    }

    public ContainerDroneDockingStation(@Nullable MenuType<?> p_i50105_1_, Inventory inventory, ItemStackHandler dockingstationInventory, int p_i50105_2_) {
        super(p_i50105_1_, p_i50105_2_);
        this.screenMode = ScreenMode.NORMAL;
        this.populateDockingStationInv(dockingstationInventory);
        this.addSlot(new DummySlot(this, 57, 58));
        this.populatePlayerInv(inventory);
    }

    public void SetFilterIDFromItemStack(ItemStack stack){
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if(key == null){
            return;
        }
        this.Filter_ID =key.toString();
        this.FilterType = ItemFilterEntry.ItemTypes.ITEM;
    }

    public void SetFilterID(String string){
        this.Filter_ID = string;
    }

    public String GetFilterID(){
        return this.Filter_ID;
    }



    private void populatePlayerInv(Inventory inventory){
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    private void populateDockingStationInv(ItemStackHandler inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new DroneDockingStationInvSlot(this, ScreenMode.NORMAL, inventory, j + i * 3, 49 + j * 18, 18 + i * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new DroneDockingStationInvSlot(this, ScreenMode.NORMAL, inventory, j + i * 3+9, 156 + j * 18, 18 + i * 18));
            }
        }
    }

    public void ChangeScreenMode(){
        this.SetFilterID("");
        switch (this.screenMode){
            case NORMAL:
                this.screenMode = ScreenMode.EDITFILTER;
                break;
            case EDITFILTER:
                this.screenMode = ScreenMode.NORMAL;
                break;
        }
    }

    public ScreenMode getScreenMode() {
        return this.screenMode;
    }

    @Override
    public boolean stillValid(@Nonnull Player p_75145_1_) {
        return true;
    }

    public enum ScreenMode{
        NORMAL, EDITFILTER
    }
}