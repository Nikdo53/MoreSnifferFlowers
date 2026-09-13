package net.abraxator.moresnifferflowers.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public enum PatternspriaMode implements StringRepresentable {
    SINGLE("single", PatternspriaMode.DyespriaSelector::single, ChatFormatting.WHITE),
    COLUMN("column", PatternspriaMode.DyespriaSelector::column, ChatFormatting.BLUE),
    ROW("row", PatternspriaMode.DyespriaSelector::row, ChatFormatting.GREEN),
    SHAPE("shape", PatternspriaMode.DyespriaSelector::shape, ChatFormatting.RED);

    public static final Codec<PatternspriaMode> CODEC = StringRepresentable.fromEnum(PatternspriaMode::values);
    public static final IntFunction<PatternspriaMode> BY_ID = ByIdMap.continuous(PatternspriaMode::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, PatternspriaMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

    private final String name;
    private final Function<PatternspriaMode.DyespriaSelector, Set<BlockPos>> selector;
    private final ChatFormatting textColor;

    PatternspriaMode(String name, Function<PatternspriaMode.DyespriaSelector, Set<BlockPos>> selector, ChatFormatting textColor) {
        this.name = name;
        this.selector = selector;
        this.textColor = textColor;
    }

    public static PatternspriaMode byIndex(int index) {
        return BY_ID.apply(index);
    }

    public static PatternspriaMode shift(PatternspriaMode current, int amount) {
        int size = values().length;
        int currentIndex = current.ordinal();
        int newIndex = (currentIndex + amount) % size;

        return byIndex(newIndex);
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public Function<PatternspriaMode.DyespriaSelector, Set<BlockPos>> getSelector() {
        return selector;
    }

    public ChatFormatting getTextColor() {
        return textColor;
    }

    public static record DyespriaSelector(BlockPos originalPos, Level level, Direction clickedDir) {
        public Set<BlockPos> single() {
            return Set.of(originalPos());
        }

        public Set<BlockPos> column() {
            Set<BlockPos> ret = new HashSet<>();
            ret.add(originalPos);
            var posUp = originalPos.above().mutable();
            var posDown = originalPos.below().mutable();
            while (matchBlock(posUp)  && ret.size() <= 64) {
                ret.add(posUp.immutable());
                posUp.move(Direction.UP);
            }

            while(matchBlock(posDown)  && ret.size() <= 64) {
                ret.add(posDown.immutable());
                posDown.move(Direction.DOWN);
            }

            return ret;
        }

        public Set<BlockPos> row() {
            Set<BlockPos> ret = new HashSet<>();
            ret.add(originalPos);

            if (clickedDir == Direction.DOWN || clickedDir == Direction.UP) {
                return ret;
            }

            var rightDir = clickedDir.getCounterClockWise();
            var leftDir = clickedDir.getClockWise();
            var posRight = originalPos.relative(rightDir).mutable();
            var posDown = originalPos.relative(leftDir).mutable();
            while (matchBlock(posRight) && ret.size() <= 64) {
                ret.add(posRight.immutable());
                posRight.move(rightDir);
            }

            while(matchBlock(posDown) && ret.size() <= 64) {
                ret.add(posDown.immutable());
                posDown.move(leftDir);
            }

            return ret;
        }

        public Set<BlockPos> shape() {
            return BlockPos.withinManhattanStream(originalPos, 4, 4, 4)
                    .map(BlockPos::immutable)
                    .filter(this::matchBlock)
                    .collect(Collectors.toSet());
        }

        private boolean matchBlock(BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            if (state.canBeReplaced()) return false;
            if (BlockPatternCapability.hasPattern(pos, level) && BlockPatternCapability.hasPattern(originalPos, level)) {
                int originalId = BlockPatternCapability.getPattern(originalPos, level).patternId();
                int thisId = BlockPatternCapability.getPattern(pos, level).patternId();

                return originalId == thisId;
            }
            boolean isThisVisible = false;
            boolean isThisSturdy = false;
            for(Direction dir : Direction.values()) {
                if (state.isFaceSturdy(level, pos, dir)) {
                    isThisSturdy = true;
                    break;
                }
            }

            for(Direction dir : Direction.values()) {
                if (!level.getBlockState(pos.relative(dir)).isSolidRender()) {
                    isThisVisible = true;
                    break;
                }
            }

            return isThisVisible && isThisSturdy;
        }
    }
}
