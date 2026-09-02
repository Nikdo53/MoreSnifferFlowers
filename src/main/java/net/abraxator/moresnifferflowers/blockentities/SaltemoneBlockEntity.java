package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.blocks.SaltemoneBlock;
import net.abraxator.moresnifferflowers.entities.SaltBubbleProjectile;
import net.abraxator.moresnifferflowers.init.MSFBlockEntities;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.abraxator.moresnifferflowers.networking.toClient.SaltemoneParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;

public class SaltemoneBlockEntity extends AbstractMultiBlockEntity implements IMSFBlockEntity {
    public SaltemoneBlockEntity(BlockPos pos, BlockState state) {
        super(MSFBlockEntities.SALTEMONE.get(), pos, state);
    }

    public int bubbleCount = 0;
    public static final int MAX_BUBBLE_COUNT = 5;
    public int fullBubbleTicks = 0;

    public static final int PROBABLY_STUCK_TICKS = 7200;


    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        SaltemoneBlock saltemoneBlock = (SaltemoneBlock) state.getBlock();
        RandomSource random = level.getRandom();

        if (bubbleCount >= MAX_BUBBLE_COUNT){
            fullBubbleTicks++;
            if (fullBubbleTicks > PROBABLY_STUCK_TICKS){
                bubbleCount = 0;
                fullBubbleTicks = 0;
            }
        } else {
            fullBubbleTicks = 0;
        }
        if (!canSpawnBubble(state, random, saltemoneBlock))
            return;

        Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
        Vec3 vec3 = this.getCenter().getCenter().relative(direction, 0.5D).relative(direction.getClockWise(), 0.5D).relative(Direction.UP, 0.0);
        float speed = 0.2F;

        SaltBubbleProjectile projectile = new SaltBubbleProjectile(vec3.x, vec3.y, vec3.z, level, getCenter());
        projectile.setNoGravity(true);
        projectile.setCorrupted(saltemoneBlock.isCorrupted());
        projectile.setState(0);
        projectile.setDeltaMovement((random.nextFloat() - 0.5)*speed,1*speed, (random.nextFloat() - 0.5)*speed);

        level.addFreshEntity(projectile);
        PacketDistributor.sendToAllPlayers(new SaltemoneParticlePacket(vec3.toVector3f()));

        bubbleCount++;
        setChanged();
    }

    private boolean canSpawnBubble(BlockState state, RandomSource random, SaltemoneBlock saltemoneBlock) {
        if (state.getValue(MSFStateProperties.SHEARED)) return false;
        if (bubbleCount >= MAX_BUBBLE_COUNT) return false;
        if (!(getLevel().getGameTime() % 320 == 0 && random.nextFloat() < 0.20f)) return false;
        if (!isCenter()) return false;
        return saltemoneBlock.isMaxAge(state);
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        if (isCenter())
            tag.putInt("bubbleCount", bubbleCount);
    }

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        if (isCenter()) {
            bubbleCount = tag.getIntOr("bubbleCount", bubbleCount);
            bubbleCount = Math.clamp(bubbleCount, 0, MAX_BUBBLE_COUNT);
        }
    }
}
