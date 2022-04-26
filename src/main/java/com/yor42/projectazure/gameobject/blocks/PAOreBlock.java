package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.libs.enums;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslatableComponent;

public class PAOreBlock extends OreBlock {

    private final String material;
    private final enums.ResourceType resourceType;

    public PAOreBlock(String materialName){
        this(materialName, enums.ResourceType.ORE, (AbstractBlock.Properties.of(Material.STONE).harvestLevel(2).sound(SoundType.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    }

    public PAOreBlock(String materialName, enums.ResourceType resourceType, Block.Properties properties) {
        super(properties);
        this.material = materialName;
        this.resourceType = resourceType;
    }


    @Override
    public IFormattableTextComponent getName() {
        return new TranslatableComponent(material).append(" ").append(new TranslatableComponent(resourceType.getName()));
    }
}
