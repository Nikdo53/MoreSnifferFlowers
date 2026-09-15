package net.abraxator.moresnifferflowers.worldgen.feature;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;

public class VivicusTreeFeature extends Feature<TreeConfiguration> {
    public VivicusTreeFeature(Codec<TreeConfiguration> codec) {
        super(codec);
    }

    public static boolean isVine(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, state -> state.is(Blocks.VINE));
    }

    public static boolean isAirOrLeaves(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, state -> state.isAir() || state.is(BlockTags.LEAVES));
    }

    private static void setBlockKnownShape(LevelWriter level, BlockPos pos, BlockState blockState) {
        level.setBlock(pos, blockState, 19);
    }

    public static boolean validTreePos(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, state -> state.isAir() || state.is(BlockTags.REPLACEABLE_BY_TREES));
    }

    public boolean doPlace(
            WorldGenLevel level,
            RandomSource random,
            BlockPos origin,
            BiConsumer<BlockPos, BlockState> rootSetter,
            BiConsumer<BlockPos, BlockState> trunkSetter,
            FoliagePlacer.FoliageSetter foliageSetter,
            TreeConfiguration config
    ) {
        int treeHeight = config.trunkPlacer.getTreeHeight(random);
        int foliageHeight = config.foliagePlacer.foliageHeight(random, treeHeight, config);
        int trunkHeight = treeHeight - foliageHeight;
        int leafRadius = config.foliagePlacer.foliageRadius(random, trunkHeight);
        BlockPos trunkOrigin = config.rootPlacer.<BlockPos>map(rootPlacer -> rootPlacer.getTrunkOrigin(origin, random)).orElse(origin);
        int minY = Math.min(origin.getY(), trunkOrigin.getY());
        int maxY = Math.max(origin.getY(), trunkOrigin.getY()) + treeHeight + 1;
        if (minY >= level.getMinY() + 1 && maxY <= level.getMaxY() + 1) {
            OptionalInt minClippedHeight = config.minimumSize.minClippedHeight();
            int clippedTreeHeight = this.getMaxFreeTreeHeight(level, treeHeight, trunkOrigin, config);
            if (clippedTreeHeight >= treeHeight || !minClippedHeight.isEmpty() && clippedTreeHeight >= minClippedHeight.getAsInt()) {
                if (config.rootPlacer.isPresent() && !config.rootPlacer.get().placeRoots(level, rootSetter, random, origin, trunkOrigin, config)) {
                    return false;
                } else {
                    List<FoliagePlacer.FoliageAttachment> foliageAttachments = config.trunkPlacer
                            .placeTrunk(level, trunkSetter, random, clippedTreeHeight, trunkOrigin, config);
                    foliageAttachments.forEach(
                            foliageAttachment -> config.foliagePlacer
                                    .createFoliage(level, foliageSetter, random, config, clippedTreeHeight, foliageAttachment, foliageHeight, leafRadius)
                    );
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getMaxFreeTreeHeight(WorldGenLevel level, int maxTreeHeight, BlockPos treePos, TreeConfiguration config) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

        for (int y = 0; y <= maxTreeHeight + 1; y++) {
            int r = config.minimumSize.getSizeAtHeight(maxTreeHeight, y);

            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    blockPos.setWithOffset(treePos, x, y, z);
                    if (!config.trunkPlacer.isFree(level, blockPos) || !config.ignoreVines && isVine(level, blockPos)) {
                        return y - 2;
                    }
                }
            }
        }

        return maxTreeHeight;
    }

    @Override
    protected void setBlock(LevelWriter level, BlockPos pos, BlockState blockState) {
        setBlockKnownShape(level, pos, blockState);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        final WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        BlockState saplingState = level.getBlockState(origin);
        DyeColor color = saplingState.getValue(MSFStateProperties.COLOR);
        TreeConfiguration config = context.config();
        Set<BlockPos> rootPositions = Sets.newHashSet();
        Set<BlockPos> trunks = Sets.newHashSet();
        final Set<BlockPos> foliage = Sets.newHashSet();
        Set<BlockPos> decorations = Sets.newHashSet();
        BiConsumer<BlockPos, BlockState> rootSetter = (pos, state) -> {
            rootPositions.add(pos.immutable());
            state = state.trySetValue(MSFStateProperties.COLOR, color);
            level.setBlock(pos, state, 19);
        };
        BiConsumer<BlockPos, BlockState> trunkSetter = (pos, state) -> {
            trunks.add(pos.immutable());
            state = state.trySetValue(MSFStateProperties.COLOR, color);
            level.setBlock(pos, state, 19);
        };
        FoliagePlacer.FoliageSetter foliageSetter = new FoliagePlacer.FoliageSetter() {
            @Override
            public void set(BlockPos pos, BlockState state) {
                foliage.add(pos.immutable());
                state = state.trySetValue(MSFStateProperties.COLOR, color);
                level.setBlock(pos, state, 19);
            }

            @Override
            public boolean isSet(BlockPos pos) {
                return foliage.contains(pos);
            }
        };
        BiConsumer<BlockPos, BlockState> decorationSetter = (pos, state) -> {
            decorations.add(pos.immutable());
            state = state.trySetValue(MSFStateProperties.COLOR, color);
            level.setBlock(pos, state, 19);
        };
        boolean result = this.doPlace(level, random, origin, rootSetter, trunkSetter, foliageSetter, config);
        if (result && (!trunks.isEmpty() || !foliage.isEmpty())) {
            if (!config.decorators.isEmpty()) {
                TreeDecorator.Context decoratorContext = new TreeDecorator.Context(level, decorationSetter, random, trunks, foliage, rootPositions);
                config.decorators.forEach(decorator -> decorator.place(decoratorContext));
            }

            return BoundingBox.encapsulatingPositions(Iterables.concat(rootPositions, trunks, foliage, decorations)).map(bounds -> {
                DiscreteVoxelShape shape = updateLeaves(level, bounds, trunks, decorations, rootPositions);
                StructureTemplate.updateShapeAtEdge(level, 3, shape, bounds.minX(), bounds.minY(), bounds.minZ());
                return true;
            }).orElse(false);
        } else {
            return false;
        }
    }

    private static DiscreteVoxelShape updateLeaves(
            LevelAccessor level, BoundingBox bounds, Set<BlockPos> logs, Set<BlockPos> decorationSet, Set<BlockPos> rootPositions
    ) {
        DiscreteVoxelShape shape = new BitSetDiscreteVoxelShape(bounds.getXSpan(), bounds.getYSpan(), bounds.getZSpan());
        int maxDistance = 7;
        List<Set<BlockPos>> toCheck = Lists.newArrayList();

        for (int i = 0; i < 7; i++) {
            toCheck.add(Sets.newHashSet());
        }

        for (BlockPos pos : Lists.newArrayList(Sets.union(decorationSet, rootPositions))) {
            if (bounds.isInside(pos)) {
                shape.fill(pos.getX() - bounds.minX(), pos.getY() - bounds.minY(), pos.getZ() - bounds.minZ());
            }
        }

        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
        int smallestDistance = 0;
        toCheck.get(0).addAll(logs);

        while (true) {
            while (smallestDistance >= 7 || !toCheck.get(smallestDistance).isEmpty()) {
                if (smallestDistance >= 7) {
                    return shape;
                }

                Iterator<BlockPos> iterator = toCheck.get(smallestDistance).iterator();
                BlockPos posx = iterator.next();
                iterator.remove();
                if (bounds.isInside(posx)) {
                    if (smallestDistance != 0) {
                        BlockState state = level.getBlockState(posx);
                        setBlockKnownShape(level, posx, state.setValue(BlockStateProperties.DISTANCE, smallestDistance));
                    }

                    shape.fill(posx.getX() - bounds.minX(), posx.getY() - bounds.minY(), posx.getZ() - bounds.minZ());

                    for (Direction direction : Direction.values()) {
                        neighborPos.setWithOffset(posx, direction);
                        if (bounds.isInside(neighborPos)) {
                            int xInShape = neighborPos.getX() - bounds.minX();
                            int yInShape = neighborPos.getY() - bounds.minY();
                            int zinShape = neighborPos.getZ() - bounds.minZ();
                            if (!shape.isFull(xInShape, yInShape, zinShape)) {
                                BlockState currentState = level.getBlockState(neighborPos);
                                OptionalInt distance = LeavesBlock.getOptionalDistanceAt(currentState);
                                if (!distance.isEmpty()) {
                                    int newDistance = Math.min(distance.getAsInt(), smallestDistance + 1);
                                    if (newDistance < 7) {
                                        toCheck.get(newDistance).add(neighborPos.immutable());
                                        smallestDistance = Math.min(smallestDistance, newDistance);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            smallestDistance++;
        }
    }
}
