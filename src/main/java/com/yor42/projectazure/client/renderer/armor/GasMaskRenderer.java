package com.yor42.projectazure.client.renderer.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.armor.GeoGasMaskModel;
import com.yor42.projectazure.gameobject.items.GasMaskFilterItem;
import com.yor42.projectazure.gameobject.items.GasMaskItem;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.*;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class GasMaskRenderer extends GeoArmorRenderer<GasMaskItem> {
    public GasMaskRenderer() {
        super(new GeoGasMaskModel());
        this.bodyBone = null;
        this.leftArmBone = null;
        this.rightArmBone = null;
        this.leftLegBone = null;
        this.rightLegBone = null;
        this.leftBootBone = null;
        this.rightBootBone = null;
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        String bonename = bone.getName();
        if(bonename.contains("filter")) {
            CompoundNBT compoundNBT = this.itemStack.getOrCreateTag();
            int index = Integer.parseInt(bonename.substring(bonename.length()-1))-1;
            ListNBT filters = compoundNBT.getList("filters", Constants.NBT.TAG_COMPOUND);
            if(index>=filters.size()){
                bone.setHidden(true);
            }
            else {
                bone.setHidden(!(ItemStack.of(filters.getCompound(index)).getItem() instanceof GasMaskFilterItem));
            }
        }
        else if(bonename.equals("transparent_lens")){
            alpha=0.25F;
        }


        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
