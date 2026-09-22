package net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.abraxator.moresnifferflowers.init.MSFWood;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class VivicusTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<VivicusTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            p_338099_ -> trunkPlacerParts(p_338099_).apply(p_338099_, VivicusTrunkPlacer::new)
    );

    public VivicusTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return MSFWood.TrunkPlacerTypes.VIVICUS_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int pFreeTreeHeight, BlockPos pos, TreeConfiguration config) {
        List<FoliagePlacer.FoliageAttachment> ret = new ArrayList<>();
        Map<BlockPos, BlockPos> blocks = new LinkedHashMap<>();
        int lastLogHeight = pFreeTreeHeight - ((int) Mth.randomBetween(random, 2, 3));
        BlockPos.MutableBlockPos mainTrunk = pos.mutable();
        for(int i = 0; i < (pFreeTreeHeight); i++) {
            this.placeLog(level, blockSetter, random, mainTrunk, config);
            
            if (i == 0) {
                for(Direction direction : Direction.Plane.HORIZONTAL) {
                    int outerHeight = random.nextInt(3);;
                    int cornerHeight = outerHeight + (random.nextInt(2) - 1);
                    int innerHeight = Math.min(outerHeight + random.nextIntBetweenInclusive(5, 7), pFreeTreeHeight - 1);
                    if(random.nextDouble() <= 0.90D) {
                        BlockPos blockPosInner = pos.relative(direction);
                        for(int j = 0; j < innerHeight; j++) {
                            this.placeLog(level, blockSetter, random, blockPosInner.above(j), config);
                        }

                        if(random.nextDouble() <= 0.8D) {
                            ret.add(new FoliagePlacer.FoliageAttachment(blockPosInner.above(innerHeight), 4, false));
                        }

                        if(random.nextDouble() <= 0.90D) {
                            BlockPos blockPosCorner = blockPosInner.relative(random.nextDouble() > 0.5D ? direction.getClockWise() : direction.getCounterClockWise());
                            for(int j = 0; j < cornerHeight; j++) {
                                this.placeLog(level, blockSetter, random, blockPosCorner.above(j), config);
                            }
                        }

                        if(random.nextDouble() <= 0.90D) {
                            BlockPos blockPosOuter = blockPosInner.relative(direction);
                            for(int j = 0; j < outerHeight; j++) {
                                this.placeLog(level, blockSetter, random, blockPosOuter.above(j), config);
                            }
                        }
                    }
                }
            }
            
            mainTrunk.move(Direction.UP);
        }
        
        ret.add(new FoliagePlacer.FoliageAttachment(mainTrunk.below(1), 0, false));
        return ret;
    }

    private void placeStump(WorldGenLevel level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, BlockPos.MutableBlockPos stumpPos, TreeConfiguration config) {
        if (random.nextDouble() <= 0.3D) {
            for (int height = 0; height < random.nextInt(3); height++) {
                this.placeLog(level, blockSetter, random, stumpPos.immutable().above(height), config);
            }
        }
    }
    
    @Override
    protected boolean validTreePos(WorldGenLevel level, BlockPos pos) {
        return super.validTreePos(level, pos) || level.isStateAtPosition(pos, blockState -> blockState.is(MSFTags.MSFBlockTags.VIVICUS_TREE_REPLACABLE));
    }
}
