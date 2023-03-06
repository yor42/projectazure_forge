package com.yor42.projectazure.events;

import com.google.common.base.Throwables;
import com.tac.guns.event.GunFireEvent;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.GeoGunItem;
import com.yor42.projectazure.gameobject.items.ItemCompanionSpawnEgg;
import com.yor42.projectazure.gameobject.items.ItemEnergyGun;
import com.yor42.projectazure.intermod.Patchouli;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.lootmodifier.SledgeHammerModifier;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability.CapabilityID;
import static com.yor42.projectazure.gameobject.items.GeoGunItem.ANIM_FIRE;

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

            boolean flag = !data.getBoolean("PRJA:gotStarterItem");

            if (flag) {
                Patchouli.HandlePatchouliCompatibility(event.getPlayer());

                PlayerEntity player = event.getPlayer();
                UUID yorUUID = UUID.fromString("d45160dc-ae0b-4f7c-b44a-b535a48182d2");
                UUID AoichiID = UUID.fromString("d189319f-ee53-4e80-9472-7c5e4711642e");
                UUID NecromID = UUID.fromString("23b61d99-fbe4-4202-a6e6-3d467a08f3ba");
                UUID GilgameshID = UUID.fromString("ce2f01e2-0459-4d2b-9138-a6ef09b85100");
                UUID GuriUUID = UUID.fromString("83d3c9dc-eb3d-4245-bd75-35d3d41b92d5");
                boolean isDev = player.getUUID().equals(yorUUID) || player.getDisplayName().getString().equals("Dev");
                boolean isAoichi = player.getUUID().equals(AoichiID);
                boolean isNecrom = player.getUUID().equals(NecromID);
                boolean isGilagmesh = player.getUUID().equals(GilgameshID);
                boolean isGuri = player.getUUID().equals(GuriUUID);

                ItemStack cubeStack = new ItemStack(RegisterItems.GLITCHED_PHONE.get());
                CompoundNBT nbt = cubeStack.getOrCreateTag();
                nbt.putUUID("owner", player.getUUID());
                player.inventory.setItem(player.inventory.getFreeSlot(), cubeStack);
                NonNullList<Item> stacks = NonNullList.create();
                if (isDev) {
                    stacks.addAll(ItemCompanionSpawnEgg.MAP.values());
                }
                else if(isAoichi){
                    stacks.add(RegisterItems.SPAWN_MUDROCK.get());
                    stacks.add(RegisterItems.SPAWN_ROSMONTIS.get());
                }else if(isNecrom){
                    stacks.add(RegisterItems.SPAWN_AMIYA.get());
                }
                else if(isGilagmesh){
                    stacks.add(RegisterItems.SPAWN_ARTORIA.get());
                    stacks.add(RegisterItems.SPAWN_SHIKI.get());
                    stacks.add(RegisterItems.SPAWN_SCATHATH.get());
                }
                else if(isGuri){
                    stacks.add(RegisterItems.SPAWN_W.get());
                }

                if(!stacks.isEmpty()){
                    ItemStack stack = new ItemStack(isDev? RegisterItems.DEVELOPER_BONUS.get(): RegisterItems.CONTRIBUTOR_BONUS.get());
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

                data.putBoolean("PRJA:gotStarterItem", true);
                playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
            }
            ServerPlayerEntity serverplayer = (ServerPlayerEntity) event.getPlayer();
            ProjectAzureWorldSavedData.getSaveddata(serverplayer.getLevel()).SyncEntireTeamListtoPlayer(serverplayer);

            for(INBT inbt : playerData.getList("PRJA:passengers", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND)){
                if(inbt instanceof CompoundNBT){
                    CompoundNBT compoundNBT = (CompoundNBT) inbt;
                    Entity entity1 = EntityType.loadEntityRecursive(compoundNBT.getCompound("Entity"), serverplayer.getLevel(), (p_217885_1_) -> !serverplayer.getLevel().addWithUUID(p_217885_1_) ? null : p_217885_1_);

                    if(entity1 == null){
                        continue;
                    }

                    serverplayer.getLevel().addFreshEntity(entity1);
                    entity1.startRiding(serverplayer, true);
                }
            }

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

                        CompoundNBT compoundnbt = new CompoundNBT();
                        if (entity.saveAsPassenger(compoundnbt)) {
                            listnbt1.add(compoundnbt);
                        }
                        playerData.put("PRJA:passengers", listnbt1);
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
        World world = event.getPlayer().getCommandSenderWorld();
        if(gunItem instanceof ItemEnergyGun){
            ItemEnergyGun energygun = (ItemEnergyGun) gunItem;
            if(gunstack.getCapability(CapabilityEnergy.ENERGY).map((energyhandler)-> energyhandler.extractEnergy(energygun.getEnergyperShot(), true) < ((ItemEnergyGun) gunItem).getEnergyperShot()).orElse(true)){
                player.displayClientMessage(new TranslationTextComponent("message.energyguns.gun.notenoughenergy").withStyle(TextFormatting.DARK_RED), true);
                SoundEvent sound = ((ItemEnergyGun) gunItem).getNoAmmoSound();
                if(sound != null) {
                    player.playSound(sound, 1, 1);
                }
                event.setCanceled(true);
            }
            else{
                if(!player.abilities.instabuild) {
                    gunstack.getCapability(CapabilityEnergy.ENERGY).ifPresent((energyhandler) -> energyhandler.extractEnergy(energygun.getEnergyperShot(), false));
                }
            }
        }

        if(gunItem instanceof GeoGunItem && !world.isClientSide()){
            final int id = GeckoLibUtil.guaranteeIDForStack(gunstack, (ServerWorld) world);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player);
            GeckoLibNetwork.syncAnimation(target, (ISyncable) gunItem, id, ANIM_FIRE);
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
