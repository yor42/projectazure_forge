package com.yor42.projectazure.gameobject.capability.playercapability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

public class CompanionTeam {

    private final ArrayList<UUID> teammates;
    private final UUID OwnerUUID, TeamUUID;
    private final String ownername;
    @Nullable
    private IFormattableTextComponent CustomName;

    private CompanionTeam(UUID TeamUUID, UUID OwnerUUID, String ownername){
        this(new ArrayList<>(), TeamUUID, OwnerUUID, ownername, null);
    }

    private CompanionTeam(ArrayList<UUID> entries, UUID TeamUUID, UUID OwnerUUID, String ownername, @Nullable IFormattableTextComponent customname){
        this.teammates = entries;
        this.TeamUUID = TeamUUID;
        this.OwnerUUID = OwnerUUID;
        this.CustomName = customname;
        this.ownername = ownername;
    }
    public CompanionTeam(UUID OwnerUUID, String ownername){
        this(UUID.randomUUID(), OwnerUUID, ownername);
    }

    public void setCustomName(IFormattableTextComponent customname){
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

    public ITextComponent getDisplayName(){
        return this.CustomName != null? this.CustomName:new TranslationTextComponent("gui.team.newname");
    }

    public CompoundNBT serializeNBT(){
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("teamUUID", this.TeamUUID);
        nbt.putUUID("ownerUUID", this.OwnerUUID);
        nbt.putString("ownername", this.ownername);
        ListNBT list = new ListNBT();
        for(UUID id : this.teammates){
            CompoundNBT entry = new CompoundNBT();
            entry.putUUID("UUID", id);
            list.add(entry);
        }
        nbt.put("entries", list);
        if(this.CustomName != null) {
            nbt.putString("CustomName", ITextComponent.Serializer.toJson(this.CustomName));
        }
        return nbt;
    }

    public static CompanionTeam deserializeNBT(CompoundNBT compound){
        UUID TeamUUID = compound.getUUID("teamUUID");
        UUID OwnerUUID = compound.getUUID("ownerUUID");
        String ownerName = compound.getString("ownerName");
        ListNBT list = compound.getList("entries", Constants.NBT.TAG_COMPOUND);
        ArrayList<UUID> entries = new ArrayList<UUID>();
        for(int i = 0; i < list.size(); i++){
            CompoundNBT entry = list.getCompound(i);
            UUID TeammateUUID = entry.getUUID("UUID");
            entries.add(TeammateUUID);
        }
        IFormattableTextComponent customname=null;

        if (compound.contains("CustomName", 8)) {
            customname = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
        }

        return new CompanionTeam(entries, TeamUUID, OwnerUUID, ownerName, customname);
    }
}
