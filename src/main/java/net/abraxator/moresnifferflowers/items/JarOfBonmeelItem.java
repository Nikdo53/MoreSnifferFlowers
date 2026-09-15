package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.blocks.Bonmeelable;
import net.abraxator.moresnifferflowers.blocks.GiantCropBlock;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.function.Consumer;

public class JarOfBonmeelItem extends Item {
    public JarOfBonmeelItem(Properties properties) {
        super(properties);
    }

    public InteractionResult highPriorityUseOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        Player player = context.getPlayer();

        if(blockState.is(MSFTags.BlockTags.BONMEELABLE)) {
            Block block = GiantCropBlock.getCropMap().get(blockState.getBlock()).getA();
            if(block instanceof Bonmeelable bonmeelable && player != null && bonmeelable.canBonmeel(blockPos,blockState,level,player)) {
                bonmeelable.performBonmeel(blockPos, blockState, level, player);
                if (!player.isCreative()) player.setItemInHand(context.getHand(), ItemUtils.createFilledResult(player.getItemInHand(context.getHand()), player, new ItemStack(Items.GLASS_BOTTLE)));

                return InteractionResult.SUCCESS;

            }

        }

        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        Component component = Component.translatableWithFallback("tooltip.jar_of_bonmeel.usage", "Can be applied to a 3x3 grid of the following crops: carrot, potato, wheat, beetroot and nether wart").withStyle(ChatFormatting.GOLD);
        var usageComponents = Arrays.stream(component.getString().split("\n", -1))
                .filter(s -> !s.isEmpty())
                .map(String::trim);

        usageComponents.forEach(s -> builder.accept(Component.literal(s).withStyle(ChatFormatting.GOLD)));

    }
}
