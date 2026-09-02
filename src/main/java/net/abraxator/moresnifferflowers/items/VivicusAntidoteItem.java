package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.init.MSFAdvancementCritters;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class VivicusAntidoteItem extends Item {
    public VivicusAntidoteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var blockPos = context.getClickedPos();
        var relativePos = blockPos.relative(context.getClickedFace());
        var blockState = level.getBlockState(blockPos);
        var random = level.getRandom();
        var player = context.getPlayer();
        var particle = new DustParticleOptions(Vec3.fromRGB24(7118872).toVector3f(), 1);

        if(blockState.is(MSFBlocks.VIVICUS_SAPLING.get()) && !blockState.getValue(MSFStateProperties.VIVICUS_CURED)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(MSFStateProperties.VIVICUS_CURED, true));

            for(int i = 0; i <= 10; i++) {
                level.addParticle(particle, blockPos.getX() + random.nextDouble(), blockPos.getY() + random.nextDouble(), blockPos.getZ() + random.nextDouble(), 0, -0.3, 0);
            }
            
            if (player instanceof ServerPlayer serverPlayer) {
                MSFAdvancementCritters.USED_CURE.get().trigger(serverPlayer);
            }
            
            return InteractionResult.SUCCESS;
        }
        
        if(blockState.is(MSFBlocks.CORRUPTED_SLUDGE.get()) && blockState.getValue(MSFStateProperties.CURED).equals(false)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(MSFStateProperties.CURED, true));

            for(int i = 0; i <= 10; i++) {
                level.addParticle(particle, relativePos.getX() + random.nextDouble(), relativePos.getY() + random.nextDouble(), relativePos.getZ() + random.nextDouble(), 0, -0.3, 0);
            }
            return InteractionResult.SUCCESS;

        }

        if (blockState.is(MSFBlocks.CORRUPTED_GRASS_BLOCK.get())) {
            level.setBlockAndUpdate(blockPos, MSFBlocks.CURED_GRASS_BLOCK.get().defaultBlockState());

            for(int i = 0; i <= 10; i++) {
                level.addParticle(particle, relativePos.getX() + random.nextDouble(), relativePos.getY() + random.nextDouble(), relativePos.getZ() + random.nextDouble(), 0, -0.3, 0);
            }
            return InteractionResult.SUCCESS;
        }
        
        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context , List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(stack, context, pTooltip, pFlag);
        pTooltip.add(Component.translatableWithFallback("tooltip.wip", "WIP").withStyle(ChatFormatting.DARK_RED));
        pTooltip.add(Component.translatableWithFallback("tooltip.vivicus_antidote", "Cures Boblings and Corrupted Grass").withStyle(ChatFormatting.GOLD));
        ;
    }
}
