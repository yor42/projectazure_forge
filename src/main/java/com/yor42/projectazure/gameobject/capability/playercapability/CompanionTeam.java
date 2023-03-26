package com.yor42.projectazure.gameobject.capability.playercapability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.nbt.Tag;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

public class CompanionTeam {

    private final ArrayList<UUID> teammates;
    private final UUID OwnerUUID, TeamUUID;
    private final String ownername;
    @Nullable
    private MutableComponent CustomName;

    private CompanionTeam(UUID TeamUUID, UUID OwnerUUID, String ownername){
        this(new ArrayList<>(), TeamUUID, OwnerUUID, ownername, null);
    }

    private CompanionTeam(ArrayList<UUID> entries, UUID TeamUUID, UUID OwnerUUID, String ownername, @Nullable MutableComponent customname){
        this.teammates = entries;
        this.TeamUUID = TeamUUID;
        this.OwnerUUID = OwnerUUID;
        this.CustomName = customname;
        this.ownername = ownername;
    }
    public CompanionTeam(UUID OwnerUUID, String ownername){
        this(UUID.randomUUID(), OwnerUUID, ownername);
    }

    public void setCustomName(MutableComponent customname){
        this.CustomName = customname;
    }
    public UUID getOwnerUUID(){
        return this.OwnerUUID;
    }

    public boolean addEntitytoTeam(UUID uuid){
        if(!this.isFull()){
            for(UUID id : this.teammates){
                if(id.equals(uuid)){
                    return false;
                }
            }
            this.teammates.add(uuid);
            return true;
        }
        return false;
    }

    public boolean removeEntityfromTeam(UUID uuid){
        if(this.teammates.contains(uuid)){
            this.teammates.remove(uuid);
            return true;
        }
        return false;
    }

    @Nullable
    public UUID getMemberUUID(int index){
        try{
            return this.teammates.get(index);
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public boolean isFull(){
        return this.teammates.size()>=5;
    }

    public ArrayList<UUID> getMembers(){
        return this.teammates;
    }

    public UUID getTeamUUID(){
        return this.TeamUUID;
    }

    public Component getDisplayName(){
        return this.CustomName != null? this.CustomName:new TranslatableComponent("gui.team.newname");
    }

    public CompoundTag serializeNBT(){
        CompoundTag nbt = new CompoundTag();
        nbt.putUUID("teamUUID", this.TeamUUID);
        nbt.putUUID("ownerUUID", this.OwnerUUID);
        nbt.putString("ownername", this.ownername);
        ListTag list = new ListTag();
        for(UUID id : this.teammates){
            CompoundTag entry = new CompoundTag();
            entry.putUUID("UUID", id);
            list.add(entry);
        }
        nbt.put("entries", list);
        if(this.CustomName != null) {
            nbt.putString("CustomName", Component.Serializer.toJson(this.CustomName));
        }
        return nbt;
    }

    public static CompanionTeam deserializeNBT(CompoundTag compound){
        UUID TeamUUID = compound.getUUID("teamUUID");
        UUID OwnerUUID = compound.getUUID("ownerUUID");
        String ownerName = compound.getString("ownerName");
        ListTag list = compound.getList("entries", Tag.TAG_COMPOUND);
        ArrayList<UUID> entries = new ArrayList<UUID>();
        for(int i = 0; i < list.size(); i++){
            CompoundTag entry = list.getCompound(i);
            UUID TeammateUUID = entry.getUUID("UUID");
            entries.add(TeammateUUID);
        }
        MutableComponent customname=null;

        if (compound.contains("CustomName", 8)) {
            customname = Component.Serializer.fromJson(compound.getString("CustomName"));
        }

        return new CompanionTeam(entries, TeamUUID, OwnerUUID, ownerName, customname);
    }
}
