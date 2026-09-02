package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.blockentities.DyespriaPlantBlockEntity;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.client.MSFColorHandler;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFDataComponents;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

import static net.abraxator.moresnifferflowers.items.DyespriaItem.copyAllBlockStateProperties;

public class DyescrapiaItem extends BlockItem {
    public DyescrapiaItem(Properties properties) {
        super(MSFBlocks.DYESCRAPIA_PLANT.get(), properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var pos = context.getClickedPos();
        var state = context.getLevel().getBlockState(pos);
        var player = context.getPlayer();
        var level = context.getLevel();
        var stack = context.getItemInHand();
        int uses = getDyescrapiaUses(stack) + 1;


        if (BlockPatternCapability.hasPattern(pos, level)) {
            if(uses >= 4) {
                player.addItem(BlockPattern.fromId(BlockPatternCapability.getPattern(pos, level).patternId()).getItem().getDefaultInstance());
                uses = 0;
            }
            BlockPatternCapability.removePattern(pos, level);
            stack.set(MSFDataComponents.USES, uses);

            return InteractionResult.SUCCESS;
        }

        if(state.getBlock() instanceof Colorable colorable) {
            var dye = new Dye(colorable.getDyeFromBlock(state).color(), 1);
            if(!dye.color().equals(DyeColor.WHITE)) {
                colorable.colorBlock(level, pos, state, new Dye(DyeColor.WHITE, 1));
                
                if(uses >= 4) {
                    player.addItem(Dye.stackFromDye(dye));
                    uses = 0;
                }

                stack.set(MSFDataComponents.USES, uses);
                return InteractionResult.SUCCESS;
            }            
        } else if (state.is(MSFTags.BlockTags.DYED)){

            Identifier location = BuiltInRegistries.BLOCK.getKey(state.getBlock());
            String modId = location.getNamespace();
            String blockId = location.getPath();
            String finalBlockId;
            String validColorName = "white|light_gray|gray|black|brown|red|orange|yellow|lime|green|cyan|light_blue|blue|purple|magenta|pink";
            boolean colorless = false;


            if(blockId.endsWith("candle") || blockId.endsWith("shulker_box") || (blockId.endsWith("terracotta") && !blockId.endsWith("glazed_terracotta"))) {
                finalBlockId = blockId.replaceFirst((validColorName), "").replaceFirst("_","");
                colorless = true;
            } else if (blockId.endsWith("stained_glass") || blockId.endsWith("stained_glass_pane") ){
                finalBlockId = blockId.replaceFirst((validColorName), "").replaceFirst("_stained_", "");
                colorless = true;
            } else {
                finalBlockId = blockId.replaceFirst(validColorName, "");
            }

            if ((!blockId.contains("white_") || colorless) && !finalBlockId.equals(blockId)){

                Block finalBlock = net.minecraft.world.level.block.Blocks.AIR;
                for (int i = 0; i < 3; i++) {

                    finalBlock = switch (i) {
                        case 0 -> BuiltInRegistries.BLOCK.get(Identifier.fromNamespaceAndPath(modId, finalBlockId));
                        case 1 -> BuiltInRegistries.BLOCK.get(Identifier.fromNamespaceAndPath(modId,"white" + finalBlockId));
                        case 2 -> BuiltInRegistries.BLOCK.get(Identifier.fromNamespaceAndPath(modId,finalBlockId + "white"));
                        default -> net.minecraft.world.level.block.Blocks.AIR;
                    };

                    if (finalBlock != net.minecraft.world.level.block.Blocks.AIR) {
                        break;
                    }
                }

                if (finalBlock == net.minecraft.world.level.block.Blocks.AIR) {
                    return InteractionResult.FAIL;
                }

                BlockState finalBlockState = finalBlock.defaultBlockState();

                BlockEntity originalShulker = level.getBlockEntity(pos);
                CompoundTag shulkerData = null;

                if(originalShulker instanceof ShulkerBoxBlockEntity entity) {
                    shulkerData = entity.saveWithoutMetadata(level.registryAccess());
                }

                if (finalBlock != net.minecraft.world.level.block.Blocks.AIR) level.setBlockAndUpdate(pos, copyAllBlockStateProperties(state, finalBlockState));

                if (shulkerData != null && level.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity newShulkerBox) {
                    newShulkerBox.loadFromTag(shulkerData, level.registryAccess());
                }

                if(uses >= 4) {
                    String dyeName = blockId.replace(blockId.replaceFirst(validColorName, ""), "") + "_dye";
                    player.addItem(BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath("minecraft", dyeName)).getDefaultInstance());
                    uses = 0;
                }

                stack.set(MSFDataComponents.USES, uses);
                return InteractionResult.SUCCESS;

            } else return InteractionResult.FAIL;

        }
        
        return handlePlacement(pos, level, player, context.getHand(), context.getItemInHand());
    }

    private InteractionResult handlePlacement(BlockPos blockPos, Level level, Player player, InteractionHand hand, ItemStack stack) {
        var posForDyespria = blockPos.above();
        var blockHitResult = new BlockHitResult(posForDyespria.below().getCenter(), Direction.UP, posForDyespria.below(), false);
        var useOnCtx = new UseOnContext(level, player, hand, stack, blockHitResult);
        var result = super.useOn(useOnCtx);

        if (level.getBlockEntity(blockPos.above()) instanceof DyespriaPlantBlockEntity entity) {
            entity.dye = Dye.getDyeFromDyespria(stack);
            entity.setChanged();
        }

        return result;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int input = getDyescrapiaUses(stack)-1;
        int maxInput= 4;

        return MSFColorHandler.barColorHelper(input, maxInput);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(getDyescrapiaUses(stack) * 13.0F / 4);
    }

    public static int getDyescrapiaUses(ItemStack stack) {
        return stack.getOrDefault(MSFDataComponents.USES, 0);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatableWithFallback("tooltip.dyescrapia", "Scrapes the dye off of colored blocks").withStyle(ChatFormatting.GOLD));
    }
}
