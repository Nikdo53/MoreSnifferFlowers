package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.components.Corruptable;
import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.abraxator.moresnifferflowers.init.MSFBlockEntities;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.abraxator.moresnifferflowers.init.config.MSFServerConfig;
import net.abraxator.moresnifferflowers.networking.toClient.CorruptedSludgeParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CorruptedSludgeBlockEntity extends BlockEntity implements IMSFBlockEntity, GameEventListener.Provider<CorruptedSludgeBlockEntity.CorruptedSludgeListener> {
    public CorruptedSludgeListener corruptedSludgeListener;
    public int usesLeft = -1;
    public int stateChange = 1;
    
    public CorruptedSludgeBlockEntity(BlockPos pos, BlockState state) {
        super(MSFBlockEntities.CORRUPTED_SLUDGE.get(), pos, state);
        this.corruptedSludgeListener = new CorruptedSludgeListener(new BlockPositionSource(pos));
        this.stateChange = usesLeft / 4;
    }

    public void updateUses() {
        this.usesLeft--;

        if (stateChange == 0) { // I HATE YOU !!!!!!! WHY ARE YOU ZERO
            this.stateChange = usesLeft / 4;
        }

        if(this.usesLeft % stateChange == 0 && this.getBlockState().getValue(MSFStateProperties.USES_4) - 1 != -1) {
            this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(MSFStateProperties.USES_4, this.getBlockState().getValue(MSFStateProperties.USES_4) - 1));
        }

        if(this.usesLeft <= 0) {
            if (MSFServerConfig.CORRUPTED_SLUDGE_GRIEFING.get()) CorruptedSludgeListener.shootProjectiles(this.getBlockPos().getCenter(), this.level.getRandom().nextIntBetweenInclusive(8, 16), this.level);
            super.setRemoved();
            this.level.setBlockAndUpdate(this.getBlockPos(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
        }

    }
    
    @Override
    public CorruptedSludgeListener getListener() {
        return corruptedSludgeListener;
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        tag.putInt("uses", usesLeft);
    }

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        usesLeft = tag.getIntOr("uses", usesLeft);
    }

    public static class CorruptedSludgeListener implements GameEventListener {
        private PositionSource positionSource;

        public CorruptedSludgeListener(PositionSource positionSource) {
            this.positionSource = positionSource;
        }

        @Override
        public PositionSource getListenerSource() {
            return this.positionSource;
        }

        @Override
        public int getListenerRadius() {
            return GameEvent.BLOCK_DESTROY.value().notificationRadius();
        }



        @Override
        @SuppressWarnings("deprecation")
        public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {
            CorruptedSludgeBlockEntity entity;

            if(level.getBlockEntity(BlockPos.containing(this.positionSource.getPosition(level).get())) instanceof CorruptedSludgeBlockEntity entity1) {
                entity = entity1;
            } else return false;
            
            boolean validEvent = (gameEvent != GameEvent.BLOCK_PLACE || gameEvent != GameEvent.BLOCK_DESTROY);
            
            if (entity.usesLeft == -1) {
                entity.usesLeft = level.getRandom().nextIntBetweenInclusive(16, 32) - 1;
                entity.stateChange = entity.usesLeft / 4;
            }
            
            if(entity.usesLeft <= 0 || entity.getBlockState().getValue(MSFStateProperties.CURED) || !validEvent) {
                return false;
            }

            if(gameEvent.is(GameEvent.BLOCK_PLACE) && Corruptable.canBeCorrupted(context.affectedState().getBlock(), level.getRandom())) {
                Vec3 startPos = this.getListenerSource().getPosition(level).get();
                Vec3 dirNormal = new Vec3(pos.x - startPos.x, pos.y - startPos.y, pos.z - startPos.z).normalize();
                Optional<Block> corrupted = Corruptable.getCorruptedBlock(context.affectedState().getBlock(), level.getRandom());
                BlockPos blockPos = BlockPos.containing(pos);
                corrupted.ifPresent(block -> {
                    PacketDistributor.sendToAllPlayers(new CorruptedSludgeParticlePacket(startPos.toVector3f(), pos.toVector3f(), dirNormal.toVector3f()));
                    if(level.getBlockState(BlockPos.containing(pos)).getBlock() instanceof net.abraxator.moresnifferflowers.blocks.Corruptable corruptable) {
                        corruptable.onCorrupt(level, blockPos, level.getBlockState(BlockPos.containing(pos)), block);
                    } else {
                        level.setBlockAndUpdate(BlockPos.containing(pos), block.withPropertiesOf(context.affectedState()));
                    }
                    level.sendParticles(
                            new DustParticleOptions(0x443248, 1.0F),
                            blockPos.getX() + level.getRandom().nextDouble(), blockPos.getY() + level.getRandom().nextDouble(), blockPos.getZ() + level.getRandom().nextDouble(),
                            10,
                            0.0D, 0.0D, 0.0D,
                            0.0D
                    );

                    entity.updateUses();
                });

                return false;
            }

            if (MSFServerConfig.CORRUPTED_SLUDGE_GRIEFING.get()) {
                if (gameEvent.is(GameEvent.BLOCK_DESTROY) && context.affectedState().is(MSFTags.BlockTags.CORRUPTED_SLUDGE) && !pos.equals(this.positionSource.getPosition(level).get()) && context.sourceEntity() instanceof Player player) {
                    final var projectileNumber = context.affectedState().is(MSFBlocks.CORRUPTED_LEAVES) || context.affectedState().is(MSFBlocks.CORRUPTED_LEAVES_BUSH) ? level.getRandom().nextInt(1) + 1 : level.getRandom().nextInt(5) + 1;
                    shootProjectiles(this.positionSource.getPosition(level).get(), projectileNumber, level);
                    entity.updateUses();
                    return false;
                }
            }

            return false;
        }
        
        public static void shootProjectiles(Vec3 center, int projectileNumber, Level level) {
            var radius = 2.5;
            Set<Vec3> placed = new HashSet<>();

            for(int i = 0; i < projectileNumber; i++) {
                generatePoint(placed, center, radius, level);
            }
        }

        private static void generatePoint(Set<Vec3> placed, Vec3 center, double radius, Level level) {
            var random = level.getRandom();

            double theta = 2 * Mth.PI * random.nextDouble();
            double phi = Math.acos(2 * random.nextDouble() - 1);

            double xg = center.x + radius * Mth.sin((float) phi) * Mth.cos((float) theta);
            double yg = center.y + radius * Mth.sin((float) phi) * Mth.sin((float) theta);
            double zg = center.z + radius * Mth.cos((float) phi);
            var vec3 = new Vec3(xg, yg, zg);
            
            if (placed.stream().noneMatch(vec31 -> AABB.ofSize(vec3, 1, 1, 1).contains(vec31)) && level.getBlockState(BlockPos.containing(vec3)).canBeReplaced()) {
                var pos = center;
                var x = random.nextDouble() * 0.5;
                var y = random.nextDouble() * 0.5;
                var z = random.nextDouble() * 0.5;
                CorruptedProjectile projectile = new CorruptedProjectile(level);
                projectile.setPos(vec3);
                Vec3 dir = new Vec3(projectile.getX() - pos.x, projectile.getY() - pos.y, projectile.getZ() - pos.z).normalize().multiply(x, y, z);
                projectile.setDeltaMovement(dir);
                level.addFreshEntity(projectile);

                //level.sendParticles(ModParticles.CARROT.get(), vec3.x, vec3.y, vec3.z, 1, 0D, 0D, 0D, 0D);
                placed.add(vec3);
            }
        }
    }
}