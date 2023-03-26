package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemStasisCrystal extends Item {
    public ItemStasisCrystal() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();

        if(context.getPlayer() == null)
            return InteractionResult.FAIL;

        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ItemStack itemstack = context.getItemInHand();
        int level = itemstack.getOrCreateTag().getInt("cost");
        CompoundTag compound = itemstack.getOrCreateTag().getCompound("entity");
        Player player = context.getPlayer();
        if(player.experienceLevel<level){
            player.displayClientMessage(new TranslatableComponent("message.stasiscrystal.notenoughlevel", level), true);
            return InteractionResult.FAIL;
        }

        Optional<Entity> entity = EntityType.create(compound, world);
        entity.map((spawnedEntity)->{
            if(spawnedEntity instanceof AbstractEntityCompanion) {
                if(((AbstractEntityCompanion) spawnedEntity).getOwner()!= null && player != ((AbstractEntityCompanion) spawnedEntity).getOwner()){
                    player.displayClientMessage(new TranslatableComponent("message.stasiscrystal.notowner", ((AbstractEntityCompanion) spawnedEntity).getOwner().getDisplayName()), true);
                    return InteractionResult.FAIL;
                }
                AbstractEntityCompanion companion = (AbstractEntityCompanion)spawnedEntity;
                companion.setPos(context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 1.1F, context.getClickedPos().getZ() + 0.5);
                ((AbstractEntityCompanion) spawnedEntity).getTeamUUID().ifPresent((uuid)->ProjectAzureWorldSavedData.getSaveddata((ServerLevel) world).getTeambyUUID(uuid).ifPresent((team)->{
                    if(!team.addEntitytoTeam(companion.getUUID())){
                        companion.setTeam(null);
                    }
                }));
                ProjectAzurePlayerCapability.getCapability(player).addCompanion(companion);
                context.getLevel().addFreshEntity(spawnedEntity);
                if (!context.getPlayer().isCreative()) {
                    itemstack.shrink(1);
                    player.giveExperienceLevels(-level);
                }
                return InteractionResult.CONSUME;
            }
            else{
                return InteractionResult.PASS;
            }
        });
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_77624_2_, @Nonnull List<Component> list, @Nonnull TooltipFlag p_77624_4_) {
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
