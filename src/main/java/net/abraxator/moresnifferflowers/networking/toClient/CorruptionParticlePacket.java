package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.networking.MSFPacket;
import net.abraxator.moresnifferflowers.networking.MSFToClientPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record CorruptionParticlePacket (BlockPos pos, boolean isPositive, boolean isFlower) implements MSFToClientPacket {
    public static final CustomPacketPayload.Type<CorruptionParticlePacket> TYPE = MSFPacket.makeType("corruption_particle", CorruptionParticlePacket.class);
    public static final StreamCodec<ByteBuf, CorruptionParticlePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, CorruptionParticlePacket::pos,
            ByteBufCodecs.BOOL, CorruptionParticlePacket::isPositive,
            ByteBufCodecs.BOOL, CorruptionParticlePacket::isFlower,
            CorruptionParticlePacket::new
    );

    @Override
    public void handleClientPacket(Player player, Level level) {
        RandomSource random = level.getRandom();
        BlockState state = level.getBlockState(pos);

        Vec3 vec = pos.getCenter();

        if(isFlower){
            Vec3 offset = state.getOffset(pos).add(vec);
            vec.add(offset);
            for (int i = 0; i < 10; i++){
                double inaccuracy = 0.7;
                double xOff = (random.nextFloat() - 0.5f) * inaccuracy;
                double zOff = (random.nextFloat() - 0.5f) * inaccuracy;
                double yOff = random.nextFloat() / 2 ;

                level.addParticle(ParticleTypes.HAPPY_VILLAGER, offset.x + xOff, offset.y + yOff, offset.z + zOff, 0, 0.2, 0);

            }

        } else {
            for (int i = 0; i < 5; i++) {
                double xOff = random.nextFloat() - 0.5f;
                double zOff = random.nextFloat() - 0.5f;
                double yOff = random.nextFloat() / 3 + 0.5f ;

                double slowDown = 3;

                ParticleOptions particle = isPositive ? ParticleTypes.HAPPY_VILLAGER : new DustParticleOptions(0xff6b3e7a, random.nextFloat() /2 + 0.5f);
                level.addParticle(particle, vec.x + xOff, vec.y + yOff, vec.z + zOff, xOff / slowDown, 0.2, zOff / slowDown);

            }
        }

    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
