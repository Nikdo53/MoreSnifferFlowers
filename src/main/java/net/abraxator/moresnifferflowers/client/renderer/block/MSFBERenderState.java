package net.abraxator.moresnifferflowers.client.renderer.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.state.BlockState;

public class MSFBERenderState extends BlockEntityRenderState {
    public float partialTicks;

    public BlockState getBlockState() {
        return blockState;
    }
}
