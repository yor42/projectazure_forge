package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.Item;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

import static com.yor42.projectazure.Main.PA_RESOURCES;

public class ItemResource extends Item {

    private final String material;
    private final enums.ResourceType resourceType;

    public ItemResource(String materialName, enums.ResourceType resourceType){
        this(materialName, resourceType, new Item.Properties().tab(PA_RESOURCES));
    }

    public ItemResource(String materialName, enums.ResourceType resourceType, Properties properties) {
        super(properties);
        this.material = materialName;
        this.resourceType = resourceType;
    }

    public enums.ResourceType getResourceType(){
        return this.resourceType;
    }

    @Override
    public Component getName(ItemStack stack) {
        return new TranslatableComponent(material).append(" ").append(new TranslatableComponent(resourceType.getName()));
    }
}
