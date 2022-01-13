package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static com.yor42.projectazure.Main.PA_RESOURCES;

import net.minecraft.item.Item.Properties;

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
    public ITextComponent getName(ItemStack stack) {
        return new TranslationTextComponent(material).append(" ").append(new TranslationTextComponent(resourceType.getName()));
    }
}
