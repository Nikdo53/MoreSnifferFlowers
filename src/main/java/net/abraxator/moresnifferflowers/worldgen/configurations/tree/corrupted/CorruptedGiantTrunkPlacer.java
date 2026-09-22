package net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.MSFWood;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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

public class CorruptedGiantTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<CorruptedGiantTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            p_70161_ -> trunkPlacerParts(p_70161_).apply(p_70161_, CorruptedGiantTrunkPlacer::new)
    );

    public CorruptedGiantTrunkPlacer(int pBaseHeight, int pHeightRandA, int pBranchCount) {
        super(pBaseHeight, pHeightRandA, pBranchCount);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return MSFWood.TrunkPlacerTypes.CORRUPTED_GIANT_TRUNK_PLACER.get();
    }



    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int treeHeightA, BlockPos pos, TreeConfiguration config) {
        List<FoliagePlacer.FoliageAttachment> ret = new ArrayList<>();
        BlockPos.MutableBlockPos mainTrunk = pos.mutable();
        BlockPos.MutableBlockPos tempTrunk = pos.mutable();
        int trunkRadius = 2;
        int trunkHeight = 3;
        int treeHeight = random.nextIntBetweenInclusive(0, heightRandA) + treeHeightA;
        int branchRnd = random.nextIntBetweenInclusive(0, 3);

        for(int i = 0; i < treeHeight; i++) {

            if (i == 0) {
                tempTrunk.set(mainTrunk);
                this.placeLog(level, blockSetter, random, tempTrunk, config);
               // blockSetter.accept(tempTrunk.above(), ModBlocks.AMBER_BLOCK.get().defaultBlockState());
                this.placeLog(level, blockSetter, random, tempTrunk.move(Direction.NORTH), config);
                this.placeLog(level, blockSetter, random, tempTrunk.move(Direction.EAST), config);
                this.placeLog(level, blockSetter, random, tempTrunk.move(Direction.SOUTH), config);
                this.placeLog(level, blockSetter, random, tempTrunk.move(Direction.SOUTH), config);
                this.placeLog(level, blockSetter, random, tempTrunk.move(Direction.WEST), config);
                this.placeLog(level, blockSetter, random, tempTrunk.move(Direction.WEST), config);
                this.placeLog(level, blockSetter, random, tempTrunk.move(Direction.NORTH), config);
                this.placeLog(level, blockSetter, random, tempTrunk.move(Direction.NORTH), config);

                for(int trunkOrder = 0; trunkOrder < (trunkRadius*2)*4; trunkOrder++)
                    fattenTrunk(level, blockSetter, random, pos, config, trunkOrder, ret, trunkHeight, trunkRadius);
                for (int u = 0; u < trunkHeight*2; u++){
                    mainTrunk.move(Direction.UP);
                }

            }
            for(int u = 0 ; u < 4 ; u++){
                tempTrunk.set(mainTrunk);
                this.placeLog(level, blockSetter, random, tempTrunk, config);
                int z = (u == 0) ? 1 : (u == 1) ? -1 : 0;
                int x = (u == 2) ? 1 : (u == 3) ? -1 : 0;
                tempTrunk.move(x,0,z);
                this.placeLog(level, blockSetter, random, tempTrunk, config);

                if (random.nextFloat() < 0.3F && i < (treeHeight-4) ){
                    tempTrunk.set(mainTrunk);
                    Direction randDir = Direction.getRandom(random);
                    if (randDir.getAxis() != Direction.Axis.Y) {
                        tempTrunk.move(randDir);
                        tempTrunk.move(randDir.getCounterClockWise());
                        for (int p = 0; p < random.nextIntBetweenInclusive(2, 4); p++) {
                            this.placeLog(level, blockSetter, random, tempTrunk, config);
                            tempTrunk.move(Direction.UP);
                        }
                    }
                }
            }
                if (i % 4 == 2 && i < treeHeight-5) {
                    int branchOrder = branchRnd + Mth.floor((float)i / 4);
                    if (random.nextFloat() < 0.3F && branchOrder > 1) branchOrder = branchOrder - 2 ;
                    addSmallBranch(mainTrunk.immutable(), ret, blockSetter, level, config, random, branchOrder);
                }
                if (i == treeHeight-1)
                    for (int c = 0; c < heightRandB; c++){
                        addTopBranch(mainTrunk.immutable(), ret, blockSetter, level, config, random, c);
                    }


            mainTrunk.move(Direction.UP);
        }
        return ret;
    }

    private void fattenTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, BlockPos posOriginal, TreeConfiguration config, int trunkOrder, List<FoliagePlacer.FoliageAttachment> ret, int trunkRadius, int trunkHeight) {
        BlockPos.MutableBlockPos pos = posOriginal.mutable();
        boolean hasDoor = random.nextFloat() < 0.3F && trunkOrder % 4 == 1;
        final Vec3i trunkOffset =
                switch (Mth.floor((float) trunkOrder / 4)) {
                    case 0 -> new Vec3i((trunkOrder % 4)-1,0,1);
                    case 1 -> new Vec3i(-1,0,((trunkOrder % 4)-1));
                    case 2 -> new Vec3i(-((trunkOrder % 4)-1),0,-1);
                    case 3 -> new Vec3i(1,0,-((trunkOrder % 4)-1));
                    default -> new Vec3i(0,0,0);
        };
        Direction trunkDir =
                switch (Mth.floor((float) trunkOrder / 4)) {
                    case 0 -> Direction.NORTH;
                    case 1 -> Direction.EAST;
                    case 2 -> Direction.SOUTH;
                    case 3 -> Direction.WEST;
                    default -> Direction.NORTH;
        };

        for (int i = 0; i < trunkRadius+1; i++){
            pos.move(trunkDir);
        }

        pos.move(trunkOffset);

        if(trunkOrder % 4 == 3){
            pos.move(trunkDir.getOpposite());
        }

        for (int x = 0; (trunkOrder % 4 == 1) ? x <= trunkHeight+1 : x <= trunkHeight ; x++){
            if (hasDoor && x < 2){
                pos.move(Direction.UP);
            }
            else {
                this.placeLog(level, blockSetter, random, pos, config);
                pos.move(Direction.UP);
            }
        }
        if (trunkOrder % 4 != 3) {
            for (int y = 0; y < 3; y++) {
                pos.move(trunkDir.getOpposite());
                this.placeLog(level, blockSetter, random, pos, config);
                if (y == 0) {
                    pos.move(Direction.UP);
                    this.placeLog(level, blockSetter, random, pos, config);
                }
                pos.move(Direction.UP);
            }
        }
    }

    private void addSmallBranch(BlockPos blockPos, List<FoliagePlacer.FoliageAttachment> ret, BiConsumer<BlockPos, BlockState> blockSetter, WorldGenLevel level, TreeConfiguration config, RandomSource random, int branchOrder) {
        Direction direction = computeBranchDir(random);
        BlockPos.MutableBlockPos pos = blockPos.relative(direction).mutable();
        BlockPos.MutableBlockPos defaultPos = blockPos.relative(direction).mutable();
        int branchLength = random.nextIntBetweenInclusive(7, 9);
        // int branchLength = 6;
        int branchDir = (int)(360f/heightRandB)*branchOrder;
        int v1 = (branchOrder % 4 == 0) ? 1 : (branchOrder % 4 == 2) ? -1 : 0;
        int v3 = (branchOrder % 4 == 1) ? 1 : (branchOrder % 4 == 3) ? -1 : 0;

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
           if (x > 1) ret.add(new FoliagePlacer.FoliageAttachment(pos.above(), 0, false));

           if(x == branchLength - 1) {
                this.placeLog(level, blockSetter, random, pos.move(0, -1, 0), config);
           //     ret.add(new FoliagePlacer.FoliageAttachment(pos.below(1), 0, false));
            }
        }

    }
    private void addTopBranch(BlockPos blockPos, List<FoliagePlacer.FoliageAttachment> ret, BiConsumer<BlockPos, BlockState> blockSetter, WorldGenLevel level, TreeConfiguration config, RandomSource random, int branchOrder) {
        Direction direction = computeBranchDir(random);
        BlockPos.MutableBlockPos pos = blockPos.relative(direction).mutable();
        BlockPos.MutableBlockPos defaultPos = blockPos.relative(direction).mutable();
        int branchLength = (branchOrder < 4) ? random.nextIntBetweenInclusive(10, 12) : random.nextIntBetweenInclusive(7, 9);
        // int branchLength = 6;
        int branchDir = (int)(360f/heightRandB)*branchOrder;
        int v1 = (branchOrder % 4 == 0) ? 1 : (branchOrder % 4 == 2) ? -1 : 0;
        int v3 = (branchOrder % 4 == 1) ? 1 : (branchOrder % 4 == 3) ? -1 : 0;
        int v4 = (branchOrder % 2 == 0) ? 1 : -1 ;

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
            if (branchOrder > 3)
                this.placeLog(level, blockSetter, random, pos.move(v4*v3, 0, v4*v1), config);
             ret.add(new FoliagePlacer.FoliageAttachment(pos.above(), 0, false));

            if(x == branchLength - 1) {
                this.placeLog(level, blockSetter, random, pos.move(0, -1, 0), config);
                ret.add(new FoliagePlacer.FoliageAttachment(pos, 0, false));
            }
        }

    }
/*    private void addDirt(BlockPos blockPos, List<FoliagePlacer.FoliageAttachment> ret, BiConsumer<BlockPos, BlockState> blockSetter, WorldGenLevel level, TreeConfiguration config, RandomSource random, int dirtOrder) {
        int dirtLength = 5;
        BlockPos.MutableBlockPos pos = blockPos.below().mutable();

        int v1 = (dirtOrder % 4 == 0) ? 1 : (dirtOrder % 4 == 2) ? -1 : 0;
        int v3 = (dirtOrder % 4 == 1) ? 1 : (dirtOrder % 4 == 3) ? -1 : 0;
        int v4 = (dirtOrder % 2 == 0) ? 1 : -1 ;

        addDirt(pos, ret, blockSetter, level, config, random, dirtOrder);
        for(int x = 0; x < dirtLength; x++) {
            addDirt(pos.move(v1, 0, v3), ret, blockSetter, level, config, random, dirtOrder);

            if ( x == 0 && dirtOrder < 4) addDirt(pos.move(v1, 0, v3), ret, blockSetter, level, config, random, dirtOrder);
            if (dirtOrder > 3 && dirtOrder < 8) {
                addDirt(pos.move(v4 * v3, 0, v4 * v1), ret, blockSetter, level, config, random, dirtOrder);
            }
            if (dirtOrder > 7) {
                addDirt(pos.move(-v4 * v3, 0, -v4 * v1), ret, blockSetter, level, config, random, dirtOrder);
            }
        }
    }*/

        private static Direction computeBranchDir(RandomSource random) {
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Direction clockAdjusted = random.nextBoolean() ? direction.getClockWise() : direction.getCounterClockWise();
        return random.nextBoolean() ? direction : clockAdjusted;
    }

    @Override
    protected boolean validTreePos(WorldGenLevel level, BlockPos pos) {
        return true;
    }
}