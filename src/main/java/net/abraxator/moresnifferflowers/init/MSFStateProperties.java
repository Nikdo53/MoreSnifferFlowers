package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;

public interface MSFStateProperties {
     IntegerProperty AGE_1 = IntegerProperty.create("age", 0, 1);
     IntegerProperty AGE_2 = IntegerProperty.create("age", 0, 2);
     IntegerProperty AGE_3 = IntegerProperty.create("age", 0, 3);
     IntegerProperty AGE_8 = IntegerProperty.create("age", 0, 8);
     EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
     BooleanProperty FLIPPED = BooleanProperty.create("flipped");
     EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
     BooleanProperty SHEARED = BooleanProperty.create("sheared");
     BooleanProperty VIVICUS_CURED = BooleanProperty.create("vivicus_cured");
     IntegerProperty LAYER = BlockStateProperties.LAYERS;
     BooleanProperty EMPTY = BooleanProperty.create("empty");
     IntegerProperty FULLNESS = IntegerProperty.create("fullness", 0, 8);
     IntegerProperty USES_4 = IntegerProperty.create("uses", 0, 3);
     BooleanProperty CURED = BooleanProperty.create("cured");
     IntegerProperty AMOUNT_4 = IntegerProperty.create("amount", 1, 4);
     BooleanProperty FULL = BooleanProperty.create("full");
     EnumProperty<BlockPattern> BLOCK_PATTERN = EnumProperty.create("block_pattern", BlockPattern.class);
     IntegerProperty FIRE_TICKS = IntegerProperty.create("fire_ticks", 0, 5);
     BooleanProperty CROWDED = BooleanProperty.create("crowded");

     BooleanProperty NOT_CURED = BooleanProperty.create("not_cured");
     BooleanProperty NOT_CORRUPTED = BooleanProperty.create("not_corrupted");

     static boolean hasCustomLeavesProperties(BlockState state){
        return state.getOptionalValue(MSFStateProperties.NOT_CORRUPTED).isPresent() && state.getOptionalValue(MSFStateProperties.NOT_CORRUPTED).isPresent();
    }


}
