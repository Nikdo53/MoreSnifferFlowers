package net.abraxator.moresnifferflowers.items;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;

import java.util.Map;

public class WandOfCubingItem extends Item {
    public WandOfCubingItem(Properties properties) {
        super(properties);
    }
    public InteractionResult useOn(UseOnContext context) {
        var pos = context.getClickedPos().getCenter();
        var r = 5;
        float angle = Mth.PI / 4;

        Map<Integer, BlockState> map = Util.make(Maps.newLinkedHashMap(), o -> {
            o.put(0, Blocks.WHITE_WOOL.defaultBlockState());
            o.put(1, Blocks.GRAY_WOOL.defaultBlockState());
            o.put(2, Blocks.BROWN_WOOL.defaultBlockState());
            o.put(3, Blocks.GREEN_WOOL.defaultBlockState());
            o.put(4, Blocks.YELLOW_WOOL.defaultBlockState());
            o.put(5, Blocks.RED_WOOL.defaultBlockState());
        });

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    if(!shouldRemoveBlock(x, y, z, r)) {
                        Vector3d vec3 = new Vector3d(x, y, z);
                        //vec3.rotateX(angle);
                        //vec3.rotateZ(angle);
                        //vec3.rotateY(angle);
                        context.getLevel().setBlock(new BlockPos(
                                        (int) (pos.x + vec3.x),
                                        (int) (pos.y + vec3.y),
                                        (int) (pos.z + vec3.z)),
                                map.get(Math.min(Math.min(Mth.abs(x), Mth.abs(y)), Mth.abs(z))), 3);
                    }
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    private boolean isOnEdge(int x, int y, int z, int r) {
        int absX = Mth.abs(x);
        int absY = Mth.abs(y);
        int absZ = Mth.abs(z);

        return
                absX != r && absY == r && absZ != r ||
                        absX == r && absY != r && absZ != r ||
                        absX != r && absY != r && absZ == r;
    }

    private boolean shouldRemoveBlock(int x, int y, int z, int r) {
        if(y == 0) {
            return Mth.abs(x) == r && Mth.abs(z) == r;
        } else if(Mth.abs(y) == 1) {
            return Mth.abs(x) == r || Mth.abs(z) == r;
        }

        if(x == 0) {
            return Mth.abs(y) == r && Mth.abs(z) == r;
        } else if(Mth.abs(x) == 1) {
            return Mth.abs(y) == r || Mth.abs(z) == r;
        }

        if(z == 0) {
            return Mth.abs(y) == r && Mth.abs(x) == r;
        } else if(Mth.abs(z) == 1) {
            return Mth.abs(y) == r || Mth.abs(x) == r;
        }

        return false;
    }
}
