package net.abraxator.moresnifferflowers.client.renderer.entity;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.SaltProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class SaltProjectileRenderer extends CoolProjectileRenderer<SaltProjectile>{
    public static final Identifier TEXTURE_SALT = MoreSnifferFlowers.loc("textures/entity/salt_projectile.png");
    public static final Identifier TEXTURE_SOUR = MoreSnifferFlowers.loc("textures/entity/sour_projectile.png");

    public SaltProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation(SaltProjectile entity) {
        return entity.isCorrupted() ? TEXTURE_SOUR : TEXTURE_SALT;
    }
}
