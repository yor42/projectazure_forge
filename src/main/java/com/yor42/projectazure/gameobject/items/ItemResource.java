package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.yor42.projectazure.Main.PA_RESOURCES;

public class ItemResource extends Item {

    private final String material;
    private final enums.ResourceItemType resourceItemType;

    public ItemResource(String materialName, enums.ResourceItemType resourceItemType){
        this(materialName, resourceItemType, new Item.Properties().tab(PA_RESOURCES));
    }

    public ItemResource(String materialName, enums.ResourceItemType resourceItemType, Properties properties) {
        super(properties);
        this.material = materialName;
        this.resourceItemType = resourceItemType;
    }

    public enums.ResourceItemType getResourceType(){
        return this.resourceItemType;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return new TranslatableComponent(resourceItemType.getName(), new TranslatableComponent(material));
    }
}
