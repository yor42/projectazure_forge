package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipelogics;

import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.capability.IO;
import com.lowdragmc.multiblocked.api.kubejs.events.RecipeFinishEvent;
import com.lowdragmc.multiblocked.api.kubejs.events.SetupRecipeEvent;
import com.lowdragmc.multiblocked.api.recipe.Recipe;
import com.lowdragmc.multiblocked.api.recipe.RecipeLogic;
import com.lowdragmc.multiblocked.api.tile.ControllerTileEntity;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipes.RiftwayRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import java.util.UUID;

public class RiftwayRecipeLogic extends RecipeLogic {

    @Nullable
    public UUID userID;
    public boolean canStart = false;

    public RiftwayRecipeLogic(ControllerTileEntity controller) {
        super(controller);
    }

    public void setupRecipe(Recipe recipe) {
        if(this.canStart && this.userID != null) {
            if (handleFuelRecipe()) {
                recipe.preWorking(this.controller);
                if (recipe.handleRecipeIO(IO.IN, this.controller)) {
                    this.canStart = false;
                    lastRecipe = recipe;
                    setStatus(Status.WORKING);
                    this.progress = 0;
                    duration = recipe.duration;
                    markDirty();
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if(timer % 5 == 0 && canStart){
            canStart = false;
        }
    }

    @Override
    public void onRecipeFinish() {
        boolean isRiftway = lastRecipe instanceof RiftwayRecipes;
        if(isRiftway){
            ((RiftwayRecipes) lastRecipe).setPlayerUUID(this.userID);
        }
        lastRecipe.postWorking(this.controller);

        lastRecipe.handleRecipeIO(IO.OUT, this.controller);

        if(isRiftway){
            ((RiftwayRecipes) lastRecipe).setPlayerUUID(null);
        }
        this.userID = null;

        setStatus(Status.IDLE);
        progress = 0;
        duration = 0;
        markDirty();
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        if(compound.contains("userid")) {
            this.userID = compound.getUUID("userid");
        }
        this.canStart = compound.getBoolean("canstart");
        super.readFromNBT(compound);
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT compound) {
        if(this.userID != null) {
            compound.putUUID("userid", this.userID);
        }
        compound.putBoolean("canstart", this.canStart);
        return super.writeToNBT(compound);
    }

    public void startProcess(UUID player){
        this.userID = player;
        this.canStart = true;
    }
}
