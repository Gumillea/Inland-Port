package com.gumillea.inlandport.core.api.record;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record IPEntityCompoundTagPacket(int entity, CompoundTag tag) implements CustomPacketPayload {

    public static final Type<IPEntityCompoundTagPacket> TYPE = new Type<>(IPUtil.loc(InlandPort.MODID, "compound_tag"));
    public static final StreamCodec<ByteBuf, IPEntityCompoundTagPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, IPEntityCompoundTagPacket::entity, ByteBufCodecs.COMPOUND_TAG, IPEntityCompoundTagPacket::tag, IPEntityCompoundTagPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(IPEntityCompoundTagPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> IPEntityTagPacketHandler.handle(packet));
    }

    @OnlyIn(Dist.CLIENT)
    static class IPEntityTagPacketHandler {
        public static void handle(IPEntityCompoundTagPacket packet) {
            Level level = Minecraft.getInstance().level;

            if (level != null && level.getEntity(packet.entity()) instanceof LivingEntity living) {
                CompoundTag compoundTag = living.getPersistentData().getCompound(InlandPort.MODID);

                compoundTag.merge(packet.tag());

                living.getPersistentData().put(InlandPort.MODID, compoundTag);
            }
        }
    }
}