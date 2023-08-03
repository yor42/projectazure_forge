package com.yor42.projectazure.client.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.client.model.armor.GeoGasMaskModel;
import com.yor42.projectazure.gameobject.items.tools.GasMaskFilterItem;
import com.yor42.projectazure.gameobject.items.tools.GasMaskItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

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
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        String bonename = bone.getName();
        if(bonename.contains("filter")) {
            CompoundTag compoundNBT = this.itemStack.getOrCreateTag();
            int index = Integer.parseInt(bonename.substring(bonename.length()-1))-1;
            ListTag filters = compoundNBT.getList("filters", Tag.TAG_COMPOUND);
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
