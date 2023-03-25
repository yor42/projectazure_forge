package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.text.*;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static net.minecraft.entity.ai.attributes.Attributes.MAX_HEALTH;

publicimport net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item.Properties;

 clasnet.minecraft.world.entity.ai.attributes.AttributesityCompanion> extends Item {

    private final RegistryObject<EntityType<T>> Entity;

    public static final HashMap<String, ItemCompanionSpawnEgg<?>> MAP = new HashMap<>();

    public ItemCompanionSpawnEgg(RegistryObject<EntityType<T>> type, Properties properties) {
        super(properties);
        this.Entity = type;
        MAP.put(type.getId().toString(), this);
    }

    @Nullable
    public static ItemCompanionSpawnEgg<?> fromEntityType(@Nullable EntityType<?> type)
    {
        if(type == null){
            return null;
        }
        String string = Objects.requireNonNull(type.getRegistryName()).toString();

        return MAP.get(string);
    }

    @Nonnull
    @Override
    @MethodsReturnNonnullByDefault
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();

        if(context.getPlayer() == null)
            return InteractionResult.FAIL;

        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ItemStack itemstack = context.getItemInHand();
        context.getClickedPos();

        AbstractEntityCompanion spawnedEntity = this.Entity.get().create(context.getLevel());

        if(spawnedEntity!=null) {
            spawnedEntity.setPos(context.getClickedPos().getX()+0.5, context.getClickedPos().getY() + 1.1F, context.getClickedPos().getZ()+0.5);
            spawnedEntity.handleInitialspawn(context.getPlayer());
            context.getLevel().addFreshEntity(spawnedEntity);


            if (!context.getPlayer().isCreative())
                itemstack.shrink(1);

            return InteractionResult.CONSUME;
        }

        return InteractionResult.CONSUME;

    }

    public EntityType<?> getType(@Nullable CompoundTag tag)
    {
        return this.Entity.get();
    }

    @Override
    @ParametersAreNonnullByDefault
    public Component getName(ItemStack stack) {
        return new TranslatableComponent("item.projectazure.spawnegg.spawn").append(this.Entity.get().getDescription());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if(worldIn != null) {
            AbstractEntityCompanion entity = this.Entity.get().create(worldIn);
            if (entity!= null) {

                enums.CompanionRarity rarity = entity.getRarity();
                tooltip.add(new TranslatableComponent("tooltip.companion.rarity").append(": ").append(new TranslatableComponent(entity.getRarity().getTranslationkey()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rarity.getColor())))).withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.companion.type").append(": ").append(new TranslatableComponent(entity.getEntityType().getName())).withStyle(ChatFormatting.GRAY));
                if(entity instanceof EntityKansenBase){
                    tooltip.add(new TranslatableComponent("tooltip.companion.shipgirl_class").append(": ").append(new TranslatableComponent(((EntityKansenBase) entity).getShipClass().getName()).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
                }
                else if(entity instanceof IAknOp){
                    tooltip.add(new TranslatableComponent("tooltip.companion.operator_class").append(": ").withStyle(ChatFormatting.GRAY).append(new TranslatableComponent(((IAknOp) entity).getOperatorClass().getName()).withStyle(ChatFormatting.YELLOW)));
                }

                if(entity.getGunSpecialty() != enums.GunClass.NONE){
                    tooltip.add(new TranslatableComponent("tooltip.companion.gun_speciality").append(": ").append(new TranslatableComponent(entity.getGunSpecialty().getName()).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
                }
                if(entity.getAttribute(MAX_HEALTH) != null) {
                    tooltip.add(new TranslatableComponent("tooltip.companion.maxhealth").append(": ").append(new TextComponent(String.format("%,.2f", entity.getAttribute(MAX_HEALTH).getBaseValue())).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFC0CB)))).withStyle(ChatFormatting.GRAY));
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
