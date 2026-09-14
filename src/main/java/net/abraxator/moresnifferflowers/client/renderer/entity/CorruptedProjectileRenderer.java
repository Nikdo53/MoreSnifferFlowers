package net.abraxator.moresnifferflowers.client.renderer.entity;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CorruptedProjectileRenderer extends CoolProjectileRenderer<CorruptedProjectile> {
    public static final Identifier TEXTURE = MoreSnifferFlowers.loc("textures/entity/corrupted_projectile.png");

    public CorruptedProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation(State entity) {
        return TEXTURE;
    }
}
