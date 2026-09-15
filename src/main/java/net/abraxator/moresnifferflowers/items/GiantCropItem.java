package net.abraxator.moresnifferflowers.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.blockentities.IMultiBlockEntity;

import java.util.function.Consumer;

import static net.abraxator.moresnifferflowers.blocks.GiantCropBlock.WATERLOGGED;

public class GiantCropItem extends BlockItem {
    public GiantCropItem(Block pBlock, Properties properties) {
        super(pBlock, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        var level = context.getLevel();
        var clickPos = context.getClickedPos().relative(context.getClickedFace(), 1);
        var aabb = AABB.ofSize(clickPos.getCenter(), 2, 2, 2);
        IMultiBlock multiBlock = (IMultiBlock) state.getBlock();

        multiBlock.getMultiblockShape(level, clickPos, state).getGlobalPositions().forEach(pos -> {

            boolean isWaterLogged = context.getLevel().getFluidState(pos).getType() == Fluids.WATER;

            level.setBlockAndUpdate(pos, this.getBlock().defaultBlockState()
                    .setValue(AbstractMultiBlock.CENTER, pos.equals(clickPos))
                    .setValue(WATERLOGGED, isWaterLogged));

            if (level.getBlockEntity(pos) instanceof IMultiBlockEntity entity) {
                entity.setCenter(clickPos);
            }
        });

        return true;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        var pos = context.getClickedPos();
        var level = context.getLevel();
        IMultiBlock multiBlock = (IMultiBlock) state.getBlock();
        var aabb = AABB.ofSize(context.getClickedPos().relative(context.getClickedFace(), 1).getCenter(), 2, 2, 2);

        return multiBlock.getMultiblockShape(level, pos.relative(context.getClickedFace()), state).getGlobalPositions().stream().allMatch(blockPos -> level.getBlockState(blockPos).canBeReplaced());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.literal("CREATIVE ONLY").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
    }
}
