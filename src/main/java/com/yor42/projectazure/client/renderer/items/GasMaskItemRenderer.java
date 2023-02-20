package com.yor42.projectazure.client.renderer.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.items.GeoGasMaskItemModel;
import com.yor42.projectazure.gameobject.items.GasMaskFilterItem;
import com.yor42.projectazure.gameobject.items.GasMaskItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class GasMaskItemRenderer extends GeoItemRenderer<GasMaskItem> {
    public GasMaskItemRenderer() {
        super(new GeoGasMaskItemModel());
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        String bonename = bone.getName();
        if(bonename.contains("filter")) {
            CompoundNBT compoundNBT = this.currentItemStack.getOrCreateTag();
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