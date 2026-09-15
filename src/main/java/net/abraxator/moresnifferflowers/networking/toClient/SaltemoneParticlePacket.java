package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.init.MSFParticles;
import net.abraxator.moresnifferflowers.networking.MSFPacket;
import net.abraxator.moresnifferflowers.networking.MSFToClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3fc;

public record SaltemoneParticlePacket(Vector3fc pos) implements MSFToClientPacket {
    public static final CustomPacketPayload.Type<SaltemoneParticlePacket> TYPE = MSFPacket.makeType("saltemone_particle", SaltemoneParticlePacket.class);
    public static final StreamCodec<ByteBuf, SaltemoneParticlePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, SaltemoneParticlePacket::pos,
            SaltemoneParticlePacket::new
    );

    @Override
    public void handleClientPacket(Player player, Level level) {
        Proxy.handle(player, level, this);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    interface Proxy{
        static void handle(Player player, Level level, SaltemoneParticlePacket packet) {
            RandomSource random = level.getRandom();

            Vector3fc pos = packet.pos();
            for (int i = 0; i < 5; i++) {
                Particle particle = Minecraft.getInstance().particleEngine.createParticle(MSFParticles.BUBBLE.get(),
                        pos.x() + random.nextDouble() - 0.5, pos.y() + random.nextDouble() - 0.5, pos.z() + random.nextDouble() - 0.5, (random.nextDouble()  - 0.5 )/2, (random.nextDouble()  - 0.5 )*2, (random.nextDouble()  - 0.5 )/2);
                if (particle != null) {
                    particle.scale(0.5F + random.nextFloat());
                    particle.setLifetime(random.nextIntBetweenInclusive(15, 25));
                }
            }
            ;
        }
    }

}
