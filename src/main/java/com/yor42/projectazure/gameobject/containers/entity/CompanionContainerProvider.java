package com.yor42.projectazure.gameobject.containers.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CompanionContainerProvider implements MenuProvider {

    @Nonnull
    private final AbstractEntityCompanion companion;

    public CompanionContainerProvider(@Nonnull AbstractEntityCompanion companion){
        this.companion = companion;
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.companioninventory");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_createMenu_1_, @Nonnull Inventory p_createMenu_2_, @Nonnull Player p_createMenu_3_) {
        return this.companion.getEntityType().createmenu(p_createMenu_1_,p_createMenu_2_, p_createMenu_3_, this.companion);
    }
}
