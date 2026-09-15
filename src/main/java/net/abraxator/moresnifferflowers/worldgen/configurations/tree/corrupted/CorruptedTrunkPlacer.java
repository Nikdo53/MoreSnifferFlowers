package net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.MSFWood;
import net.abraxator.moresnifferflowers.init.config.MSFServerConfig;
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
import java.util.List;
import java.util.function.BiConsumer;

public class CorruptedTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<CorruptedTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            p_70161_ -> trunkPlacerParts(p_70161_).apply(p_70161_, CorruptedTrunkPlacer::new)
    );
    
    public CorruptedTrunkPlacer(int pBaseHeight, int pHeightRandA, int pBranchCount) {
        super(pBaseHeight, pHeightRandA, pBranchCount);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return MSFWood.TrunkPlacerTypes.CORRUPTED_TRUNK_PLACER.get();
    }


    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int pFreeTreeHeight, BlockPos pos, TreeConfiguration config) {
        List<FoliagePlacer.FoliageAttachment> ret = new ArrayList<>();
        int lastLogHeight = pFreeTreeHeight - ((int) Mth.randomBetween(random, 2, 3));
        BlockPos.MutableBlockPos mainTrunk = pos.mutable();
        Direction growthDir = null;
        int outerHeight = random.nextInt(3);;
        int cornerHeight = outerHeight + (random.nextInt(2) - 1);
        int innerHeight =  Math.min(outerHeight + random.nextIntBetweenInclusive(3, 5), pFreeTreeHeight - 2);

        for(int i = 0; i < pFreeTreeHeight; i++) {
            
            this.placeLog(level, blockSetter, random, mainTrunk, config);

            if (i == pFreeTreeHeight - 1){
                for(int branchOrder = 0; branchOrder < heightRandB; branchOrder++) {
                    addBranch(mainTrunk.immutable(), ret, blockSetter, branchOrder, level, config, random, pFreeTreeHeight);
                }
            }

            if (i == 0) {
                fattenTrunk(level, blockSetter, random, pos, config, innerHeight, ret, cornerHeight, outerHeight);
            }

            mainTrunk.move(Direction.UP);
        }

       // ret.add(new FoliagePlacer.FoliageAttachment(mainTrunk.above(1), 0, false));
        return ret;
    }

    private void fattenTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, BlockPos pos, TreeConfiguration config, int innerHeight, List<FoliagePlacer.FoliageAttachment> ret, int cornerHeight, int outerHeight) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if(random.nextDouble() <= 0.90D) {
                BlockPos blockPosInner = pos.relative(direction);
                for(int j = 0; j < innerHeight; j++) {
                    this.placeLog(level, blockSetter, random, blockPosInner.above(j), config);
                    //addBranch(blockPosInner, direction, branchesPos, branchesDir, random, j);
                }

               /* if(random.nextDouble() <= 0.8D) {
                    ret.add(new FoliagePlacer.FoliageAttachment(blockPosInner.above(innerHeight), 1, false));
                } */

                if(random.nextDouble() <= 0.90D) {
                    BlockPos blockPosCorner = blockPosInner.relative(random.nextDouble() > 0.5D ? direction.getClockWise() : direction.getCounterClockWise());
                    for(int j = 0; j < cornerHeight; j++) {
                        this.placeLog(level, blockSetter, random, blockPosCorner.above(j), config);
                        //addBranch(blockPosInner, direction, branchesPos, branchesDir, random, j);
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

    private void addBranch(BlockPos blockPos, List<FoliagePlacer.FoliageAttachment> ret, BiConsumer<BlockPos, BlockState> blockSetter, int branchOrder, WorldGenLevel level, TreeConfiguration config, RandomSource random, int pFreeTreeHeight) {
        Direction direction = computeBranchDir(random);
        BlockPos.MutableBlockPos pos = blockPos.relative(direction).mutable();
        BlockPos.MutableBlockPos defaultPos = blockPos.relative(direction).mutable();
        int branchLength = Math.min(random.nextIntBetweenInclusive(5, 7), pFreeTreeHeight);
        // int branchLength = 6;
        int branchDir = (int)(360f/heightRandB)*branchOrder;
        int v1 = (branchOrder == 0) ? 1 : (branchOrder == 2) ? -1 : 0;
        int v3 = (branchOrder == 1) ? 1 : (branchOrder == 3) ? -1 : 0;

        for(int x = 0; x < branchLength; x++) {
            float branchHeightRand = (float)x / branchLength;

            if (branchHeightRand < random.nextFloat() & branchHeightRand > 0) {
                this.placeLog(level, blockSetter, random, pos.move(0, 1, 0), config);
            }

            if (branchHeightRand > random.nextFloat()/1.5 & branchHeightRand > 0.5F) {
                this.placeLog(level, blockSetter, random, pos.move(0, -1, 0), config);
            }

            if (x == 0) {
                this.placeLog(level, blockSetter, random, pos.move(0, 0, 0), config);
            }

            this.placeLog(level, blockSetter, random, pos.move(v1, 0, v3), config);
            ret.add(new FoliagePlacer.FoliageAttachment(pos.above(), 0, false));

            if(x == branchLength - 1) {
                this.placeLog(level, blockSetter, random, pos.move(0, -1, 0), config);
           //     ret.add(new FoliagePlacer.FoliageAttachment(pos.below(1), 0, false));
            }
        }

    }

    private static Direction computeBranchDir(RandomSource random) {
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Direction clockAdjusted = random.nextBoolean() ? direction.getClockWise() : direction.getCounterClockWise();
        return random.nextBoolean() ? direction : clockAdjusted;
    }
    
    @Override
    protected boolean validTreePos(WorldGenLevel level, BlockPos pos) {
        return MSFServerConfig.CORRUPTED_TREE_GROW_THROUGH.get() || super.validTreePos(level, pos);
    }
}