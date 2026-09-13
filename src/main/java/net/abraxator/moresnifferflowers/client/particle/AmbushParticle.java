package net.abraxator.moresnifferflowers.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class AmbushParticle extends SimpleAnimatedParticle {
    public AmbushParticle(ClientLevel level, double pX, double pY, double pZ, SpriteSet pSprites) {
        super(level, pX, pY, pZ, pSprites, -0.125F);
        this.scale(0.95F);
        this.setLifetime(30);
        this.setSpriteFromAge(pSprites);

    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new AmbushParticle(level, x, y, z, sprites);
        }
    }
}
