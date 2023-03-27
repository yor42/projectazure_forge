package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.libs.enums;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class PAOreBlock extends OreBlock {

    private final String material;
    private final enums.ResourceType resourceType;

    public PAOreBlock(String materialName, enums.ResourceType resourceType) {
        super((BlockBehaviour.Properties.of(resourceType == enums.ResourceType.BLOCK? Material.METAL:Material.STONE).friction(2).sound(resourceType == enums.ResourceType.BLOCK? SoundType.METAL:SoundType.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
        this.material = materialName;
        this.resourceType = resourceType;
    }

    public PAOreBlock(String materialName, enums.ResourceType resourceType, Block.Properties properties) {
        super(properties);
        this.material = materialName;
        this.resourceType = resourceType;
    }


    @Override
    public MutableComponent getName() {
        return new TranslatableComponent(material).append(" ").append(new TranslatableComponent(resourceType.getName()));
    }
}
