package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import static com.yor42.projectazure.Main.PA_RESOURCES;

import net.minecraft.world.item.Item.Properties;

public class PAOreBlockItem extends BlockItem {

    private final String material;
    private final enums.ResourceType resourceType;

    public PAOreBlockItem(Block blockIn, String materialName){
        this(blockIn, materialName, enums.ResourceType.ORE, new Item.Properties().tab(PA_RESOURCES));
    }

    public PAOreBlockItem(Block blockIn, String materialName, enums.ResourceType resourceType){
        this(blockIn, materialName, resourceType, new Item.Properties().tab(PA_RESOURCES));
    }

    public PAOreBlockItem(Block blockIn, String materialName, enums.ResourceType resourceType, Properties builder) {
        super(blockIn, builder);
        this.material = materialName;
        this.resourceType = resourceType;
    }

    @Override
    public Component getName(ItemStack stack) {
        return new TranslatableComponent(material).append(" ").append(new TranslatableComponent(resourceType.getName()));
    }
}
