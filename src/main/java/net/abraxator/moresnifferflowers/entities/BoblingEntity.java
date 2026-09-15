package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.goals.BoblingAttackPlayerGoal;
import net.abraxator.moresnifferflowers.entities.goals.BoblingAvoidPlayerGoal;
import net.abraxator.moresnifferflowers.init.MSFAdvancementCritters;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFEntityTypes;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.config.MSFServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BoblingEntity extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> DATA_CURED = SynchedEntityData.defineId(BoblingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_RUNNING = SynchedEntityData.defineId(BoblingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_WANTED_POS = SynchedEntityData.defineId(BoblingEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Boolean> DATA_PLANTING = SynchedEntityData.defineId(BoblingEntity.class, EntityDataSerializers.BOOLEAN);

    private int idleAnimationTimeout = 0;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState plantingAnimationState = new AnimationState();

    private boolean finalizePlanting = false;
    private int plantingProgress = 0;
    private static final int MAX_PLANTING_PROGRESS = 35;

    public BoblingEntity(EntityType<? extends BoblingEntity> entityType, Level level, boolean type) {
        super(entityType, level);
        setCured(type);
    }

    public BoblingEntity(EntityType<? extends BoblingEntity> entityType, Level level) {
        this(entityType, level, false);
    }

    public BoblingEntity(Level level, boolean type) {
        this(MSFEntityTypes.BOBLING.get(), level, type);
    }

    public boolean isCured() {
        return this.entityData.get(DATA_CURED);
    }

    public void setCured(boolean type) {
        this.entityData.set(DATA_CURED, type);
    }

    public boolean isRunning() {
        return this.entityData.get(DATA_RUNNING);
    }

    public void setRunning(boolean running) {
        this.entityData.set(DATA_RUNNING, running);
    }

    @Nullable
    public BlockPos getWantedPos() {
        return this.entityData.get(DATA_WANTED_POS).orElse(null);
    }

    public void setWantedPos(Optional<BlockPos> wantedPos) {
        this.entityData.set(DATA_WANTED_POS, wantedPos);
    }

    public boolean isPlanting() {
        return this.entityData.get(DATA_PLANTING);
    }

    public void setPlanting(boolean plan) {
        this.entityData.set(DATA_PLANTING, plan);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("cured", this.isCured());
        output.putBoolean("running", this.isRunning());
        output.putBoolean("planting", this.isPlanting());
        if (getWantedPos() != null) {
            output.store("wanted_pos",BlockPos.CODEC, getWantedPos());
        }

    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setCured(input.getBooleanOr("cured", false));
        this.setRunning(input.getBooleanOr("running", false));
        this.setPlanting(input.getBooleanOr("planting", false));
        this.setWantedPos(input.read("wanted_pos", BlockPos.CODEC));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CURED, false);
        builder.define(DATA_RUNNING, false);
        builder.define(DATA_WANTED_POS, Optional.empty());
        builder.define(DATA_PLANTING, false);
    }

    @Override
    protected void registerGoals() {
        /* else if (this.getBoblingType() == Type.CURED) {
            this.goalSelector.addGoal(3, new TemptGoal(this, 0.9F, itemStack ->
                    itemStack.is(ModItems.JAR_OF_BONMEEL), false));
        }*/

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BoblingAttackPlayerGoal(this, 1.5F, false));
        this.goalSelector.addGoal(2, new BoblingAvoidPlayerGoal<>(this, Player.class, 16.0F, 1.0F, 1.3F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    protected void actuallyHurt(ServerLevel level, DamageSource damageSource, float dmg) {
        super.actuallyHurt(level, damageSource, dmg);
        if (this.isRunning() && damageSource.is(DamageTypes.PLAYER_ATTACK) && !isCured()) {
            var r = 1.0;
            var checkR = 1.5;
            Set<Vec3> set = new HashSet<>();

            if (damageSource.getEntity() instanceof ServerPlayer serverPlayer) {
                MSFAdvancementCritters.BOBLING_ATTACK.get().trigger(serverPlayer);
            }

            if (MSFServerConfig.CORRUPTED_BOBLING_GRIEFING.get()) {
                for (double theta = 0; theta <= Mth.TWO_PI; theta += Mth.TWO_PI / random.nextIntBetweenInclusive(2, 5)) {
                    generateProjectile(set, r, theta + this.level().getRandom().nextDouble(), checkR);
                }
            }

        }

        if (!this.isRunning() && damageSource.is(DamageTypes.PLAYER_ATTACK)) {
            this.setRunning(true);
        }

    }

    @Override
    protected int calculateFallDamage(double fallDistance, float damageModifier) {
        if (this.tickCount <= 60) return 0;
        return super.calculateFallDamage(fallDistance, damageModifier);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isPlanting()) {
            this.plantingProgress++;
            if (plantingProgress >= MAX_PLANTING_PROGRESS) {
                this.finalizePlanting = true;
                this.setPlanting(false);
            }
        }

        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Override
    public void aiStep() {
        for (WrappedGoal wrappedGoal : goalSelector.getAvailableGoals()) {
            if(wrappedGoal == null) {
                MoreSnifferFlowers.LOGGER.error("NULL");
            }
        }
        for (WrappedGoal wrappedGoal : targetSelector.getAvailableGoals()) {
            if(wrappedGoal == null) {
                MoreSnifferFlowers.LOGGER.error("NULL");
            }
        }

        super.aiStep();

        if (canPlant()) {
            this.plantingProgress = 0;
            this.setPlanting(true);
            this.removeFreeWill();
            this.setYRot(this.getDirection().toYRot());
        }

        if(this.finalizePlanting && this.isAlive()) {
            var blockPos = BlockPos.containing(this.position()).relative(this.getDirection());
            if (!level().isClientSide()) {

                boolean config = MSFServerConfig.CORRUPTED_BOBLING_GRIEFING.get();
                boolean isReplaceable = level().getBlockState(blockPos).canBeReplaced();

                if (config || isReplaceable) {
                    this.level().setBlockAndUpdate(blockPos, MSFBlocks.CORRUPTED_SAPLING.get().defaultBlockState());
                    if (level().getBlockState(blockPos.below()).canBeReplaced())
                        this.level().setBlockAndUpdate(blockPos.below(), MSFBlocks.CORRUPTED_GRASS_BLOCK.get().defaultBlockState());
                }

            }
            this.discard();
        }
    }

    private boolean canPlant() {
        BlockPos pos = this.getWantedPos();
        return  this.isRunning() &&
                this.isAlive() &&
                !this.isPlanting() &&
                pos != null &&
                this.level().getBlockState(pos).canBeReplaced() &&
                this.level().getBlockState(pos.below()).isFaceSturdy(level(), pos, Direction.UP) &&
                this.position().closerThan(pos.getCenter(), 0.75F);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if(this.isPlanting()) {
            this.idleAnimationState.stop();
            if(!this.plantingAnimationState.isStarted()) {
                this.plantingAnimationState.start(this.tickCount);
            }
        } else {
            this.plantingAnimationState.stop();
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (itemStack.is(MSFItems.VIVICUS_ANTIDOTE) && !isCured()) {
            this.setCured(true);

            particles(new DustParticleOptions(7118872, 1));
            itemStack.shrink(1);

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    private void particles(ParticleOptions particle) {
        for(int i = 0; i <= 30; i++) {
            double d0 = this.random.nextGaussian() * 0.02;
            double d1 = this.random.nextGaussian() * 0.02;
            double d2 = this.random.nextGaussian() * 0.02;
            this.level().addParticle(particle, this.getRandomX(1), this.getRandomY() + 0.5D, this.getRandomZ(1), d0, d1, d2);
        }
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }


    private void generateProjectile(Set<Vec3> set, double r, double theta, double checkR) {
        var x = this.getX() + r * Mth.cos((float) theta) * 0.8;
        var yx = this.getY() + r * Mth.sin((float) theta) + 0.3;
        var yz = this.getY() + r * Mth.cos((float) theta) + 0.3;
        var z = this.getZ() + r * Mth.sin((float) theta) * 0.8;

      if (random.nextFloat() > 0.5f) createAndAddProjectile(set, checkR, new Vec3(x, yo, z));
      if (random.nextFloat() > 0.5f) createAndAddProjectile(set, checkR, new Vec3(x, yx, zo));
      if (random.nextFloat() > 0.5f) createAndAddProjectile(set, checkR, new Vec3(xo, yz, z));
    }

    private void createAndAddProjectile(Set<Vec3> set, double checkR, Vec3 vec3) {
        AABB aabb = AABB.ofSize(vec3, checkR, checkR, checkR);
        if (set.stream().noneMatch(aabb::contains)) {
            CorruptedProjectile projectile = new CorruptedProjectile(this.level());
            projectile.setPos(vec3);
            float slowdown = 2.8f;
            Vec3 dir = new Vec3((vec3.x() - this.getX())/ slowdown, (vec3.y() - this.getY())/ slowdown, (vec3.z() - this.getZ())/ slowdown);

            projectile.setDeltaMovement(dir);
            this.level().addFreshEntity(projectile);
            set.add(vec3);
        }
    }
}
