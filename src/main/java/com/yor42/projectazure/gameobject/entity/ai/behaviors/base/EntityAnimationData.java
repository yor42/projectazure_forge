package com.yor42.projectazure.gameobject.entity.ai.behaviors.base;

import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;

public class EntityAnimationData {
    @Nullable
    private RegisterAI.Animations animation;
    private int cooldown;

    public EntityAnimationData(@Nullable RegisterAI.Animations animation, int cooldown){
        this.animation = animation;
        this.cooldown = cooldown;
    }

    public EntityAnimationData(){
        this.animation = null;
        this.cooldown = 0;
    }

    public void setAnimation(@Nullable RegisterAI.Animations animation, int cooldown) {
        this.animation = animation;
        this.cooldown = cooldown;
    }

    public void tick(){
        if(this.cooldown>0) {
            this.cooldown -= 1;
        }

        if(this.cooldown == 0){
            this.animation = null;
        }
    }

    public int getCooldown(RegisterAI.Animations animation){
        if(animation != this.animation){
            return 0;
        }
        else{
            return this.cooldown;
        }
    }

    public boolean isAnimating(){
        return this.animation != null;
    }

    public boolean isAnimating(RegisterAI.Animations animation){
        return this.getCooldown(animation)>0;
    }

    public void write(FriendlyByteBuf buffer){
        buffer.writeVarInt(this.animation == null? -1: this.animation.ordinal());
        buffer.writeInt(this.cooldown);
    }

    public static EntityAnimationData read(FriendlyByteBuf buffer){
        int index = buffer.readVarInt();
        int cooldown = buffer.readInt();
        return new EntityAnimationData(index == -1? null: RegisterAI.Animations.values()[index], cooldown);
    }

    public EntityAnimationData copy(){
        return new EntityAnimationData(this.animation, this.cooldown);
    }

}
