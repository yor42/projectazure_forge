package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static com.yor42.projectazure.Main.PA_RESOURCES;

import net.minecraft.item.Item.Properties;

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
    public ITextComponent getName(ItemStack stack) {
        return new TranslationTextComponent(material).append(" ").append(new TranslationTextComponent(resourceType.getName()));
    }
}
