package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.entity.ai.behaviors.base.EntityAnimationData;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.function.Supplier;

public class RegisterDataSerializers {
    public static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.DATA_SERIALIZERS, Constants.MODID);

    public static final RegistryObject<DataSerializerEntry> ENTITY_ANIMATIONDATA = register("animationdata", ()->new EntityDataSerializer<Optional<EntityAnimationData>>() {
        @Override
        public void write(FriendlyByteBuf pBuffer, Optional<EntityAnimationData> pValue) {
            pBuffer.writeBoolean(pValue.isPresent());
            pValue.ifPresent((val)-> val.write(pBuffer));
        }

        @Override
        public Optional<EntityAnimationData> read(FriendlyByteBuf pBuffer) {
            boolean exists = pBuffer.readBoolean();

            if(!exists){
                return Optional.empty();
            }
            return Optional.of(EntityAnimationData.read(pBuffer));

        }

        @Override
        public Optional<EntityAnimationData> copy(Optional<EntityAnimationData> pValue) {
            return Optional.empty();
        }
    });

    private static RegistryObject<DataSerializerEntry> register(String name, Supplier<? extends EntityDataSerializer<?>> sup){
        return DATA_SERIALIZER.register(name, ()->new DataSerializerEntry(sup.get()));
    }

    public static void register(IEventBus bus){
        DATA_SERIALIZER.register(bus);
    }

}
