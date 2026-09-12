package net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.MSFWood;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CorruptedSludgeDecorator extends TreeDecorator {
    public static final MapCodec<CorruptedSludgeDecorator> CODEC = RecordCodecBuilder.mapCodec(
            p_225996_ -> p_225996_.group(
                            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(p_226014_ -> p_226014_.probability),
                            Codec.intRange(0, 16).fieldOf("exclusion_radius_xz").forGetter(p_226012_ -> p_226012_.exclusionRadiusXZ),
                            Codec.intRange(0, 16).fieldOf("exclusion_radius_y").forGetter(p_226010_ -> p_226010_.exclusionRadiusY),
                            BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(p_226008_ -> p_226008_.blockProvider),
                            ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter(p_225998_ -> p_225998_.directions)
                    )
                    .apply(p_225996_, CorruptedSludgeDecorator::new)
    );

    protected final float probability;
    protected final int exclusionRadiusXZ;
    protected final int exclusionRadiusY;
    protected final BlockStateProvider blockProvider;
    protected final List<Direction> directions;

    public CorruptedSludgeDecorator(float probability, int exclusionRadiusXZ, int exclusionRadiusY, BlockStateProvider blockProvider, List<Direction> directions) {
        this.probability = probability;
        this.exclusionRadiusXZ = exclusionRadiusXZ;
        this.exclusionRadiusY = exclusionRadiusY;
        this.blockProvider = blockProvider;
        this.directions = directions;
    }

    @Override
    public void place(TreeDecorator.Context context) {
        Set<BlockPos> set = new HashSet<>();
        RandomSource randomsource = context.random();

        for (BlockPos blockpos : Util.shuffledCopy(context.leaves(), randomsource)) {
            Direction direction = Util.getRandom(this.directions, randomsource);
            BlockPos blockpos1 = blockpos.relative(direction);
            if (!set.contains(blockpos1) && randomsource.nextFloat() < this.probability) {
                BlockPos blockpos2 = blockpos1.offset(-this.exclusionRadiusXZ, -this.exclusionRadiusY, -this.exclusionRadiusXZ);
                BlockPos blockpos3 = blockpos1.offset(this.exclusionRadiusXZ, this.exclusionRadiusY, this.exclusionRadiusXZ);

                for (BlockPos blockpos4 : BlockPos.betweenClosed(blockpos2, blockpos3)) {
                    set.add(blockpos4.immutable());
                }

                context.setBlock(blockpos1, this.blockProvider.getState(context.level(), randomsource, blockpos1));
            }
        }
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return MSFWood.TreeDecoratorTypes.CORRUPTED_SLUDGE.get();
    }
}
