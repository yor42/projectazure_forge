package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityGunUserBase;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.interfaces.IArknightOperator;
import com.yor42.projectazure.interfaces.IAzurLaneKansen;
import com.yor42.projectazure.libs.enums;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.entity.ai.attributes.Attributes.MAX_HEALTH;

public class ItemKansenSpawnEgg extends Item {

    EntityType<? extends AbstractEntityCompanion> Entity;

    public ItemKansenSpawnEgg(EntityType<? extends AbstractEntityCompanion> Entity, Properties properties) {
        super(properties);
        this.Entity = Entity;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();

        if(context.getPlayer() == null)
            return ActionResultType.FAIL;

        if (!(world instanceof ServerWorld)) {
            return ActionResultType.SUCCESS;
        }
        ItemStack itemstack = context.getItem();
        BlockPos blockpos = context.getPos();

        AbstractEntityCompanion spawnedEntity = this.Entity.create(context.getWorld());

        if(spawnedEntity!=null) {
            spawnedEntity.setPosition(context.getPos().getX(), context.getPos().getY() + 1.1F, context.getPos().getZ());
            spawnedEntity.setTamedBy(context.getPlayer());
            spawnedEntity.setMorale(150);
            spawnedEntity.setAffection(50);
            context.getWorld().addEntity(spawnedEntity);


            if (!context.getPlayer().isCreative())
                itemstack.shrink(1);

            return ActionResultType.CONSUME;
        }

        return ActionResultType.CONSUME;

    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new TranslationTextComponent("item.projectazure.spawnegg.spawn").append(this.Entity.getName());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(Minecraft.getInstance().world != null) {
            AbstractEntityCompanion entity = this.Entity.create(Minecraft.getInstance().world);
            if (entity!= null) {
                tooltip.add(new TranslationTextComponent("tooltip.companion.type").appendString(": ").append(new TranslationTextComponent(entity.getEntityType().getName())));
                if(entity instanceof EntityKansenBase){
                    tooltip.add(new TranslationTextComponent("tooltip.companion.shipgirl_class").appendString(": ").append(new TranslationTextComponent(((EntityKansenBase) entity).getShipClass().getName())));
                }
                else if(entity instanceof IArknightOperator){
                    tooltip.add(new TranslationTextComponent("tooltip.companion.operator_class").appendString(": ").append(new TranslationTextComponent(((IArknightOperator) entity).getOperatorClass().getName())));
                }

                if(entity.getGunSpecialty() != enums.GunClass.NONE){
                    tooltip.add(new TranslationTextComponent("tooltip.companion.gun_speciality").appendString(": ").append(new TranslationTextComponent(entity.getGunSpecialty().getName())));
                }

                tooltip.add(new TranslationTextComponent("tooltip.companion.rarity").appendString(": ").append(new TranslationTextComponent(entity.getRarity().getTranslationkey()).setStyle(Style.EMPTY.setColor(Color.fromInt(entity.getRarity().getColor())))));
                if(entity.getAttribute(MAX_HEALTH) != null) {
                    tooltip.add(new TranslationTextComponent("tooltip.companion.maxhealth").appendString(": ").appendString(String.format("%,.2f", entity.getAttribute(MAX_HEALTH).getBaseValue())));
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
