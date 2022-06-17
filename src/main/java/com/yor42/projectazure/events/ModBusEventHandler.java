package com.yor42.projectazure.events;

import com.google.common.base.Throwables;
import com.tac.guns.event.GunFireEvent;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.ItemEnergyGun;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.lootmodifier.SledgeHammerModifier;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability.CapabilityID;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        if (!event.getPlayer().level.isClientSide) {

            CompoundNBT playerData = event.getPlayer().getPersistentData();
            CompoundNBT data;

            if (!playerData.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
                data = new CompoundNBT();
            } else {
                data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            }

            boolean flag = !data.getBoolean("PRJA:gotStarterCube");

            if (flag) {
                PlayerEntity player = event.getPlayer();
                UUID yorUUID = UUID.fromString("d45160dc-ae0b-4f7c-b44a-b535a48182d2");
                UUID AoichiID = UUID.fromString("d189319f-ee53-4e80-9472-7c5e4711642e");
                UUID NecromID = UUID.fromString("23b61d99-fbe4-4202-a6e6-3d467a08f3ba");
                boolean isDev = player.getUUID().equals(yorUUID) || player.getDisplayName().getString().equals("Dev");
                boolean isAoichi = player.getUUID().equals(AoichiID);
                boolean isNecrom = player.getUUID().equals(NecromID);

                ItemStack cubeStack = new ItemStack(registerItems.GLITCHED_PHONE.get());
                CompoundNBT nbt = cubeStack.getOrCreateTag();
                nbt.putUUID("owner", player.getUUID());
                cubeStack.setTag(nbt);
                player.inventory.setItem(player.inventory.getFreeSlot(), cubeStack);
                NonNullList<Item> stacks = NonNullList.create();
                if (isDev) {
                    stacks.add(registerItems.SPAWN_NAGATO.get());
                    stacks.add(registerItems.SPAWM_ENTERPRISE.get());
                    stacks.add(registerItems.SPAWN_CHEN.get());
                    stacks.add(registerItems.SPAWN_SHIROKO.get());
                    stacks.add(registerItems.SPAWM_ENTERPRISE.get());
                    stacks.add(registerItems.SPAWN_AMIYA.get());
                    stacks.add(registerItems.SPAWN_MUDROCK.get());
                    stacks.add(registerItems.SPAWN_Z23.get());
                    stacks.add(registerItems.SPAWM_JAVELIN.get());
                    stacks.add(registerItems.SPAWN_TALULAH.get());
                    stacks.add(registerItems.SPAWN_M4A1.get());
                    stacks.add(registerItems.SPAWN_TEXAS.get());
                    stacks.add(registerItems.SPAWN_FROSTNOVA.get());
                    stacks.add(registerItems.SPAWN_LAPPLAND.get());
                    stacks.add(registerItems.SPAWN_SIEGE.get());
                    stacks.add(registerItems.SPAWN_SCHWARZ.get());
                    stacks.add(registerItems.SPAWN_CROWNSLAYER.get());
                }
                else if(isAoichi){
                    stacks.add(registerItems.SPAWN_MUDROCK.get());
                    stacks.add(registerItems.SPAWN_ROSMONTIS.get());
                }else if(isNecrom){
                    stacks.add(registerItems.SPAWN_AMIYA.get());
                }

                if(!stacks.isEmpty()){
                    ItemStack stack = new ItemStack(isDev? registerItems.DEVELOPER_BONUS.get():registerItems.CONTRIBUTOR_BONUS.get());
                    CompoundNBT compound = stack.getOrCreateTag();
                    compound.putUUID("owner", player.getUUID());
                    ListNBT stackList = new ListNBT();
                    for(Item item:stacks){
                        CompoundNBT itemTag = new CompoundNBT();
                        new ItemStack(item).save(itemTag);
                        stackList.add(itemTag);
                    }
                    compound.put("inventory", stackList);
                    player.inventory.setItem(player.inventory.getFreeSlot(), stack);
                }

                data.putBoolean("PRJA:gotStarterCube", true);
                playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);

                Optional<Entity> entity = Optional.ofNullable(EntityType.loadEntityRecursive(data, player.level, Function.identity()));
                entity.ifPresent((ent)->{
                    if(ent instanceof AbstractEntityCompanion){
                        player.level.addFreshEntity(ent);
                        if(ent.startRiding(player, true)){
                            Main.LOGGER.debug("Successfully loaded entity");
                        }
                    }
                });
            }
            ServerPlayerEntity serverplayer = (ServerPlayerEntity) event.getPlayer();
            ProjectAzureWorldSavedData.getSaveddata(serverplayer.getLevel()).SyncEntireTeamListtoPlayer(serverplayer);

        }

    }

    @SubscribeEvent
    public void PlayerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getPlayer().level.isClientSide) {
            PlayerEntity player = event.getPlayer();
            CompoundNBT playerData = event.getPlayer().getPersistentData();
            if(!player.getPassengers().isEmpty()){
                ListNBT listnbt1 = new ListNBT();

                for(Entity entity : player.getPassengers()) {
                    if(entity instanceof AbstractEntityCompanion) {

                        entity.stopRiding();
                        ((AbstractEntityCompanion) entity).setOrderedToSit(true);
                        /*
                        CompoundNBT compoundnbt = new CompoundNBT();
                        if (entity.saveAsPassenger(compoundnbt)) {
                            listnbt1.add(compoundnbt);
                        }

                         */
                    }
                }

                ProjectAzureWorldSavedData.TeamListCLIENT.clear();

            }
        }
    }

    @SubscribeEvent
    public void onGunFire(GunFireEvent.Pre event) {
        ItemStack gunstack = event.getStack();
        Item gunItem = gunstack.getItem();
        PlayerEntity player = event.getPlayer();
        if(gunItem instanceof ItemEnergyGun){
            ItemEnergyGun energygun = (ItemEnergyGun) gunItem;
            if(gunstack.getCapability(CapabilityEnergy.ENERGY).map((energyhandler)-> energyhandler.extractEnergy(energygun.getEnergyperShot(), true) < ((ItemEnergyGun) gunItem).getEnergyperShot()).orElse(true)){
                player.sendMessage(new TranslationTextComponent("message.energyguns.gun.notenoughenergy"), UUID.randomUUID());
                event.setCanceled(true);
            }
            else{
                gunstack.getCapability(CapabilityEnergy.ENERGY).ifPresent((energyhandler)-> energyhandler.extractEnergy(energygun.getEnergyperShot(), false));
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
        if (event.getObject() instanceof PlayerEntity) {
            try {
                event.addCapability(CapabilityID, ProjectAzurePlayerCapability.createNewCapability((PlayerEntity) event.getObject()));

            }
            catch (Exception e) {
                Main.LOGGER.error("Failed to attach capabilities to player. Player: {}", event.getObject());
                Throwables.throwIfUnchecked(e);
            }
        }
    }

}
