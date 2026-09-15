package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.client.gui.screen.DyespriaTooltip;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.components.DyespriaMode;
import net.abraxator.moresnifferflowers.components.EntityDistanceComparator;
import net.abraxator.moresnifferflowers.components.PatternspriaMode;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFDataComponents;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.text.WordUtils;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class PatternspriaItem extends Item {
    public PatternspriaItem(Properties properties) {
        super(properties);
    }

    public static final int DEFAULT_COLOR = 0xA9948D;

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack stack = context.getItemInHand();
        Direction horizontalDirection = context.getHorizontalDirection();
        BlockPattern fromPatternspria = BlockPattern.fromPatternspria(stack);

        if (context.getHand() != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if (player.isCrouching() && BlockPatternCapability.hasPattern(blockPos, level)) {
            if (!stack.has(MSFDataComponents.COLOR)){
                stack.set(MSFDataComponents.COLOR, -1);
            }
            if (stack.getOrDefault(MSFDataComponents.COLOR, DEFAULT_COLOR) != BlockPatternCapability.getPattern(blockPos, level).color() ) {
                copyColor(stack, level, blockPos);
                return InteractionResult.SUCCESS;
            }
        }

        if (blockState.is(MSFBlocks.PATTERNFLOWER.get()) && fromPatternspria != BlockPattern.EMPTY){
            if (BlockPattern.fromState(blockState).equals(fromPatternspria)) return InteractionResult.PASS;
            level.setBlock(blockPos, blockState.setValue(MSFStateProperties.BLOCK_PATTERN, fromPatternspria).setValue(MSFStateProperties.EMPTY, false), 3);
            finishColoring(fromPatternspria.getItemStack(stack), level, stack, blockPos, context.getClickedFace());
            return InteractionResult.SUCCESS;
        }

        if (canUse(blockPos, level, stack)) {
            int oldCount = stack.getOrDefault(MSFDataComponents.AMOUNT, 0);
            AtomicInteger currentCount = new AtomicInteger(oldCount);
            AtomicBoolean canContinueDyeing = new AtomicBoolean(true);
            PatternspriaMode dyespriaMode = getMode(stack);
            PatternspriaMode.DyespriaSelector dyespriaSelector = new PatternspriaMode.DyespriaSelector(blockPos, level, context.getClickedFace());

            Set<BlockPos> set = dyespriaMode.getSelector().apply(dyespriaSelector);
            final Map<BlockPos, BlockPatternCapability.PatternData> cachedPatterns = BlockPatternCapability.getPatterns(level, blockPos);

            set.stream().sorted(new EntityDistanceComparator(blockPos)).takeWhile(t -> canContinueDyeing.get()).forEach(blockPos1 -> {

                if(canUse(blockPos1, level, stack) && fromPatternspria != BlockPattern.EMPTY) {
                    patternOne(stack, level, blockPos1, fromPatternspria, context.getClickedFace(), horizontalDirection, cachedPatterns);
                    currentCount.getAndDecrement();

                } else if (stack.getOrDefault(MSFDataComponents.AMOUNT, 0) <= 0 || fromPatternspria == BlockPattern.EMPTY){
                    canContinueDyeing.set(false);
                }

                if (oldCount - currentCount.get() >= 64) {
                    canContinueDyeing.set(false);
                }
            });

            BlockPatternCapability.setBulkPatterns(cachedPatterns, level);

            return InteractionResult.SUCCESS;
        }

        BlockPattern pattern = fromPatternspria;
        if (BlockPatternCapability.hasPattern(blockPos, level) && pattern != BlockPattern.EMPTY ) {
            BlockPatternCapability.PatternData patternData = BlockPatternCapability.getPattern(blockPos, level);
            if (!patternData.direction().equals(horizontalDirection) && pattern.getId() == patternData.patternId()){
                BlockPatternCapability.setPattern(level,blockPos, new BlockPatternCapability.PatternData(patternData.patternId(), patternData.color(), horizontalDirection, patternData.isGlowing() ));
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(context);
    }

    public PatternspriaMode getMode(ItemStack stack) {
        return stack.getOrDefault(MSFDataComponents.PATTERNSPRIA_MODE, PatternspriaMode.SINGLE);
    }

    public void patternOne(ItemStack stack, Level level, BlockPos blockPos, BlockPattern pattern, Direction face, Direction horizontalDirection, Map<BlockPos, BlockPatternCapability.PatternData> cachedPatterns) {

        int color = getColor(stack);
        if (BlockPatternCapability.hasPattern(blockPos, level)) color = BlockPatternCapability.getPattern(blockPos, level).color();

        cachedPatterns.put(blockPos.immutable(), new BlockPatternCapability.PatternData(pattern.getId(), color, horizontalDirection, false));

        finishColoring(pattern.getItemStack(stack), level, stack, blockPos, face);
    }

    private boolean canUse(BlockPos pos, Level level, ItemStack patternspria) {

        if (BlockPattern.fromPatternspria(patternspria) == BlockPattern.EMPTY) return false;
        int groundId = -1;
        if (BlockPatternCapability.hasPattern(pos, level)){
            groundId = BlockPatternCapability.getPattern(pos, level).patternId();
        }
        int inputId = BlockPattern.fromPatternspria(patternspria).getId();


        boolean isSturdy = false;
        for(Direction dir : Direction.values()) {
            if (level.getBlockState(pos).isFaceSturdy(level, pos, dir)) {
                isSturdy = true;
                break;
            }
        }

        return (!BlockPatternCapability.hasPattern(pos, level) || inputId != groundId)
                && patternspria.getOrDefault(MSFDataComponents.AMOUNT, 0) > 0 && isSturdy;
    }

    public void finishColoring(ItemStack blockPattern, Level level, ItemStack patternspria, BlockPos blockPos, Direction face) {
        int uses = getPatternspriaUses(patternspria) - 1;
        BlockPattern pattern = BlockPattern.fromItem(blockPattern.getItem());

        if(uses <= 0) {
            blockPattern.shrink(1);
            if (blockPattern.isEmpty()) {
                BlockPattern.removePatternFromStack(patternspria);
            } else {
                setPatternspriaUses(patternspria, 4);
            }
        } else {
            setPatternspriaUses(patternspria, uses);
        }


        BlockPattern.setPatternToHolderStack(patternspria, blockPattern, blockPattern.getCount(), getPatternspriaUses(patternspria));
        if (level.isClientSide() && pattern != BlockPattern.EMPTY) {
            spawnParticles(level.getRandom(), level, pattern, blockPos, face, patternspria);
        }
    }

    public ItemStack addPattern(ItemStack patternspria, ItemStack itemToInsert) {
        BlockPattern pattern = BlockPattern.fromItem(itemToInsert.getItem());
        BlockPattern patternInside = BlockPattern.fromPatternspria(patternspria);

        if (patternInside == BlockPattern.EMPTY){
            BlockPattern.removePatternFromStack(patternspria);
        }

        if (pattern == BlockPattern.EMPTY) {
            return itemToInsert;
        }

        if (patternInside == BlockPattern.EMPTY || patternspria.getOrDefault(MSFDataComponents.AMOUNT, 0) <= 0) {
            onAddPattern(patternspria, itemToInsert, itemToInsert.getCount());
            return ItemStack.EMPTY;
        }

        if (!pattern.isSamePattern(patternspria)) {
            ItemStack returnStack = patternInside.getItemStack(patternspria);
            onAddPattern(patternspria, itemToInsert, itemToInsert.getCount());
            return returnStack;
        }

        int amountInside = patternspria.getOrDefault(MSFDataComponents.AMOUNT, 0);
        int freeSpace = 64 - amountInside;

        if (freeSpace <= 0) {
            return itemToInsert;
        }

        int amountToAdd = Math.min(itemToInsert.getCount(), freeSpace);
        onAddPattern(patternspria, itemToInsert, amountInside + amountToAdd);
        itemToInsert.shrink(amountToAdd);

        return itemToInsert;
    }

    public void onAddPattern(ItemStack destinationStack, ItemStack pattern, int amount) {
        destinationStack.remove(MSFDataComponents.COLOR);
        int uses = getPatternspriaUses(destinationStack);
        BlockPattern.setPatternToHolderStack(destinationStack, pattern, amount, uses <= 0 ? 4 : uses);
    }

    private ItemStack removePattern(ItemStack stack) {
        var pattern = BlockPattern.fromPatternspria(stack);
        int uses = getPatternspriaUses(stack);

        if(pattern != BlockPattern.EMPTY) {
            ItemStack returnStack = pattern.getItemStack(stack);
            BlockPattern.removePatternFromStack(stack);
            returnStack.shrink(uses == 4 ? 0 : 1);
            return returnStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if(action == ClickAction.SECONDARY && slot.allowModification(player)) {
            if(other.isEmpty()) {
                access.set(removePattern(stack));
                playRemoveOneSound(player);
            } else {
                ItemStack itemStack = addPattern(stack, other);
                access.set(itemStack);
                if(itemStack.isEmpty()) {
                    this.playInsertSound(player);
                }
            }
            return true;
        }
        return false;
    }

    public void copyColor(ItemStack patternspria, Level level, BlockPos blockPos){
        if (BlockPatternCapability.hasPattern(blockPos, level)) {
            BlockPatternCapability.PatternData patternData = BlockPatternCapability.getPattern(blockPos, level);
            patternspria.set(MSFDataComponents.COLOR, patternData.color());
        }
    }

    public int getColor(ItemStack patternspria){
        return patternspria.getOrDefault(MSFDataComponents.COLOR, DEFAULT_COLOR);
    }


    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int input = getPatternspriaUses(stack);
        int maxInput= 4;

        int color;
        if (stack.has(MSFDataComponents.COLOR)) {
            color = stack.getOrDefault(MSFDataComponents.COLOR, DEFAULT_COLOR);
        } else
            color = BlockPattern.fromPatternspria(stack).getColor();


        return color;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return BlockPattern.fromPatternspria(stack) != BlockPattern.EMPTY;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(getPatternspriaUses(stack) * 13.0F / 4);
    }

    public static int getPatternspriaUses(ItemStack stack) {
        int uses = stack.getOrDefault(MSFDataComponents.USES, 4);
        if (uses > 4 || uses < 0) {
            MoreSnifferFlowers.LOGGER.warn("Invalid uses amount for patternspria: " + uses);
            stack.set(MSFDataComponents.USES, 4);
            return 4;
        }
        return uses;
    }

    public static void setPatternspriaUses(ItemStack stack, int uses) {
        if (uses > 4 || uses < 0) {
            MoreSnifferFlowers.LOGGER.warn("Tried setting invalid uses amount for patternspria: " + uses);
            stack.set(MSFDataComponents.USES, 4);
            return;
        }
        stack.set(MSFDataComponents.USES, uses);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, builder, tooltipFlag);

        if (!tooltipFlag.hasShiftDown()) {
            builder.accept(Component.translatable("tooltip.dyespria.shift").withStyle(ChatFormatting.GOLD));
            return;
        }

        BlockPattern pattern = BlockPattern.fromPatternspria(stack);
        Component usage = Component.translatableWithFallback("tooltip.patternspria.usage", "Right click with dye to insert \nRight click caulorflower to repaint \nSneak to apply to the whole column \n").withStyle(ChatFormatting.GOLD);
        var usageComponents = Arrays.stream(usage.getString().split("\n", -1))
                .filter(s -> !s.isEmpty())
                .map(String::trim);

        usageComponents.forEach(s -> builder.accept(Component.literal(s).withStyle(ChatFormatting.GOLD)));
        builder.accept(Component.empty());
        builder.accept(DyespriaItem.getCurrentModeComponent(DyespriaMode.byIndex(getMode(stack).ordinal())));
        builder.accept(Component.empty());

        if(pattern != BlockPattern.EMPTY) {
            ItemStack patternStack = pattern.getItemStack(stack);
            var name = Component
                    .literal(patternStack.getCount() + " - " + WordUtils.capitalizeFully(pattern.getSerializedName()
                            .toLowerCase()
                            .replaceAll("[^a-z_]", "")
                            .replaceAll("_", " ")))
                    .withStyle(Style.EMPTY
                            .withColor(pattern.getColor()));
            builder.accept(name);
        } else {
            builder.accept(Component.translatableWithFallback("tooltip.dyespria.empty", "Empty").withStyle(ChatFormatting.GRAY));
        }
    }

    public void spawnParticles(RandomSource randomSource, Level level, BlockPattern pattern, BlockPos blockPos, Direction face, ItemStack stack) {
        Vector3f vector3f = blockPos.getCenter().toVector3f();
        if (face != null) vector3f = vector3f.add(face.step().div(new Vector3f(2,2,2)));
        int color = stack.getOrDefault(MSFDataComponents.COLOR, pattern.getColor());
        for(int i = 0; i <= randomSource.nextIntBetweenInclusive(5, 10); i++) {
            level.addParticle(
                    new DustParticleOptions(color, 1.0F),
                    vector3f.x + randomSource.nextDouble() - 0.5D,
                    vector3f.y + randomSource.nextDouble() - 0.5D,
                    vector3f.z + randomSource.nextDouble() - 0.5D,
                    0, 0, 0);
        }
    }

    public void changeMode(ServerPlayer player, ItemStack stack, int amount) {
        var currentMode = getMode(stack);
        PatternspriaMode newMode = PatternspriaMode.shift(currentMode, amount);
        stack.set(MSFDataComponents.PATTERNSPRIA_MODE, newMode);
        player.sendOverlayMessage(DyespriaItem.getCurrentModeComponent(DyespriaMode.byIndex(newMode.ordinal())));

    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        ItemStack pattern = BlockPattern.getItemStackFromPatternspria(stack);
        return Optional.of(new DyespriaTooltip(pattern, true, getMode(stack).ordinal()));
    }
}
