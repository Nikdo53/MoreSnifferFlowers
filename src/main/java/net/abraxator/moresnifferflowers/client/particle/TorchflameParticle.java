package net.abraxator.moresnifferflowers.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;

public class TorchflameParticle extends SimpleAnimatedParticle {
    protected TorchflameParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float gravity) {
        super(level, x, y, z, sprites, gravity);
        this.scale(2.0F);
        this.setLifetime(30);
        this.setSpriteFromAge(sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, RandomSource randomSource) {
            return new TorchflameParticle(level, pX, pY, pZ, sprites, -0.3F);
        }
    }
}
