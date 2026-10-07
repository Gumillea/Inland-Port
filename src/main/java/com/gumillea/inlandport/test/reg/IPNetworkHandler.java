package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.api.record.IPEntityCompoundTagPacket;
import com.gumillea.inlandport.core.api.record.IPEntityTagPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class IPNetworkHandler {

    public static void register(IEventBus bus) {
        bus.addListener(IPNetworkHandler::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar(InlandPort.MODID).playToClient(IPEntityCompoundTagPacket.TYPE, IPEntityCompoundTagPacket.STREAM_CODEC, IPEntityCompoundTagPacket::handle);
        event.registrar(InlandPort.MODID).playToClient(IPEntityTagPacket.TYPE, IPEntityTagPacket.STREAM_CODEC, IPEntityTagPacket::handle);
    }

    public static void sendEntityCompoundTagPacket(LivingEntity entity, CompoundTag tag) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new IPEntityCompoundTagPacket(entity.getId(), tag));
    }

    public static void sendEntityTagPacket(LivingEntity entity, String tag) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new IPEntityTagPacket(entity.getId(), tag));
    }
}
