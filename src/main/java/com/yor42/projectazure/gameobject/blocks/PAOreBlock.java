package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.libs.enums;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Objects;

public class PAOreBlock extends OreBlock {

    private final String material;
    private final enums.ResourceBlockType resourceType;

    public PAOreBlock(String materialName, enums.ResourceBlockType resourceType) {
        super((BlockBehaviour.Properties.of(Objects.requireNonNull(resourceType.getMaterial())).friction(2).sound(Objects.requireNonNull(resourceType.getSoundType())).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
        this.material = materialName;
        this.resourceType = resourceType;
    }

    @Override
    public MutableComponent getName() {
        return new TranslatableComponent(resourceType.getName(), new TranslatableComponent(material));
    }
}
