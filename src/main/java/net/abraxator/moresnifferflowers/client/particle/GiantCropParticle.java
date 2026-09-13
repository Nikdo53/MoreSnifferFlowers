package net.abraxator.moresnifferflowers.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class GiantCropParticle extends SingleQuadParticle {
    protected GiantCropParticle(ClientLevel level, double pX, double pY, double pZ, TextureAtlasSprite sprite) {
        super(level, pX, pY, pZ, sprite);
        this.scale(5);
        this.setLifetime(25);
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, RandomSource randomSource) {
            GiantCropParticle giantCropParticle = new GiantCropParticle(level, pX, pY, pZ, spriteSet.get(randomSource));
            return giantCropParticle;
        }
    }
}
