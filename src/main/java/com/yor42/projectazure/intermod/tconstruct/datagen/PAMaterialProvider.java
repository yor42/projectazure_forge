package com.yor42.projectazure.intermod.tconstruct.datagen;

import com.yor42.projectazure.intermod.tconstruct.TinkersRegistry;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Tiers;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.stats.ExtraMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

import javax.annotation.Nonnull;

public class PAMaterialProvider extends AbstractMaterialDataProvider {

    public static final MaterialId RMA70_12 = makeMaterial("rma7012");
    public static final MaterialId RMA70_24 = makeMaterial("rma7024");
    public static final MaterialId D32 = makeMaterial("d32");
    public static final MaterialId ORIROCK = makeMaterial("orirock");

    public PAMaterialProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addMaterials() {
        addMaterial(D32, 4,ORDER_GENERAL, false);
        addMaterial(RMA70_12, 2,ORDER_GENERAL, false);
        addMaterial(RMA70_24, 3,ORDER_GENERAL, false);
        addMaterial(ORIROCK, 1,ORDER_GENERAL, true);
    }

    private static MaterialId makeMaterial(String name){
        return new MaterialId(ResourceUtils.ModResourceLocation(name));
    }

    @Override
    public String getName() {
        return "Project: Azure Materials";
    }

    public static class PAMaterialTraits extends AbstractMaterialTraitDataProvider {

        public PAMaterialTraits(DataGenerator gen, AbstractMaterialDataProvider materials) {
            super(gen, materials);
        }

        @Override
        protected void addMaterialTraits() {
            this.addDefaultTraits(D32, TinkerModifiers.lightspeed, TinkerModifiers.severing);
            this.addDefaultTraits(ORIROCK, TinkersRegistry.ABSORBTION, TinkerModifiers.stonebound);
            this.addDefaultTraits(RMA70_12, TinkerModifiers.conducting);
            this.addDefaultTraits(RMA70_24, TinkerModifiers.piercing, TinkerModifiers.haste);
        }

        @Override
        public String getName() {
            return "Project: Azure Material Traits";
        }

    }

    public static class PAMaterialStats extends AbstractMaterialStatsDataProvider{

        public PAMaterialStats(DataGenerator gen, AbstractMaterialDataProvider materials) {
            super(gen, materials);
        }

        @Override
        protected void addMaterialStats() {
            this.addMaterialStats(D32, new HeadMaterialStats(1285, 3, Tiers.NETHERITE,2.5F), HandleMaterialStats.DEFAULT.withDurability(1.5F), ExtraMaterialStats.DEFAULT);
            this.addMaterialStats(RMA70_12, new HeadMaterialStats(261, 5.5F, Tiers.IRON, 2.5F), HandleMaterialStats.DEFAULT.withDurability(0.9F), ExtraMaterialStats.DEFAULT);
            this.addMaterialStats(RMA70_24, new HeadMaterialStats(592, 4F, Tiers.DIAMOND, 4F), HandleMaterialStats.DEFAULT.withDurability(0.7F), ExtraMaterialStats.DEFAULT);
            this.addMaterialStats(ORIROCK, new HeadMaterialStats(115, 5.0F, Tiers.STONE, 1.1F), HandleMaterialStats.DEFAULT.withDurability(0.8F), ExtraMaterialStats.DEFAULT);

        }

        @Nonnull
        @Override
        public String getName() {
            return "Project: Azure Material stats";
        }
    }
}
