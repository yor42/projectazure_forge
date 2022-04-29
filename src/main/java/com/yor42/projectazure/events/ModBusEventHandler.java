package com.yor42.projectazure.events;

import com.google.common.base.Throwables;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.lootmodifier.SledgeHammerModifier;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

import static com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability.CapabilityID;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        if (!event.getPlayer().level.isClientSide) {

            CompoundTag playerData = event.getPlayer().getPersistentData();
            CompoundTag data;

            if (!playerData.contains(Player.PERSISTED_NBT_TAG)) {
                data = new CompoundTag();
            } else {
                data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
            }

            boolean flag = !data.getBoolean("PRJA:gotStarterCube");

            if (flag) {
                Player player = event.getPlayer();
                UUID yorUUID = UUID.fromString("d45160dc-ae0b-4f7c-b44a-b535a48182d2");
                UUID AoichiID = UUID.fromString("d189319f-ee53-4e80-9472-7c5e4711642e");
                UUID NecromID = UUID.fromString("23b61d99-fbe4-4202-a6e6-3d467a08f3ba");
                boolean isDev = player.getUUID().equals(yorUUID) || player.getDisplayName().getString().equals("Dev");
                boolean isAoichi = player.getUUID().equals(AoichiID);
                boolean isNecrom = player.getUUID().equals(NecromID);

                ItemStack cubeStack = new ItemStack(Main.Rainbow_Wisdom_Cube.get());
                CompoundTag nbt = cubeStack.getOrCreateTag();
                nbt.putUUID("owner", player.getUUID());
                cubeStack.setTag(nbt);
                player.getInventory().setItem(player.getInventory().getFreeSlot(), cubeStack);
                NonNullList<Item> stacks = NonNullList.create();
                if (isDev) {
                    stacks.add(Main.SPAWN_NAGATO.get());
                    stacks.add(Main.SPAWM_ENTERPRISE.get());
                    stacks.add(Main.SPAWN_CHEN.get());
                    stacks.add(Main.SPAWN_SHIROKO.get());
                    stacks.add(Main.SPAWM_ENTERPRISE.get());
                    stacks.add(Main.SPAWN_AMIYA.get());
                    stacks.add(Main.SPAWN_MUDROCK.get());
                    stacks.add(Main.SPAWN_Z23.get());
                    stacks.add(Main.SPAWM_JAVELIN.get());
                    stacks.add(Main.SPAWN_TALULAH.get());
                    stacks.add(Main.SPAWN_M4A1.get());
                    stacks.add(Main.SPAWN_TEXAS.get());
                    stacks.add(Main.SPAWN_FROSTNOVA.get());
                    stacks.add(Main.SPAWN_LAPPLAND.get());
                    stacks.add(Main.SPAWN_SIEGE.get());
                    stacks.add(Main.SPAWN_SCHWARZ.get());
                }
                else if(isAoichi){
                    stacks.add(Main.SPAWN_MUDROCK.get());
                    stacks.add(Main.SPAWN_ROSMONTIS.get());
                }else if(isNecrom){
                    stacks.add(Main.SPAWN_AMIYA.get());
                }

                if(!stacks.isEmpty()){
                    ItemStack stack = new ItemStack(isDev? Main.DEVELOPER_BONUS.get(): Main.CONTRIBUTOR_BONUS.get());
                    CompoundTag compound = stack.getOrCreateTag();
                    compound.putUUID("owner", player.getUUID());
                    ListTag stackList = new ListTag();
                    for(Item item:stacks){
                        CompoundTag itemTag = new CompoundTag();
                        new ItemStack(item).save(itemTag);
                        stackList.add(itemTag);
                    }
                    compound.put("inventory", stackList);
                    player.getInventory().setItem(player.getInventory().getFreeSlot(), stack);
                }

                data.putBoolean("PRJA:gotStarterCube", true);
                playerData.put(Player.PERSISTED_NBT_TAG, data);

                if(data.contains("carrying_companion")){
                    ListTag list = data.getList("carrying_companion", Tag.TAG_COMPOUND);
                    for(int i = 0; i<list.size(); i++){
                        CompoundTag compound = list.getCompound(i);
                        Optional<Entity> entity = EntityType.create(compound, player.level);
                        entity.ifPresent((ent)->{
                            if(ent instanceof AbstractEntityCompanion){
                                player.level.addFreshEntity(ent);
                                if(ent.startRiding(player, true)){
                                    Main.LOGGER.debug("Successfully loaded entity");
                                }
                            }
                        });
                    }
                    data.remove("carrying_companion");
                }
            }

        }

    }

    @SubscribeEvent
    public void PlayerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getPlayer().level.isClientSide) {
            Player player = event.getPlayer();
            CompoundTag playerData = event.getPlayer().getPersistentData();
            if(!player.getPassengers().isEmpty()){
                ListTag listnbt1 = new ListTag();

                for(Entity entity : player.getPassengers()) {
                    if(entity instanceof AbstractEntityCompanion) {

                        entity.stopRiding();
                        ((AbstractEntityCompanion) entity).setOrderedToSit(true);
                        CompoundTag compoundnbt = new CompoundTag();
                        if (entity.saveAsPassenger(compoundnbt)) {
                            listnbt1.add(compoundnbt);
                        }
                    }
                }
/*
                if (!listnbt1.isEmpty()) {
                    playerData.put("carrying_companion", listnbt1);
                }

 */

            }
        }
    }

    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry()
                .register(new SledgeHammerModifier.Serializer().setRegistryName(Constants.MODID, "sledgehammer"));
    }

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            try {
                event.addCapability(CapabilityID, ProjectAzurePlayerCapability.createNewCapability((Player) event.getObject()));

            }
            catch (Exception e) {
                Main.LOGGER.error("Failed to attach capabilities to player. Player: {}", event.getObject());
                Throwables.throwIfUnchecked(e);
            }
        }
    }

}
