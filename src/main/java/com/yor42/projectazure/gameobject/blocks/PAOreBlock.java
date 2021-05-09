package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.libs.enums;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PAOreBlock extends OreBlock {

    private final String material;
    private final enums.ResourceType resourceType;

    public PAOreBlock(String materialName){
        this(materialName, enums.ResourceType.ORE, (AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2).sound(SoundType.STONE)));
    }

    public PAOreBlock(String materialName, enums.ResourceType resourceType){
        this(materialName, resourceType, (AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2).sound(SoundType.STONE)));
    }

    public PAOreBlock(String materialName, enums.ResourceType resourceType, Block.Properties properties) {
        super(properties);
        this.material = materialName;
        this.resourceType = resourceType;
    }


    @Override
    public IFormattableTextComponent getTranslatedName() {
        return new TranslationTextComponent(material).appendString(" ").append(new TranslationTextComponent(resourceType.getName()));
    }
}
