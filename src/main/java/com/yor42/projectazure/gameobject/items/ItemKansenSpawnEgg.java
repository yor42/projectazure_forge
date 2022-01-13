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

import net.minecraft.item.Item.Properties;

public class ItemKansenSpawnEgg extends Item {

    EntityType<? extends AbstractEntityCompanion> Entity;

    public ItemKansenSpawnEgg(EntityType<? extends AbstractEntityCompanion> Entity, Properties properties) {
        super(properties);
        this.Entity = Entity;
    }

    @Override
    @MethodsReturnNonnullByDefault
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();

        if(context.getPlayer() == null)
            return ActionResultType.FAIL;

        if (!(world instanceof ServerWorld)) {
            return ActionResultType.SUCCESS;
        }
        ItemStack itemstack = context.getItemInHand();
        context.getClickedPos();

        AbstractEntityCompanion spawnedEntity = this.Entity.create(context.getLevel());

        if(spawnedEntity!=null) {
            spawnedEntity.setPos(context.getClickedPos().getX()+0.5, context.getClickedPos().getY() + 1.1F, context.getClickedPos().getZ()+0.5);
            spawnedEntity.tame(context.getPlayer());
            spawnedEntity.setMorale(150);
            if(!(spawnedEntity instanceof IAknOp)) {
                spawnedEntity.setAffection(50);
            }
            context.getLevel().addFreshEntity(spawnedEntity);


            if (!context.getPlayer().isCreative())
                itemstack.shrink(1);

            return ActionResultType.CONSUME;
        }

        return ActionResultType.CONSUME;

    }

    @Override
    @ParametersAreNonnullByDefault
    public ITextComponent getName(ItemStack stack) {
        return new TranslationTextComponent("item.projectazure.spawnegg.spawn").append(this.Entity.getDescription());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if(worldIn != null) {
            AbstractEntityCompanion entity = this.Entity.create(worldIn);
            if (entity!= null) {

                enums.CompanionRarity rarity = entity.getRarity();
                tooltip.add(new TranslationTextComponent("tooltip.companion.rarity").append(": ").append(new TranslationTextComponent(entity.getRarity().getTranslationkey()).setStyle(Style.EMPTY.withColor(Color.fromRgb(rarity.getColor())))).withStyle(TextFormatting.GRAY));
                tooltip.add(new TranslationTextComponent("tooltip.companion.type").append(": ").append(new TranslationTextComponent(entity.getEntityType().getName())).withStyle(TextFormatting.GRAY));
                if(entity instanceof EntityKansenBase){
                    tooltip.add(new TranslationTextComponent("tooltip.companion.shipgirl_class").append(": ").append(new TranslationTextComponent(((EntityKansenBase) entity).getShipClass().getName()).withStyle(TextFormatting.YELLOW)).withStyle(TextFormatting.GRAY));
                }
                else if(entity instanceof IAknOp){
                    tooltip.add(new TranslationTextComponent("tooltip.companion.operator_class").append(": ").withStyle(TextFormatting.GRAY).append(new TranslationTextComponent(((IAknOp) entity).getOperatorClass().getName()).withStyle(TextFormatting.YELLOW)));
                }

                if(entity.getGunSpecialty() != enums.GunClass.NONE){
                    tooltip.add(new TranslationTextComponent("tooltip.companion.gun_speciality").append(": ").append(new TranslationTextComponent(entity.getGunSpecialty().getName()).withStyle(TextFormatting.YELLOW)).withStyle(TextFormatting.GRAY));
                }
                if(entity.getAttribute(MAX_HEALTH) != null) {
                    tooltip.add(new TranslationTextComponent("tooltip.companion.maxhealth").append(": ").append(new StringTextComponent(String.format("%,.2f", entity.getAttribute(MAX_HEALTH).getBaseValue())).withStyle(Style.EMPTY.withColor(Color.fromRgb(0xFFC0CB)))).withStyle(TextFormatting.GRAY));
                }
            }
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
