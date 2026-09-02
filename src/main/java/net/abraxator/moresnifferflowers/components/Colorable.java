package net.abraxator.moresnifferflowers.components;

import com.google.common.collect.Maps;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.abraxator.moresnifferflowers.init.MSFStateProperties.COLOR;
import static net.abraxator.moresnifferflowers.init.MSFStateProperties.EMPTY;

public interface Colorable {
    String TAG_HEX = "MSF_Hex";
    String TAG_ID = "MSF_ID";

    default TagKey<Block> matchTag() {
        return null;
    }
    
    default Pair<EnumProperty<DyeColor>, BooleanProperty> getColorAndEmptyProperties() {
        return new Pair<>(COLOR, EMPTY);
    }
        
    default boolean canBeColored(BlockState blockState, Dye dye) {
        return !getDyeFromBlock(blockState).color().equals(dye.color());
    }
    
    default Dye getDyeFromBlock(BlockState blockState) {
        DyeColor dyeColor = DyeColor.WHITE;
        boolean empty = true;
        
        if (blockState.hasProperty(getColorAndEmptyProperties().getA())) {
            dyeColor = blockState.getValue(getColorAndEmptyProperties().getA());
        }
        if (blockState.hasProperty(getColorAndEmptyProperties().getB())) {
            empty = blockState.getValue(getColorAndEmptyProperties().getB());
        }
        
        return new Dye(dyeColor, !empty ? 1 : 0);
    }
    
    default void colorBlock(Level level, BlockPos blockPos, BlockState blockState, Dye dye) {
        level.setBlockAndUpdate(blockPos, blockState.setValue(getColorAndEmptyProperties().getA(), dye.color()));
    } 
    
    default boolean isColorEmpty(BlockState blockState) {
        return blockState.getValue(getColorAndEmptyProperties().getB());
    }

    default ItemStack add(@Nullable ItemStack dyespria, Dye dyeInside, ItemStack dyeToInsert) {
        if (!(dyeToInsert.getItem() instanceof DyeItem)) {
            return dyeToInsert;
        }
        
        if (dyeInside.isEmpty()) {
            onAddDye(dyespria, dyeToInsert, dyeToInsert.getCount());
            return ItemStack.EMPTY;
        }
        
        if (!Dye.dyeCheck(dyeInside, dyeToInsert)) {
            onAddDye(dyespria, dyeToInsert, dyeToInsert.getCount());
            dyeToInsert.shrink(dyeToInsert.getCount());

            ItemStack returnStack = Dye.stackFromDye(dyeInside);

            if (DyespriaItem.getDyespriaUses(dyespria) < 4){
                returnStack.shrink(1);
                DyespriaItem.setDyespriaUses(dyespria, 4);
            }

            return returnStack;
        }
        
        int amountInside = dyeInside.amount();
        int freeSpace = 64 - amountInside;

        if (freeSpace <= 0) {
            return dyeToInsert;
        }
        
        int amountToAdd = Math.min(dyeToInsert.getCount(), freeSpace);
        onAddDye(dyespria, dyeToInsert, amountInside + amountToAdd);
        dyeToInsert.shrink(amountToAdd);
        
        return dyeToInsert;
    }
    
    default void onAddDye(@Nullable ItemStack destinationStack, ItemStack dye, int amount) {}

    default void particles(RandomSource randomSource, Level level, Dye dye, BlockPos blockPos) {
        particles(randomSource, level, dye, blockPos, null);
    }

    default void particles(RandomSource randomSource, Level level, Dye dye, BlockPos blockPos, @Nullable Direction face) {
        Vector3f vector3f = blockPos.getCenter().toVector3f();
        if (face != null) vector3f = vector3f.add(face.step().div(new Vector3f(2,2,2)));
        for(int i = 0; i <= randomSource.nextIntBetweenInclusive(5, 10); i++) {
            level.addParticle(
                    new DustParticleOptions(dye.isEmpty() ? Vec3.fromRGB24(14013909).toVector3f() : Vec3.fromRGB24(Dye.colorForDye(this, dye.color())).toVector3f(), 1.0F),
                    vector3f.x + randomSource.nextDouble() - 0.5D,
                    vector3f.y + randomSource.nextDouble() - 0.5D,
                    vector3f.z + randomSource.nextDouble() - 0.5D,
                    0, 0, 0);
        }
    }

    default Map<DyeColor, Integer> colorValues(){
        return Util.make(Maps.newLinkedHashMap(), map -> Arrays.stream(DyeColor.values()).forEach(color -> map.put(color, color.getTextColor())));
    }

    /**
     * Used for checking modded dyes and making them suck less
     * */
    static List<DyeColor> vanillaDyeColors(){
        ArrayList<DyeColor> list = new ArrayList<>();
        list.add(DyeColor.WHITE);
        list.add(DyeColor.ORANGE);
        list.add(DyeColor.MAGENTA);
        list.add(DyeColor.LIGHT_BLUE);
        list.add(DyeColor.YELLOW);
        list.add(DyeColor.LIME);
        list.add(DyeColor.PINK);
        list.add(DyeColor.GRAY);
        list.add(DyeColor.LIGHT_GRAY);
        list.add(DyeColor.CYAN);
        list.add(DyeColor.PURPLE);
        list.add(DyeColor.BLUE);
        list.add(DyeColor.BROWN);
        list.add(DyeColor.GREEN);
        list.add(DyeColor.RED);
        list.add(DyeColor.BLACK);
        return list;
    }

    static boolean isModdedDye(DyeColor dyeColor){
        return !vanillaDyeColors().contains(dyeColor);
    }

}
