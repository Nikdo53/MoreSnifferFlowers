package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFEntityTypes;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

public class SaltProjectile extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Boolean> CORRUPTED = SynchedEntityData.defineId(SaltProjectile.class, EntityDataSerializers.BOOLEAN);

    public SaltProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SaltProjectile(Level level, LivingEntity pShooter, ItemStack stack) {
        super(MSFEntityTypes.SALT_PROJECTILE.get(), pShooter, level, stack);
    }

    public SaltProjectile(Level level) {
        super(MSFEntityTypes.SALT_PROJECTILE.get(), level);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        var pos = result.getBlockPos();
        var posRelative = result.getBlockPos().relative(result.getDirection());

        var state = this.level().getBlockState(pos);
        var stateRelative = this.level().getBlockState(posRelative);

        if (level().isClientSide()) return;
        if (!isCorrupted()){
            if (placeBlockSalt(pos, state)) {
                discard();
            } else if (placeBlockSalt(posRelative, stateRelative)) {
                discard();
            }
        } else {
            if (placeBlockSour(pos, 0, result)) {
                discard();
            } else if (placeBlockSour(posRelative, 0, result)) {
                discard();
            }
        }

    }

    public boolean placeBlockSalt(BlockPos pos, BlockState state) {
        Level level = this.level();
        if (state.canBeReplaced()){
            if (!MSFBlocks.SALTY_CLUMP.get().defaultBlockState().canSurvive(level, pos)){
                return true;
            }
            level.setBlock(pos, MSFBlocks.SALTY_CLUMP.get().defaultBlockState().setValue(MSFStateProperties.AMOUNT_4, 1), 2);
            return true;
        }
        if (state.is(MSFBlocks.SALTY_CLUMP.get())){
           int amount = state.getValue(MSFStateProperties.AMOUNT_4);
           if (amount < 4){
               level.setBlock(pos, state.setValue(MSFStateProperties.AMOUNT_4, amount+1), 3);
           } else
               level.setBlock(pos, MSFBlocks.DRIPSALT.get().defaultBlockState().setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.UP), 3);
           return true;
        }
        return false;
    }

    public boolean placeBlockSour(BlockPos pos, int loop, BlockHitResult hitResult) {
        Level level = this.level();
        BlockState state = level.getBlockState(pos);

        if (loop > 2) return false;

        if (state.canBeReplaced()) {
            if (!MSFBlocks.SOUR_PUDDLE.get().defaultBlockState().canSurvive(level, pos)){
                return true;
            }

            if (level instanceof ServerLevel serverLevel) {
                BlockState stateForPlacement = MSFBlocks.SOUR_PUDDLE.get().getStateForPlacement(
                        new BlockPlaceContext(
                                new UseOnContext(
                                        FakePlayerFactory.getMinecraft(serverLevel),
                                        InteractionHand.MAIN_HAND,
                                        new BlockHitResult(hitResult.getLocation(), hitResult.getDirection(), pos, hitResult.isInside()))));

                if (stateForPlacement != null) {
                    level.setBlock(pos, stateForPlacement, 3);
                }
            }
            return true;
        }
        if (state.is(MSFBlocks.SOUR_PUDDLE.get())){
            return placeBlockSour(aroundPos(pos, random.nextInt(7)), loop+1, hitResult);
        }
        return false;
    }

    public static BlockPos aroundPos(BlockPos pos, int direction ) {
        return switch (direction) {
            case 0 -> pos.north().west();
            case 1 -> pos.north();
            case 2 -> pos.north().east();
            case 3 -> pos.east();
            case 4 -> pos.south().east();
            case 5 -> pos.south();
            case 6 -> pos.south().west();
            case 7 -> pos.west();
            default -> pos;
        };
    }

    @Override
    protected Item getDefaultItem() {
        return MSFItems.SALTY_SPICE.get();
    }


    public boolean isCorrupted() {
        return this.entityData.get(CORRUPTED);
    }

    public void setCorrupted(boolean corrupted) {
        this.entityData.set(CORRUPTED, corrupted);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("corrupted", this.isCorrupted());

    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setCorrupted(input.getBooleanOr("corrupted", isCorrupted()));

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CORRUPTED, false);

    }

}
