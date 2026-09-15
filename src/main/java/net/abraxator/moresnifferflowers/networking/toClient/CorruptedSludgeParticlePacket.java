package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.networking.MSFPacket;
import net.abraxator.moresnifferflowers.networking.MSFToClientPacket;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record CorruptedSludgeParticlePacket(Vector3fc start, Vector3fc target, Vector3fc direction) implements MSFToClientPacket {
    public static final CustomPacketPayload.Type<CorruptedSludgeParticlePacket> TYPE = MSFPacket.makeType("send_sludge_particle", CorruptedSludgeParticlePacket.class);
    public static final StreamCodec<ByteBuf, CorruptedSludgeParticlePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, CorruptedSludgeParticlePacket::start,
            ByteBufCodecs.VECTOR3F, CorruptedSludgeParticlePacket::target,
            ByteBufCodecs.VECTOR3F, CorruptedSludgeParticlePacket::direction,
            CorruptedSludgeParticlePacket::new
    );

    @Override
    public void handleClientPacket(Player player, Level level) {
        float distance = start.distance(target);

        for (int i = 0; i < 15; i++) {
            double progress = (double) i / 15;
            Vector3f pos = new Vector3f(start).add(new Vector3f(direction).mul((float) (distance * progress)));
            level.addParticle(new DustParticleOptions(0x0443248, 1.0F), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
        }
    }



    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
