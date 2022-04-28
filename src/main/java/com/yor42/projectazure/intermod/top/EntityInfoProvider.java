package com.yor42.projectazure.intermod.top;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityInfoProvider implements IProbeInfoEntityProvider {

    private static final ResourceLocation HUNGER_ICONS = new ResourceLocation("textures/gui/icons.png");
    public static final ResourceLocation CUSTOMICONS = new ResourceLocation(Constants.MODID, "textures/gui/ship_inventory.png");

    @Override
    public String getID() {
        return Constants.MODID+":"+"EntityInfo";
    }

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player playerEntity, Level world, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        if(entity instanceof AbstractEntityCompanion){
            AbstractEntityCompanion companion = (AbstractEntityCompanion) entity;
            ItemStack gunstack = companion.getGunStack();
            int hunger = companion.getFoodStats().getFoodLevel();
            iProbeInfo.horizontal().icon(HUNGER_ICONS, 16,35,9,9).text(new TextComponent(hunger+"/20"));
            double morale = companion.getMorale();
            double affection = companion.getAffection();
            int textureY = 13;

            int textureX;
            ChatFormatting format;
            if(morale>=120.0D){
                textureX = 224;
                format = ChatFormatting.AQUA;
            }
            else if(morale>=70){
                textureX = 212;
                format = ChatFormatting.GREEN;
            }
            else if(morale>=30){
                textureX = 200;
                format = ChatFormatting.YELLOW;
            }
            else if(morale>10){
                textureX = 188;
                format = ChatFormatting.RED;;
            }else{
                textureX = 176;
                format = ChatFormatting.DARK_RED;
            }
            iProbeInfo.horizontal().icon(CUSTOMICONS, textureX, textureY, 12, 12).text(new TextComponent(((int)morale)+"/150").withStyle(format));
            BaseComponent Leveltext = new TranslatableComponent("top.companion_level.message",companion.getCompanionLevel());
            if(probeMode == ProbeMode.EXTENDED){
                Leveltext.append(new TextComponent(" ["+ ((int)Math.floor(companion.getExp()))+"/"+ ((int)Math.floor(companion.getMaxExp())) +"]"));
            }
            iProbeInfo.horizontal().text(Leveltext);
            if(probeMode == ProbeMode.EXTENDED){
                textureY = 1;
                int color;
                if(affection>=100.0D){
                    if(companion.isOathed()) {
                        color = 16702964;
                        textureX = 236;
                    }
                    else{
                        color = 15964118;
                        textureX = 224;
                    }
                }
                else if(affection>80){
                    color = 8702971;
                    textureX = 212;
                }
                else if(affection>60){
                    color = 10021373;
                    textureX = 200;
                }
                else if(affection>30){
                    color = 10021373;
                    textureX = 188;
                }
                else{
                    color = 7829367;
                    textureX = 176;
                }
                iProbeInfo.horizontal().icon(CUSTOMICONS, textureX, textureY, 12, 12).text(new TextComponent(((int)morale)+"/150").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            }
            if(companion.getLimitBreakLv()>0){
                iProbeInfo.horizontal().text(new TranslatableComponent("top.companion_limit_break.message",companion.getLimitBreakLv()));
            }
            if(companion.getOwner() != null) {
                iProbeInfo.horizontal().text(new TranslatableComponent("top.companion_owner.message").append(companion.getOwner().getName()).withStyle(ChatFormatting.YELLOW));
            }
            if(gunstack.getItem() instanceof ItemGunBase){
                int RemainingAmmo = ItemStackUtils.getRemainingAmmo(gunstack);
                int maxAmmo = ((ItemGunBase)gunstack.getItem()).getMaxAmmo();
                iProbeInfo.item(gunstack).text(new TextComponent(RemainingAmmo+"/"+maxAmmo));
            }

        }
    }


}
