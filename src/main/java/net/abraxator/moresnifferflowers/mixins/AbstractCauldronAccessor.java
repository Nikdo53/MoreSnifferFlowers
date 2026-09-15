package net.abraxator.moresnifferflowers.mixins;


import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractCauldronBlock.class)
public interface AbstractCauldronAccessor {

    @Invoker(value = "canReceiveStalactiteDrip")
    boolean canReceiveStalactiteDripPublic(Fluid fluid);

}
