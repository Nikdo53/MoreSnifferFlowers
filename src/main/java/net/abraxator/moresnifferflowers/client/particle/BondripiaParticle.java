package net.abraxator.moresnifferflowers.client.particle;

import net.abraxator.moresnifferflowers.init.MSFParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class BondripiaParticle extends SingleQuadParticle {
    protected BondripiaParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.preMoveUpdate();
        if (!this.removed) {
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.postMoveUpdate();
            if (!this.removed) {
                this.xd *= 0.98F;
                this.yd *= 0.98F;
                this.zd *= 0.98F;
            }
        }
    }

    protected void preMoveUpdate() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }

    }

    protected void postMoveUpdate() {
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    static class DripHangParticle extends BondripiaParticle {
        private final ParticleOptions fallingParticle;

        DripHangParticle(ClientLevel level, double x, double y, double z, ParticleOptions fallingParticle, TextureAtlasSprite sprite) {
            super(level, x, y, z, sprite);
            this.fallingParticle = fallingParticle;
            this.gravity *= 0.02F;
            this.lifetime = 40;
        }

        protected void preMoveUpdate() {
            if (this.lifetime-- <= 0) {
                this.remove();
                this.level.addParticle(this.fallingParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }

        }

        protected void postMoveUpdate() {
            this.xd *= 0.02D;
            this.yd *= 0.02D;
            this.zd *= 0.02D;
        }
    }
    static class FallingParticle extends BondripiaParticle {
        FallingParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
            this(level, x, y, z, (int)(64.0 / (Math.random() * 0.8 + 0.2)), sprite);
        }

        FallingParticle(ClientLevel level, double x, double y, double z, int lifetime, TextureAtlasSprite sprite) {
            super(level, x, y, z, sprite);
            this.lifetime = lifetime;
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
            }

        }
    }

    static class FallAndLandParticle extends BondripiaParticle.FallingParticle {
        protected final ParticleOptions landParticle;

        FallAndLandParticle(ClientLevel level, double x, double y, double z, ParticleOptions landParticle, TextureAtlasSprite sprite) {
            super(level, x, y, z, sprite);
            this.landParticle = landParticle;
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    static class BondripiaSplashParticle extends WaterDropParticle {
        BondripiaSplashParticle(ClientLevel level, double x, double y, double z, double xS, double yS, double zS, TextureAtlasSprite sprite) {
            super(level, x, y, z, sprite);
            this.gravity = 0.01F;
            this.yd = 0.05;
        }
    }

    public static class BondripiaDripProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BondripiaDripProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, RandomSource random) {
            BondripiaParticle bondripiaParticle = new BondripiaParticle.DripHangParticle(level, pX, pY, pZ, MSFParticles.BONDRIPIA_FALL.get(), spriteSet.get(random));
            bondripiaParticle.lifetime = (int)(16.0 / (Math.random() * 0.4 + 0.2));
            bondripiaParticle.gravity = 0.007F;
            return bondripiaParticle;
        }
    }

    public static class BondripiaFallProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BondripiaFallProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, RandomSource random) {
            BondripiaParticle bondripiaParticle = new BondripiaParticle.FallAndLandParticle(level, pX, pY, pZ, MSFParticles.BONDRIPIA_LAND.get(), spriteSet.get(random));
            bondripiaParticle.lifetime = (int)(16.0 / (Math.random() * 0.4 + 0.2));
            bondripiaParticle.gravity = 0.007F;
            return bondripiaParticle;
        }
    }

    public static class BondripiaLandProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BondripiaLandProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, RandomSource random) {
            var splashParticle = new BondripiaSplashParticle(level, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, spriteSet.get(random));
            return splashParticle;
        }
    }

    public static class AcidripiaDripProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public AcidripiaDripProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, RandomSource random) {
            BondripiaParticle bondripiaParticle = new BondripiaParticle.DripHangParticle(level, pX, pY, pZ, MSFParticles.ACIDRIPIA_FALL.get(), spriteSet.get(random));
            bondripiaParticle.lifetime = (int)(16.0 / (Math.random() * 0.4 + 0.2));
            bondripiaParticle.gravity = 0.007F;
            return bondripiaParticle;
        }

    }

    public static class AcidripiaFallProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public AcidripiaFallProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, RandomSource random) {
            BondripiaParticle bondripiaParticle = new BondripiaParticle.FallAndLandParticle(level, pX, pY, pZ, MSFParticles.ACIDRIPIA_LAND.get(), spriteSet.get(random));
            bondripiaParticle.lifetime = (int)(16.0 / (Math.random() * 0.4 + 0.2));
            bondripiaParticle.gravity = 0.007F;
            return bondripiaParticle;
        }

    }

    public static class AcidripiaLandProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public AcidripiaLandProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, RandomSource random) {
            var splashParticle = new BondripiaSplashParticle(level, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, spriteSet.get(random));
            return splashParticle;
        }

    }
}
