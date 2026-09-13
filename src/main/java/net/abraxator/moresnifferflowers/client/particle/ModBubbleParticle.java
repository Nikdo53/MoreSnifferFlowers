package net.abraxator.moresnifferflowers.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.nikdo53.tinymultiblocklib.event.OnBlockPreviewEvent;

public class ModBubbleParticle extends SingleQuadParticle {
    ModBubbleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.setSize(0.02F, 0.02F);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
        this.xd = xSpeed * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.yd = ySpeed * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.zd = zSpeed * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.yd += 0.002D;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= (double)0.85F;
            this.yd *= (double)0.85F;
            this.zd *= (double)0.85F;
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource randomSource) {
            ModBubbleParticle bubbleparticle = new ModBubbleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite.get(randomSource));
            return bubbleparticle;
        }
    }
}
