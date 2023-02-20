package com.yor42.projectazure.intermod.tconstruct.datagen;

import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.stats.ExtraMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

public class PAMaterialProvider extends AbstractMaterialDataProvider {

    public static final MaterialId RMA70_12 = makeMaterial("rma7012");
    //public static final MaterialId RMA70_24 = makeMaterial("rma7024");
    public static final MaterialId D32 = makeMaterial("d32");

    public PAMaterialProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addMaterials() {
        addMaterial(D32, 4,ORDER_GENERAL, false, 0xc2dbec);
        addMaterial(RMA70_12, 2,ORDER_GENERAL, false, 0xe6c580);

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
            this.addDefaultTraits(D32, TinkerModifiers.lightweight.get());
            this.addDefaultTraits(RMA70_12, TinkerModifiers.conducting.get());
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
            addMaterialStats(D32, new HeadMaterialStats(1285, 3,4,2.5F), HandleMaterialStats.DEFAULT.withDurability(1.5F), ExtraMaterialStats.DEFAULT);
            this.addMaterialStats(RMA70_12, new HeadMaterialStats(261, 5.5F, 2, 2.5F), HandleMaterialStats.DEFAULT.withDurability(0.9F), ExtraMaterialStats.DEFAULT);
        }

        @Override
        public String getName() {
            return "Project: Azure Material stats";
        }
    }
}
