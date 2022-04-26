package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ChatFormatting;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemStasisCrystal extends Item {
    public ItemStasisCrystal() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        Level world = context.getLevel();

        if(context.getPlayer() == null)
            return ActionResultType.FAIL;

        if (!(world instanceof ServerWorld)) {
            return ActionResultType.SUCCESS;
        }
        ItemStack itemstack = context.getItemInHand();
        int level = itemstack.getOrCreateTag().getInt("cost");
        CompoundTag compound = itemstack.getOrCreateTag().getCompound("entity");
        PlayerEntity player = context.getPlayer();
        if(player.experienceLevel<level){
            player.displayClientMessage(new TranslatableComponent("message.stasiscrystal.notenoughlevel", level), true);
            return ActionResultType.FAIL;
        }

        Optional<Entity> entity = EntityType.create(compound, world);
        entity.map((spawnedEntity)->{
            if(spawnedEntity instanceof AbstractEntityCompanion) {
                if(((AbstractEntityCompanion) spawnedEntity).getOwner()!= null && player != ((AbstractEntityCompanion) spawnedEntity).getOwner()){
                    player.displayClientMessage(new TranslatableComponent("message.stasiscrystal.notowner", ((AbstractEntityCompanion) spawnedEntity).getOwner().getDisplayName()), true);
                    return ActionResultType.FAIL;
                }
                AbstractEntityCompanion companion = (AbstractEntityCompanion)spawnedEntity;
                companion.setPos(context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 1.1F, context.getClickedPos().getZ() + 0.5);
                context.getLevel().addFreshEntity(spawnedEntity);
                if (!context.getPlayer().isCreative()) {
                    itemstack.shrink(1);
                    player.giveExperienceLevels(-level);
                }
                return ActionResultType.CONSUME;
            }
            else{
                return ActionResultType.PASS;
            }
        });
        return ActionResultType.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_77624_2_, @Nonnull List<Component> list, @Nonnull ITooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, list, p_77624_4_);
        if(p_77624_2_ != null) {
            CompoundTag compound = stack.getOrCreateTag().getCompound("entity");
            int cost = stack.getOrCreateTag().getInt("cost");
            Optional<Entity> entity = EntityType.create(compound, p_77624_2_);
            entity.ifPresent((storedEntity) ->{
                list.add(new TranslatableComponent("tooltip.companion.name").withStyle(ChatFormatting.GRAY).append(": ").append(storedEntity.getDisplayName()).withStyle(ChatFormatting.YELLOW));
                if(storedEntity instanceof AbstractEntityCompanion && ((AbstractEntityCompanion)storedEntity).getOwner()!= null) {
                    list.add(new TranslatableComponent("tooltip.companion.owner", ((AbstractEntityCompanion) storedEntity).getOwner().getDisplayName()).withStyle(ChatFormatting.YELLOW));
                }
                list.add(new TranslatableComponent("tooltip.companion.cost", cost).withStyle(ChatFormatting.YELLOW));
            });
        }
    }
}
