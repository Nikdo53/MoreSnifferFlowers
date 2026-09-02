package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockBase;
import net.abraxator.moresnifferflowers.client.MSFColorHandler;
import net.abraxator.moresnifferflowers.components.BetterNonNullList;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.networking.NBTCodecHelper;
import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

public class CropressorBlockEntity extends BlockEntity implements Container, IMSFBlockEntity {
    public static int SLOT_SIZE = 9;
    public BetterNonNullList<ItemStack> container = BetterNonNullList.withSize(SLOT_SIZE, ItemStack.EMPTY);
    public Item currentCrop = net.minecraft.world.item.Items.AIR;
    public ItemStack result = ItemStack.EMPTY;
    public int progress = 0;
    public final int MAX_PROGRESS = 100;
    private static final int INV_SIZE = 16;
    private final RecipeManager.CachedCheck<SingleRecipeInput, CropressingRecipe> quickCheck = RecipeManager.createCheck(MSFRecipes.Types.CROPRESSING.get());
    public int barLength = 0;

    public CropressorBlockEntity(BlockPos pos, BlockState state) {
        super(MSFBlockEntities.CROPRESSOR.get(), pos, state);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        long gameTime = level.getGameTime();

        if (progress > 0 && gameTime % 3 == 0) {
            progress++;
            if (progress % 20 == 0) {
                level.playSound(null, worldPosition, MSFSounds.CROPRESSOR_BELT.get(), SoundSource.BLOCKS, 1.0F, (float) (1.0F + (level.getRandom().nextFloat() * 0.2)));
            }

            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);

            if (progress >= MAX_PROGRESS) {
                Vec3 blockPos = getBlockPos().relative(getBlockState().getValue(CropressorBlockBase.FACING).getOpposite()).getCenter();
                ItemEntity entity = new ItemEntity(level, blockPos.x, blockPos.y + 0.5, blockPos.z, result);

                result = ItemStack.EMPTY;
                if (barLength >= 8) barLength = 0;


                level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                level.addFreshEntity(entity);
                level.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(getBlockState()));
                progress = 0;
                setChanged();

            }
        }

        if (gameTime % 10 != 0) return;
        if (gameTime % 20 == 0) suckInItems(level);

        if (container.isFullyDefault()) return;

        if (progress <= 0) {
            for (int slot = 0; slot < SLOT_SIZE; slot++) {
                ItemStack slotStack = container.get(slot);
                if (!slotStack.isEmpty()) {
                    var recipeInput = new SingleRecipeInput(slotStack);

                    var cropressingRecipeOptional = quickCheck.getRecipeFor(recipeInput, level);

                    cropressingRecipeOptional.ifPresent(recipeHolder -> {
                        var recipe = recipeHolder.value();
                        if (slotStack.getCount() >= recipe.count()) {
                            result = recipe.result().copy();
                            slotStack.shrink(recipe.count());
                        }
                    });

                    if (!result.isEmpty()) {
                        progress++;
                        currentCrop = slotStack.getItem();
                        barLength = Math.min(Mth.ceil((float) getTotalAmount(slotStack.getItem()) / 2), 8);
                        setChanged();
                        return;
                    }
                }
            }
        }
    }

    public boolean canInteract() {
        return true;
    }

    public InteractionResult addItem(ItemStack stack) {
        boolean success = false;
        ItemStack copy = stack.copy();

        if (stack.is(MSFTags.ItemTags.CROPRESSABLE)) {

            if (this.hasAnyOf(Set.of(stack.getItem()))) {
                for (int slot = 0; slot < SLOT_SIZE && !stack.isEmpty(); slot++) {

                    ItemStack slotStack = container.get(slot);
                    int space = 64 - slotStack.getCount();

                    if ((slotStack.is(stack.getItem())) && space > 0) {
                        ItemStack split = stack.split(space);
                        slotStack.grow(split.getCount());
                        container.set(slot, slotStack);
                        success = true;
                    }
                }
            }

            if (!stack.isEmpty() && container.getValidSize() < SLOT_SIZE){
                container.set(container.getFirstEmptySlot(), stack.copy());
                stack.shrink(stack.getCount());
                success = true;
            }


            if (success) {
                currentCrop = copy.getItem();
                barLength = Math.min(Mth.ceil((float) getTotalAmount(copy.getItem()) / 2), 8);
                this.setChanged();


                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public int getTotalAmount(Item item){
        int amount = 0;
        for (int slot = 0; slot < SLOT_SIZE; slot++){
            ItemStack stack = container.get(slot);
            if (stack.is(item)){
                amount += stack.getCount();
            }
        }

        return amount;
    }

    public void suckInItems(Level level) {
        Direction direction = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        BlockPos pos = this.worldPosition.relative(direction).above();
        Container container = HopperBlockEntity.getContainerAt(level, pos);
        if (container != null) {
            Direction direction1 = Direction.DOWN;
            if (!isEmptyContainer(container, direction1)) {
                getSlots(container, direction1).anyMatch((slot) -> tryTakeInItemFromSlot(container, slot, direction1));
            }
        }
    }

    private static boolean isEmptyContainer(Container container, Direction direction) {
        return getSlots(container, direction).allMatch((slot) -> container.getItem(slot).isEmpty());
    }

    private static IntStream getSlots(Container container, Direction direction) {
        return container instanceof WorldlyContainer ? IntStream.of(((WorldlyContainer)container).getSlotsForFace(direction)) : IntStream.range(0, container.getContainerSize());
    }

    private boolean tryTakeInItemFromSlot(Container container, int slot, Direction direction) {
        ItemStack originalStack = container.getItem(slot);
        if (!originalStack.isEmpty() && canTakeItemFromContainer(container, originalStack, slot, direction)) {
            ItemStack newStack = originalStack.copy();
            addItem(newStack);
            if (newStack.getCount() != originalStack.getCount()) {
                container.setChanged();
            }
            container.setItem(slot, newStack);
            return true;
        }

        return false;
    }

    private boolean canTakeItemFromContainer(Container destination, ItemStack itemStack, int slot, Direction direction) {
        return true;
    }

    public int getColor() {
        Item item = currentCrop;
        if (item.equals(net.minecraft.world.item.Items.AIR)) return 0x000000;

        if (item.equals(net.minecraft.world.item.Items.POTATO)) return 0xb88c4c;
        if (item.equals(net.minecraft.world.item.Items.CARROT)) return 0xffa135;
        if (item.equals(net.minecraft.world.item.Items.NETHER_WART)) return 0x9e392b;
        if (item.equals(net.minecraft.world.item.Items.BEETROOT)) return 0xc36866;
        if (item.equals(net.minecraft.world.item.Items.WHEAT)) return 0xfff35e;

        int hash = item.toString().hashCode();

        double r = (hash & 0xFF0000) >> 16;
        double g = (hash & 0x00FF00) >> 8;
        double b = hash & 0x0000FF;

        return MSFColorHandler.RGBtoInt(new Vec3(r,g,b));
    }


    @Override
    public int getContainerSize() {
        return SLOT_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return container.isFullyDefault();
    }

    @Override
    public ItemStack getItem(int slot) {
        return container.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = container.get(slot);
        this.setChanged();
        return stack.split(amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return container.setDefault(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        container.set(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        container.clear();
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);

        NBTCodecHelper.encode(BuiltInRegistries.ITEM.byNameCodec(), currentCrop, tag, "current_crop");
        tag.putInt("progress", progress);
        tag.putInt("bar", barLength);
        tag.store("result", ItemStack.OPTIONAL_CODEC, result);

        container.writeToTag(ItemStack.OPTIONAL_CODEC, tag, "slots");
    }

    @Override
    public void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        this.currentCrop = Objects.requireNonNull(NBTCodecHelper.decode(BuiltInRegistries.ITEM.byNameCodec(), tag, "current_crop"));

        progress = tag.getIntOr("progress", progress);
        barLength = tag.getIntOr("bar", barLength);
        result = tag.read("result", ItemStack.OPTIONAL_CODEC).orElse(result);

        BetterNonNullList.readFromTag(container, ItemStack.OPTIONAL_CODEC, tag, "slots");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag;
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), MoreSnifferFlowers.LOGGER)) {
            TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
            saveAdditional(output);
            tag = output.buildResult();
        }
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
