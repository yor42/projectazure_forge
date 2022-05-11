package com.yor42.projectazure.gameobject.capability.playercapability;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompanionTeams {

    private final ArrayList<UUID> teammates;
    private final UUID OwnerUUID, TeamUUID;
    @Nullable
    private IFormattableTextComponent CustomName;

    private CompanionTeams(UUID TeamUUID, UUID OwnerUUID){
        this(new ArrayList<UUID>(), TeamUUID, OwnerUUID, null);
    }

    private CompanionTeams(ArrayList<UUID> entries, UUID TeamUUID, UUID OwnerUUID,@Nullable IFormattableTextComponent customname){
        this.teammates = entries;
        this.TeamUUID = TeamUUID;
        this.OwnerUUID = OwnerUUID;
        this.CustomName = customname;
    }
    public CompanionTeams(UUID OwnerUUID){
        this(UUID.randomUUID(), OwnerUUID);
    }

    public void setCustomName(IFormattableTextComponent customname){
        this.CustomName = customname;
    }
    public UUID getOwnerUUID(){
        return this.OwnerUUID;
    }

    public boolean addEntitytoTeam(AbstractEntityCompanion entity){
        for(int i=0; i<this.teammates.length; i++){
            if(this.teammates[i] == null){
                this.teammates[i] = entity.getUUID();
                return true;
            }
        }
        return false;
    }

    @Nullable
    public UUID getMemberUUID(int index){
        try{
            return this.teammates[index];
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public ITextComponent getDisplayName(){
        return this.CustomName != null? this.CustomName:new TranslationTextComponent("gui.teams.name");
    }

    public CompoundNBT serializeNBT(){
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("teamUUID", this.TeamUUID);
        nbt.putUUID("ownerUUID", this.OwnerUUID);
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

    public static CompanionTeams deserializeNBT(CompoundNBT compound){
        UUID TeamUUID = compound.getUUID("teamUUID");
        UUID OwnerUUID = compound.getUUID("ownerUUID");
        ListNBT list = compound.getList("entries", Constants.NBT.TAG_COMPOUND);
        List<UUID> entries = new ArrayList<UUID>();
        for(int i = 0; i < tagList.size(); i++){
            CompoundNBT entry = tagList.get(i);
            UUID TeammateUUID = entry.getUUID("UUID");
            entries.add(TeammateUUID);
        }
        IFormattableTextComponent customname;

        if (compound.contains("CustomName", 8)) {
            customname = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
        }

        return new CompanionTeams(entries, TeamUUID, OwnerUUID, customname);
    }
}
