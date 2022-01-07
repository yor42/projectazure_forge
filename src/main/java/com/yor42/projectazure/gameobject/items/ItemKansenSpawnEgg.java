package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.minecraft.entity.ai.attributes.Attributes.MAX_HEALTH;

public class ItemKansenSpawnEgg extends Item {

    EntityType<? extends AbstractEntityCompanion> Entity;

    public ItemKansenSpawnEgg(EntityType<? extends AbstractEntityCompanion> Entity, Properties properties) {
        super(properties);
        this.Entity = Entity;
    }

    @Override
    @MethodsReturnNonnullByDefault
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();

        if(context.getPlayer() == null)
            return ActionResultType.FAIL;

        if (!(world instanceof ServerWorld)) {
            return ActionResultType.SUCCESS;
        }
        ItemStack itemstack = context.getItem();
        context.getPos();

        AbstractEntityCompanion spawnedEntity = this.Entity.create(context.getWorld());

        if(spawnedEntity!=null) {
            spawnedEntity.setPosition(context.getPos().getX()+0.5, context.getPos().getY() + 1.1F, context.getPos().getZ()+0.5);
            spawnedEntity.setTamedBy(context.getPlayer());
            spawnedEntity.setMorale(150);
            if(!(spawnedEntity instanceof IAknOp)) {
                spawnedEntity.setAffection(50);
            }
            context.getWorld().addEntity(spawnedEntity);


            if (!context.getPlayer().isCreative())
                itemstack.shrink(1);

            return ActionResultType.CONSUME;
        }

        return ActionResultType.CONSUME;

    }

    @Override
    @ParametersAreNonnullByDefault
    public ITextComponent getDisplayName(ItemStack stack) {
        return new TranslationTextComponent("item.projectazure.spawnegg.spawn").append(this.Entity.getName());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(worldIn != null) {
            AbstractEntityCompanion entity = this.Entity.create(worldIn);
            if (entity!= null) {

                enums.CompanionRarity rarity = entity.getRarity();
                tooltip.add(new TranslationTextComponent("tooltip.companion.rarity").appendString(": ").append(new TranslationTextComponent(entity.getRarity().getTranslationkey()).setStyle(Style.EMPTY.setColor(Color.fromInt(rarity.getColor())))).mergeStyle(TextFormatting.GRAY));
                tooltip.add(new TranslationTextComponent("tooltip.companion.type").appendString(": ").append(new TranslationTextComponent(entity.getEntityType().getName())).mergeStyle(TextFormatting.GRAY));
                if(entity instanceof EntityKansenBase){
                    tooltip.add(new TranslationTextComponent("tooltip.companion.shipgirl_class").appendString(": ").append(new TranslationTextComponent(((EntityKansenBase) entity).getShipClass().getName()).mergeStyle(TextFormatting.YELLOW)).mergeStyle(TextFormatting.GRAY));
                }
                else if(entity instanceof IAknOp){
                    tooltip.add(new TranslationTextComponent("tooltip.companion.operator_class").appendString(": ").mergeStyle(TextFormatting.GRAY).append(new TranslationTextComponent(((IAknOp) entity).getOperatorClass().getName()).mergeStyle(TextFormatting.YELLOW)));
                }

                if(entity.getGunSpecialty() != enums.GunClass.NONE){
                    tooltip.add(new TranslationTextComponent("tooltip.companion.gun_speciality").appendString(": ").append(new TranslationTextComponent(entity.getGunSpecialty().getName()).mergeStyle(TextFormatting.YELLOW)).mergeStyle(TextFormatting.GRAY));
                }
                if(entity.getAttribute(MAX_HEALTH) != null) {
                    tooltip.add(new TranslationTextComponent("tooltip.companion.maxhealth").appendString(": ").append(new StringTextComponent(String.format("%,.2f", entity.getAttribute(MAX_HEALTH).getBaseValue())).mergeStyle(Style.EMPTY.setColor(Color.fromInt(0xFFC0CB)))).mergeStyle(TextFormatting.GRAY));
                }
            }
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
