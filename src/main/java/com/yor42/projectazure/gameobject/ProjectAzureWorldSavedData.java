package com.yor42.projectazure.gameobject;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.playercapability.CompanionTeam;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.network.packets.SyncTeamListPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProjectAzureWorldSavedData extends SavedData {

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
    public static void DeserializeandUpdateClientList(CompoundTag compound){
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
    public static void RemoveEntryFromClientList(CompoundTag compound){
        CompanionTeam team = CompanionTeam.deserializeNBT(compound);
        for(CompanionTeam listentry:TeamListCLIENT){
            if(listentry.getTeamUUID().equals(team.getTeamUUID())){
                TeamListCLIENT.remove(team);
                break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void DeserializeandUpdateEntireClientList(CompoundTag compound){
        ListTag teams = compound.getList("teams", Tag.TAG_COMPOUND);
        TeamListCLIENT.clear();
        for(int i=0; i<teams.size(); i++){
            CompoundTag nbt = teams.getCompound(i);
            TeamListCLIENT.add(CompanionTeam.deserializeNBT(nbt));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static List<CompanionTeam> getPlayersTeamClient(Player player){
        return TeamListCLIENT.stream().filter((team)->team.getOwnerUUID().equals(player.getUUID())).collect(Collectors.toList());
    }

    @OnlyIn(Dist.CLIENT)
    public static Optional<CompanionTeam> getTeambyUUIDClient(UUID TeamUUID){
        if(!TeamListCLIENT.isEmpty()) {
            for (CompanionTeam team : TeamListCLIENT) {
                if (team.getTeamUUID().equals(TeamUUID)) {
                    return Optional.of(team);
                }
            }
        }
        return Optional.empty();
    }
    public ProjectAzureWorldSavedData() {
        super(ID);
    }

    @Override
    public void load(CompoundTag p_76184_1_) {
        ListTag teams = p_76184_1_.getList("teams", Tag.TAG_COMPOUND);
        for(int i=0; i<teams.size(); i++){
            CompoundTag nbt = teams.getCompound(i);
            this.TeamList.add(CompanionTeam.deserializeNBT(nbt));
        }
        //this.SyncEntireTeamListClient();
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag nbt) {
        ListTag TeamList = new ListTag();
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
        this.SyncEntireTeamListClient();
        this.setDirty();
    }

    public void removeTeam(CompanionTeam team){
        this.TeamList.remove(team);
        this.SyncTeamEntriestoClient(team, SyncTeamListPacket.ACTION.REMOVE);
        this.setDirty();
    }

    public void removeTeambyUUID(UUID id){
        if(!this.TeamList.isEmpty()) {
            this.TeamList.removeIf(team -> team.getTeamUUID().equals(id));
        }
        this.SyncEntireTeamListClient();
        this.setDirty();
    }

    public Optional<CompanionTeam> getTeambyUUID(UUID uuid) {
        if(!this.TeamList.isEmpty()) {
            for (CompanionTeam team : this.TeamList) {
                if (team.getTeamUUID().equals(uuid)) {
                    return Optional.of(team);
                }
            }
        }
        return Optional.empty();
    }

    public void createteam(ServerPlayer owner){
        CompanionTeam newteam = new CompanionTeam(owner.getUUID(), owner.getDisplayName().getString());
        this.addorModifyTeam(newteam);
    }

    public void ChangeTeamName(UUID teamUUID, String name) {
        this.getTeambyUUID(teamUUID).ifPresent((team)->{
            team.setCustomName(new TextComponent(name));
            this.addorModifyTeam(team);
        });
    }

    public List<CompanionTeam> getPlayersTeam(Player player){
        return this.TeamList.stream().filter((team)->team.getOwnerUUID().equals(player.getUUID())).collect(Collectors.toList());
    }

    public void addMember(UUID teamUUID, UUID memberUUID){
        this.getTeambyUUID(teamUUID).ifPresent((team)->{
            if(team.addEntitytoTeam(memberUUID)){
                this.addorModifyTeam(team);
            }
        });
    }

    public void removeMember(UUID teamUUID, UUID memberUUID){
        this.getTeambyUUID(teamUUID).ifPresent((team)->{
            if(team.removeEntityfromTeam(memberUUID)){
                this.addorModifyTeam(team);
            }
        });
    }

    private void SyncTeamEntriestoClient(CompanionTeam team, SyncTeamListPacket.ACTION action){
        Main.NETWORK.send(PacketDistributor.ALL.noArg(), new SyncTeamListPacket(team.serializeNBT(), action));
    }

    private void SyncTeamEntriestoClient(CompanionTeam team){
        this.SyncTeamEntriestoClient(team, SyncTeamListPacket.ACTION.ADD);
    }

    private void SyncEntireTeamListClient(){
        CompoundTag compound = new CompoundTag();
        ListTag TeamList = new ListTag();
        for(CompanionTeam team: this.TeamList){
            TeamList.add(team.serializeNBT());
        }
        compound.put("teams", TeamList);
        Main.NETWORK.send(PacketDistributor.ALL.noArg(), new SyncTeamListPacket(compound, SyncTeamListPacket.ACTION.ADD_BATCH));
        this.setDirty();
    }

    public void SyncEntireTeamListtoPlayer(ServerPlayer player){
        CompoundTag compound = new CompoundTag();
        ListTag TeamList = new ListTag();
        for(CompanionTeam team: this.TeamList){
            TeamList.add(team.serializeNBT());
        }
        compound.put("teams", TeamList);
        Main.NETWORK.send(PacketDistributor.PLAYER.with(()->player), new SyncTeamListPacket(compound, SyncTeamListPacket.ACTION.ADD_BATCH));
        this.setDirty();
    }

    public static ProjectAzureWorldSavedData getSaveddata(ServerLevel world){
        ProjectAzureWorldSavedData storage = world.getDataStorage().get(ProjectAzureWorldSavedData::new, ID);
        if(storage == null){
            storage = new ProjectAzureWorldSavedData();
            world.getDataStorage().set(storage);
        }
        return storage;
    }
}
