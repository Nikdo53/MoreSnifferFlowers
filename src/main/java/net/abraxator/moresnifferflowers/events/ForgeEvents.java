package net.abraxator.moresnifferflowers.events;

import com.google.common.collect.ImmutableList;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.CorruptionCapability;
import net.abraxator.moresnifferflowers.effects.GluedEffect;
import net.abraxator.moresnifferflowers.effects.IMSFPotionEffect;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.items.JarOfBonmeelItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.TriState;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event){
        Holder<MobEffect> effect = event.getEffectInstance().getEffect();
        LivingEntity entity = event.getEntity();

        if (effect.equals(MSFEffects.GLUED)){
            GluedEffect.setAndSync(entity, true, true);
        }
    }

    @SubscribeEvent
    public static void onEffectRemove(MobEffectEvent.Remove event){
        MobEffectInstance effect = event.getEffectInstance();
        LivingEntity entity = event.getEntity();

        if (effect == null) return;
        onEffectEnd(effect, entity);

        if (effect.is(MSFEffects.EXTRACTED)){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEffectExpire(MobEffectEvent.Expired event){
        MobEffectInstance effect = event.getEffectInstance();
        LivingEntity entity = event.getEntity();

        if (effect == null) return;
        onEffectEnd(effect, entity);
    }

    public static void onEffectEnd(MobEffectInstance effectInstance, LivingEntity entity) {
        Holder<MobEffect> effect = effectInstance.getEffect();
        if (effect.value() instanceof IMSFPotionEffect msfEffect){
            msfEffect.onEffectEnd(entity, effectInstance);
        }

        if (entity instanceof Player player) {
            if (effect.equals(MSFEffects.COMBO_MEAL))
                player.getData(MSFDataAttachments.COMBO_MEAL).onEffectEnd(player);

            if (effect.equals(MSFEffects.UNTOUCHABLE))
                player.getData(MSFDataAttachments.UNTOUCHABLE).onEffectEnd(player);

        }

        if (effect.equals(MSFEffects.GLUED))
            GluedEffect.setAndSync(entity, false, true);

        if (effect.equals(MSFEffects.SLIPPERY))
            entity.getData(MSFDataAttachments.SLIPPERY).onEffectEnd(entity);

    }

    @SubscribeEvent
    public static void blockDrops(BlockDropsEvent event){
        BlockState state = event.getState();
        if (state.getBlock() instanceof ColorableVivicusBlock colorable){
            int colorId = state.getValue(MSFStateProperties.COLOR).getId();
            int color = colorable.colorValues().get(DyeColor.byId(colorId));

            for (ItemEntity itemEntity : event.getDrops()) {
                ItemStack stack = itemEntity.getItem();
                if (stack.is(MSFTags.ItemTags.COLORABLE)) {
                    stack.set(MSFDataComponents.COLOR, color);
                    stack.set(MSFDataComponents.COLOR_ID, colorId);
                }
                itemEntity.setItem(stack);
            }

        }
    }

    @SubscribeEvent
    public static void lootTableLoad(LootTableLoadEvent event){
        if (event.getKey().identifier().equals(BuiltInLootTables.SNIFFER_DIGGING.identifier())) {
            LootTable table = event.getTable();
            LootPool pool = table.getPool("main");
            if (pool == null){
                MoreSnifferFlowers.LOGGER.error("Failed to add MoreSnifferFlowers loot to sniffer digging loot table, pool 'main' not found");
                return;
            }

            List<Item> items = List.of(MSFItems.DAWNBERRY_VINE_SEEDS.get(), MSFItems.DYESPRIA_SEEDS.get(), MSFItems.AMBUSH_SEEDS.get(), MSFItems.CAULORFLOWER_SEEDS.get(),
                    MSFItems.BONMEELIA_SEEDS.get(), MSFItems.BONDRIPIA_SEEDS.get(), MSFBlocks.VIVICUS_SAPLING.get().asItem(), MSFItems.SALTEMONE_SEEDS.get());

            pool.entries = new ArrayList<>(pool.entries);
            pool.entries.addAll(items.stream().map(item -> LootItem.lootTableItem(item).build()).toList());
            pool.entries = ImmutableList.copyOf(pool.entries);
        }

    }

    @SubscribeEvent
    public static void itemEntity(ItemTossEvent event){
        ItemEntity itemEntity = event.getEntity();
        ItemStack item = itemEntity.getItem();

       if (item.is(MSFItems.BURNED_SLOT)){
           event.setCanceled(true);
       }

    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        boolean isCharged = player.getAttackStrengthScale(0.5f) > 0.9f;
        Entity entity = event.getTarget();
        Level level = player.level();

        if (player.hasEffect(MSFEffects.COMBO_MEAL) && stack.is(Tags.Items.MELEE_WEAPON_TOOLS))
            player.getData(MSFDataAttachments.COMBO_MEAL).onAttack(player, isCharged);


        if(player.hasEffect(MSFEffects.GLUING_TOUCH) && isCharged && entity instanceof LivingEntity livingEntity && !level.isClientSide()) {
            int amplifier = Objects.requireNonNull(player.getEffect(MSFEffects.GLUING_TOUCH)).getAmplifier();

            if (level.getRandom().nextFloat() < ((amplifier + 2) / 12f)) {
                livingEntity.addEffect(new MobEffectInstance(MSFEffects.GLUED, (5 + amplifier*2) * 20, 0));
            }

        }
    }

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Pre event) {
        Player player = event.getPlayer();
        ItemEntity itemEntity = event.getItemEntity();

        if (player.hasEffect(MSFEffects.STICKY)) {
           if (!player.isCrouching()) {
               event.setCanPickup(TriState.FALSE);
           } else {
               int amplifier = player.getEffect(MSFEffects.STICKY).getAmplifier();
               int slowdown = 5 + amplifier*2;

               if (player.level().getGameTime() % slowdown != 0) {
                   event.setCanPickup(TriState.FALSE);
                   return;
               }
                ItemStack stack = itemEntity.getItem();
                ItemStack retStack = stack.split(1);

                player.addItem(retStack);

                itemEntity.setItem(stack);
                event.setCanPickup(TriState.FALSE);
           }

        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity livingEntity = event.getEntity();
        Level level = livingEntity.level();
        Vec3 loc = livingEntity.position();
        BlockPos blockPos = BlockPos.containing(loc);

        if(level.getBlockState(blockPos).is(MSFBlocks.CORRUPTED_SLIME_LAYER) || level.getBlockState(blockPos.below()).is(MSFBlocks.CORRUPTED_SLIME_LAYER)) {
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().multiply(1, 0.3, 1));
        }

    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
       BlockState state = event.getPlacedBlock();
       LevelAccessor badLevel = event.getLevel();

       if (state.is(MSFTags.BlockTags.CORRUPTION_SHIELDING) && badLevel instanceof Level level){
           LevelChunk chunk = level.getChunkAt(event.getPos());
           CorruptionCapability cap = chunk.getData(MSFDataAttachments.CHUNK_CORRUPTION);

           cap.resistance++;
           cap.isSource = false;
           cap.flowers.add(event.getPos());
       }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack output = event.getCrafting();
        Container input = event.getInventory();

        if (output.is(MSFTags.ItemTags.COLORABLE)){
            for (int i = 0; i < input.getContainerSize(); i++) {
                ItemStack stack = input.getItem(i);

                int colorId = stack.getOrDefault(MSFDataComponents.COLOR_ID.get(), -1);
                int color = stack.getOrDefault(MSFDataComponents.COLOR.get(), -1);

                if (colorId != -1 && color != -1) {
                    output.set(MSFDataComponents.COLOR_ID, colorId);
                    output.set(MSFDataComponents.COLOR, color);
                    break;
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void highPriorityClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().getItem() instanceof JarOfBonmeelItem jarOfBonmeelItem) {

            if (event.getLevel().getBlockState(event.getPos()).is(net.minecraft.tags.BlockTags.CAULDRONS)) return;

            InteractionResult interactionResult = jarOfBonmeelItem.highPriorityUseOn(new UseOnContext(event.getEntity(), event.getHand(), event.getHitVec()));

            event.setCancellationResult(interactionResult);
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void onPlayerInteractRightClickItem(UseItemOnBlockEvent event) {
        Player player = event.getPlayer();
        assert player != null;
        InteractionHand hand = event.getHand();
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Consumer<InteractionResult> cancellation = (result) -> {
            event.setCancellationResult(result);
            event.setCanceled(true);
        };

        if (event.isCanceled()) return;

        if((stack.is(MSFItems.REBREWED_POTION.get()) || stack.is(MSFItems.EXTRACTED_BOTTLE.get())) && state.is(Blocks.DIRT)) {
            cancellation.accept(InteractionResult.FAIL);
        }

        if ((stack.is(MSFItems.JAR_OF_BONMEEL.get()) || stack.is(MSFItems.JAR_OF_ACID.get())) && state.getBlock() instanceof AbstractCauldronBlock cauldronBlock) {
            if (cauldronBlock.isFull(state) || state.hasProperty(LayeredCauldronBlock.LEVEL)) return;

            var cauldronType = stack.is(MSFItems.JAR_OF_BONMEEL.get()) ? MSFBlocks.BONMEEL_FILLED_CAULDRON.get() :  MSFBlocks.ACID_FILLED_CAULDRON.get();
            var state1 = cauldronType.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3);
            level.setBlock(pos, state1, 3);
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);

            if (!player.isCreative()) player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, Items.GLASS_BOTTLE.getDefaultInstance()));

            cancellation.accept(InteractionResult.SUCCESS);
        }

        if (BlockPatternCapability.hasPattern(pos, level) && stack.is(Items.GLOW_INK_SAC)){
            BlockPatternCapability.PatternData data = BlockPatternCapability.getPattern(pos, level);
            if (!data.isGlowing()){
                BlockPatternCapability.enableGlowing(level, pos);
                if (!player.isCreative()) stack.shrink(1);

                cancellation.accept(InteractionResult.SUCCESS);
            }

        }

        if (stack.is(Items.FLINT_AND_STEEL) && state.is(Blocks.TORCHFLOWER)){
            stack.hurtAndBreak(1, player, hand);
            player.setItemInHand(hand, stack);
            level.setBlock(pos, MSFBlocks.TORCHFLOWER_AFLAME.get().defaultBlockState().setValue(MSFStateProperties.AGE_2, 1), 3);

            cancellation.accept(InteractionResult.SUCCESS);
        }


    }



}
