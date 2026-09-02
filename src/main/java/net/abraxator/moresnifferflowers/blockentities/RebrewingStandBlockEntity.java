package net.abraxator.moresnifferflowers.blockentities;

import com.google.common.collect.Lists;
import net.abraxator.moresnifferflowers.blocks.rebrewingstand.RebrewingStandBlockBase;
import net.abraxator.moresnifferflowers.client.gui.menu.RebrewingStandMenu;
import net.abraxator.moresnifferflowers.init.MSFBlockEntities;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.config.MSFServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.function.Predicate;

public class RebrewingStandBlockEntity extends BaseContainerBlockEntity {
    public static final double MAX_FUEL = 16;
    public static final int DATA_PROGRESS = 0;
    public static final int DATA_FUEL = 1;
    public static final int DATA_COST = 2;
    public static final int MAX_PROGRESS = 100;
    private NonNullList<ItemStack> inv = NonNullList.withSize(6, ItemStack.EMPTY);
    int brewProgress;
    int fuel;
    int cost;
    private boolean[] lastPotionCount;
    public final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> RebrewingStandBlockEntity.this.brewProgress;
                case 1 -> RebrewingStandBlockEntity.this.fuel;
                case 2 -> RebrewingStandBlockEntity.this.cost;
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0:
                    RebrewingStandBlockEntity.this.brewProgress = pValue;
                    break;
                case 1:
                    RebrewingStandBlockEntity.this.fuel = pValue;
                    break;
                case 2:
                    RebrewingStandBlockEntity.this.cost = pValue;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public RebrewingStandBlockEntity(BlockPos pos, BlockState state) {
        super(MSFBlockEntities.REBREWING_STAND.get(), pos, state);
        lastPotionCount = new boolean[0];
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("");
    }

    @Override
    public boolean isEmpty() {
        return inv.stream().allMatch(Predicate.not(ItemStack::isEmpty));
    }

    public void tick(Level level) {
        //FUEL 0; OG-POTION 1; INGREDIENT 2; POTION 3 - 5;
        var fuelStack = inv.get(0);
        var ogPotionStack = inv.get(1);
        var ingredientStack = inv.get(2);
        var potionBits = getPotionBits();
        cost = 0;

        if(fuel < MAX_FUEL && fuelStack.is(MSFItems.CROPRESSED_NETHERWART.get())) {
            fuel++;
            fuelStack.shrink(1);
            setChanged();
        }

        if(!ogPotionStack.isEmpty()) {
            var potionContent = getPotionContents(ogPotionStack, ingredientStack);
            this.cost = potionContent != null ? 4 + ((potionContent.getB().size() - 2)) * 2 : 17;

            if(canBrew()) {
                brewProgress++;
                if(brewProgress >= MAX_PROGRESS) {
                    brew(level, ogPotionStack, ingredientStack);
                }
            }
        }

        if(!canBrew()) {
            brewProgress = 0;
        }

        if(!Arrays.equals(potionBits, lastPotionCount)) {
            bottleStateLogic(potionBits);
        }
    }

    private void brew(Level level, ItemStack ogPotionStack, ItemStack ingredientStack) {
        var potionContent = getPotionContents(ogPotionStack, ingredientStack);
        if(potionContent != null) {
            List<Integer> index = Util.make(Lists.newArrayList(), integers -> integers.addAll(Arrays.asList(3, 4, 5)));
            for (int i : index) {
                ItemStack itemStack = inv.get(i);
                if (!itemStack.is(ItemStack.EMPTY.getItem())) {
                    ItemStack outputPotion = MSFItems.REBREWED_POTION.get().getDefaultInstance();

                    if(ingredientStack.is(MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_SPLASH.get()))) {
                        outputPotion = MSFItems.REBREWED_SPLASH_POTION.get().getDefaultInstance();
                    } else if (ingredientStack.is(MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_LINGERING.get()))) {
                        outputPotion = MSFItems.REBREWED_LINGERING_POTION.get().getDefaultInstance();
                    }


                    outputPotion.set(DataComponents.POTION_CONTENTS, potionContent.getA());
                    inv.set(i, outputPotion);
                }
            }

            ingredientStack.shrink(1);
            fuel -= cost;
            inv.set(1, Items.GLASS_BOTTLE.getDefaultInstance());
            level.playSound(null, getBlockPos(), SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        brewProgress = 0;
    }

    private void bottleStateLogic(boolean[] potionBits) {
        lastPotionCount = potionBits;
        BlockState blockstate = level.getBlockState(getBlockPos().below());
        if (!(blockstate.getBlock() instanceof RebrewingStandBlockBase)) {
            return;
        }

        for(int i = 0; i < RebrewingStandBlockBase.HAS_BOTTLE.length; ++i) {
            blockstate = blockstate.setValue(RebrewingStandBlockBase.HAS_BOTTLE[i], potionBits[i]);
        }

        level.setBlock(getBlockPos().below(), blockstate, 2);
    }

    private boolean canBrew() {
        boolean ret = false;
        boolean correctInvContent = !inv.get(2).isEmpty() && inv.get(1).is(MSFItems.EXTRACTED_BOTTLE.get());
        boolean hasFuel = fuel >= 1 && this.fuel >= this.cost;
        boolean correctCost = this.cost <= 16;


        for(int i = 3; i <= 5; i++) {
            if(!inv.get(i).isEmpty() && !inv.get(i).is(MSFItems.REBREWED_POTION.get())) {
                ret = true;
            }
        }

        return ret && correctInvContent && hasFuel && correctCost;
    }

    private boolean[] getPotionBits() {
        boolean[] ret = new boolean[3];

        for(int i = 3; i <= 5; ++i) {
            if (!this.inv.get(i).isEmpty()) {
                ret[i - 3] = true;
            }
        }

        return ret;
    }

    private @Nullable Pair<PotionContents, List<MobEffectInstance>> getPotionContents(ItemStack inputPotion, ItemStack ingredient) {
        List<MobEffectInstance> ret = new ArrayList<>();
        List<Integer> durList = new ArrayList<>();
        var potionContents = inputPotion.get(DataComponents.POTION_CONTENTS);
        int defaultAmp = 1;
        int defaultDur = 6000;

        if (potionContents == null) {
            return null;
        }

        potionContents.forEachEffect(mobEffectInstance -> {
            var dur = mobEffectInstance.getDuration() + (ingredient.is(MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_AMPLIFIER.get())) ? 12000 : defaultDur);
            var amp = mobEffectInstance.getAmplifier() + (ingredient.is(MSFServerConfig.itemFromLoc(MSFServerConfig.REBREWING_LENGTH.get())) ? 2 : defaultAmp);
            durList.add(dur);
            ret.add(new MobEffectInstance(mobEffectInstance.getEffect(), dur, amp));
        }, 1);

        int maxInt = Collections.max(durList);
        ret.add(new MobEffectInstance(MSFEffects.EXTRACTED, maxInt));

        OptionalInt colorOptional = PotionContents.getColorOptional(ret);
        Optional<Integer> color = colorOptional.isPresent() ? Optional.of(colorOptional.getAsInt()) : Optional.empty();
        return new Pair<>(new PotionContents(Optional.of(Potions.WATER), color, ret, Optional.empty()), ret);
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new RebrewingStandMenu(pContainerId, pInventory, this, this.containerData);
    }

    @Override
    public int getContainerSize() {
        return 6;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < this.inv.size() ? this.inv.get(slot) : ItemStack.EMPTY;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inv;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.inv = nonNullList;
    }

    @Override
    public ItemStack removeItem(int slot, int pAmount) {
        return ContainerHelper.removeItem(this.inv, slot, pAmount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inv, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inv.size()) {
            this.inv.set(slot, stack);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.inv.clear();
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, inv, true);
        tag.putInt("progress", (brewProgress));
        tag.putInt("fuel", (fuel));
    }

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        inv = NonNullList.withSize(6, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, inv);
        fuel = tag.getIntOr("fuel", fuel);
        brewProgress = tag.getIntOr("progress", brewProgress);
    }
}