package com.yor42.projectazure.gameobject;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.playercapability.CompanionTeam;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.network.packets.SyncTeamListPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProjectAzureWorldSavedData extends WorldSavedData {

    public static final String ID = Constants.MODID+"_worldsaveddata";

    @OnlyIn(Dist.CLIENT)
    public static List<CompanionTeam> TeamListCLIENT;

    //Server
    public ArrayList<CompanionTeam> TeamList = new ArrayList<>();

    static {
        if(Main.isClient()){
            TeamListCLIENT = new ArrayList<>();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void DeserializeandUpdateClientList(CompoundNBT compound){
        CompanionTeam team = CompanionTeam.deserializeNBT(compound);
        boolean processed = false;
        for(int i = 0; i<TeamListCLIENT.size();i++){
            CompanionTeam existingEntry = TeamListCLIENT.get(i);
            if(existingEntry.getTeamUUID().equals(team.getTeamUUID())){
                processed = true;
                TeamListCLIENT.set(i, team);
                break;
            }
        }
        //It's new team. add it to entries.
        if(!processed){
            TeamListCLIENT.add(team);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void RemoveEntryFromClientList(CompoundNBT compound){
        CompanionTeam team = CompanionTeam.deserializeNBT(compound);
        for(CompanionTeam listentry:TeamListCLIENT){
            if(listentry.getTeamUUID().equals(team.getTeamUUID())){
                TeamListCLIENT.remove(team);
                break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void DeserializeandUpdateEntireClientList(CompoundNBT compound){
        ListNBT teams = compound.getList("teams", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for(int i=0; i<teams.size(); i++){
            CompoundNBT nbt = teams.getCompound(i);
            TeamListCLIENT.add(CompanionTeam.deserializeNBT(nbt));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static List<CompanionTeam> getPlayersTeamClient(PlayerEntity player){
        return TeamListCLIENT.stream().filter((team)->team.getOwnerUUID().equals(player.getUUID())).collect(Collectors.toList());
    }

    public ProjectAzureWorldSavedData() {
        super(ID);
    }

    @Override
    public void load(CompoundNBT p_76184_1_) {
        ListNBT teams = p_76184_1_.getList("teams", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for(int i=0; i<teams.size(); i++){
            CompoundNBT nbt = teams.getCompound(i);
            this.TeamList.add(CompanionTeam.deserializeNBT(nbt));
        }
        this.SyncEntireTeamListClient();
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        ListNBT TeamList = new ListNBT();
        for(CompanionTeam team: this.TeamList){
            TeamList.add(team.serializeNBT());
        }
        nbt.put("teams", TeamList);
        return nbt;
    }

    public void addorModifyTeam(CompanionTeam team){
        boolean processed = false;
        for(int i = 0; i<this.TeamList.size();i++){
            CompanionTeam existingEntry = this.TeamList.get(i);
            if(existingEntry.getTeamUUID().equals(team.getTeamUUID())){
                processed = true;
                this.TeamList.set(i, team);
                break;
            }
        }
        //It's new team. add it to entries.
        if(!processed){
            this.TeamList.add(team);
        }
        this.SyncTeamEntriestoClient(team);
        this.setDirty();
    }

    public void removeTeam(CompanionTeam team){
        this.TeamList.remove(team);
        this.SyncTeamEntriestoClient(team, SyncTeamListPacket.ACTION.REMOVE);
        this.setDirty();
    }

    public Optional<CompanionTeam> getTeambyUUID(UUID uuid) {
        if(!this.TeamList.isEmpty()) {
            for (CompanionTeam team : this.TeamList) {
                if (team.getTeamUUID() == uuid) {
                    return Optional.of(team);
                }
            }
        }
        return Optional.empty();
    }

    public void createteam(ServerPlayerEntity owner){
        CompanionTeam newteam = new CompanionTeam(owner.getUUID(), owner.getDisplayName().getString());
        this.addorModifyTeam(newteam);
    }

    public List<CompanionTeam> getPlayersTeam(PlayerEntity player){
        return this.TeamList.stream().filter((team)->team.getOwnerUUID().equals(player.getUUID())).collect(Collectors.toList());
    }

    private void SyncTeamEntriestoClient(CompanionTeam team, SyncTeamListPacket.ACTION action){
        Main.NETWORK.send(PacketDistributor.ALL.noArg(), new SyncTeamListPacket(team.serializeNBT(), action));
    }

    private void SyncTeamEntriestoClient(CompanionTeam team){
        this.SyncTeamEntriestoClient(team, SyncTeamListPacket.ACTION.ADD);
    }

    private void SyncEntireTeamListClient(){
        CompoundNBT compound = new CompoundNBT();
        ListNBT TeamList = new ListNBT();
        for(CompanionTeam team: this.TeamList){
            TeamList.add(team.serializeNBT());
        }
        compound.put("teams", TeamList);
        Main.NETWORK.send(PacketDistributor.ALL.noArg(), new SyncTeamListPacket(compound, SyncTeamListPacket.ACTION.ADD_BATCH));
        this.setDirty();
    }

    public static ProjectAzureWorldSavedData getSaveddata(ServerWorld world){
        ProjectAzureWorldSavedData storage = world.getDataStorage().get(ProjectAzureWorldSavedData::new, ID);
        if(storage == null){
            storage = new ProjectAzureWorldSavedData();
            world.getDataStorage().set(storage);
        }
        return storage;
    }
}
