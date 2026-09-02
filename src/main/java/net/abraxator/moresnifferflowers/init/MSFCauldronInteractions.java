package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.blockentities.ModCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;
import java.util.function.Predicate;

public interface MSFCauldronInteractions {
    CauldronInteraction.InteractionMap BONMEEL = CauldronInteraction.newInteractionMap("bonmeel");
    CauldronInteraction.InteractionMap ACID = CauldronInteraction.newInteractionMap("acid");
    CauldronInteraction FILL_JAR_OF_BONMEEL = (state, level, pos, player, hand, stack) ->
            emptyBottle(level, pos,  player, hand, stack, MSFBlocks.BONMEEL_FILLED_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
    CauldronInteraction FILL_JAR_OF_ACID = (state, level, pos, player, hand, stack) ->
            emptyBottle(level, pos,  player, hand, stack, MSFBlocks.ACID_FILLED_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
    CauldronInteraction EMPTY_JAR_OF_BONMEEL = (state, level, pos, player, hand, stack) ->
            fillBucket(state, level, pos, player, hand, stack, MSFItems.JAR_OF_BONMEEL.get().getDefaultInstance(), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BOTTLE_FILL);
    CauldronInteraction EMPTY_JAR_OF_ACID = (state, level, pos, player, hand, stack) ->
            fillBucket(state, level, pos, player, hand, stack, MSFItems.JAR_OF_ACID.get().getDefaultInstance(), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BOTTLE_FILL);


    static InteractionResult emptyBottle(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack, BlockState state) {
        if (level.getBlockState(pos).getValue(LayeredCauldronBlock.LEVEL) < 3) {
            if (!level.isClientSide()) {
                Item item = filledStack.getItem();
                if (!player.isCreative()) player.setItemInHand(hand, ItemUtils.createFilledResult(filledStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.FILL_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, state);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }


    static InteractionResult fillBucket(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack emptyStack, ItemStack filledStack, Predicate<BlockState> statePredicate, SoundEvent fillSound) {
        if (!statePredicate.test(blockState)) {
            return InteractionResult.PASS;
        } else {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof ModCauldronBlockEntity entity) {
                Item item = emptyStack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(emptyStack, player, filledStack));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, entity.originalCauldron);
                level.playSound(null, pos, fillSound, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    public static void bootstrap() {
        Map<Item, CauldronInteraction> bonmeel = BONMEEL.map();
        bonmeel.put(Items.GLASS_BOTTLE, EMPTY_JAR_OF_BONMEEL);
        bonmeel.put(MSFItems.JAR_OF_BONMEEL.asItem(), FILL_JAR_OF_BONMEEL);

        Map<Item, CauldronInteraction> acid = ACID.map();
        acid.put(Items.GLASS_BOTTLE, EMPTY_JAR_OF_ACID);
        acid.put(MSFItems.JAR_OF_ACID.get(), FILL_JAR_OF_ACID);
    }
}
