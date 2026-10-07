package com.gumillea.inlandport.core.api.record;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record IPEntityTagPacket(int entity, String tag) implements CustomPacketPayload {

    public static final Type<IPEntityTagPacket> TYPE = new Type<>(IPUtil.loc(InlandPort.MODID, "tag"));
    public static final StreamCodec<ByteBuf, IPEntityTagPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, IPEntityTagPacket::entity, ByteBufCodecs.STRING_UTF8, IPEntityTagPacket::tag, IPEntityTagPacket::new);

    public static String tag(String modId, String name) {
        return InlandPort.MODID + "{" + modId + ":" + name + "}";
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(IPEntityTagPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> IPEntityTagPacketHandler.handle(packet));
    }

    @OnlyIn(Dist.CLIENT)
    static class IPEntityTagPacketHandler {
        public static void handle(IPEntityTagPacket packet) {
            Level level = Minecraft.getInstance().level;

            if (level != null && level.getEntity(packet.entity()) instanceof LivingEntity living) {
                living.addTag(packet.tag());
            }
        }
    }
}