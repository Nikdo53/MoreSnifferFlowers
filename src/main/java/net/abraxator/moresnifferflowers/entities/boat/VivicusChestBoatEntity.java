package net.abraxator.moresnifferflowers.entities.boat;

import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.MSFAdvancementCritters;
import net.abraxator.moresnifferflowers.init.MSFEntityTypes;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class VivicusChestBoatEntity extends ChestBoat implements ColorableVivicusBlock {
    private static final EntityDataAccessor<Integer> COLOR_DATA = SynchedEntityData.defineId(VivicusChestBoatEntity.class, EntityDataSerializers.INT);
    
    public VivicusChestBoatEntity(EntityType<? extends ChestBoat> entityType, Level level, Supplier<Item> itemSupplier) {
        super(entityType, level, itemSupplier);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOR_DATA, DyeColor.WHITE.getId());
    }

    public void setColor(DyeColor color) {
        this.entityData.set(COLOR_DATA, color.getId());
    }

    public DyeColor getColor() {
        return Dye.colorFromId(this.entityData.get(COLOR_DATA));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        var dyespria = player.getMainHandItem();
        if (dyespria.is(MSFItems.DYESPRIA)) {
            var dye = Dye.getDyeFromDyespria(dyespria);
            int uses = DyespriaItem.getDyespriaUses(dyespria);
            int dyeCount;

            if(uses <= 0) {
                dyeCount = dye.amount() - 1;
                DyespriaItem.setDyespriaUses(dyespria, 4);
            } else {
                dyeCount = dye.amount();
                DyespriaItem.setDyespriaUses(dyespria, uses);
            }

            this.setColor(dye.color());
            var stack = Dye.stackFromDye(new Dye(dye.color(), dyeCount));
            Dye.setDyeToDyeHolderStack(dyespria, stack, stack.getCount());

            if(player instanceof ServerPlayer serverPlayer) {
                MSFAdvancementCritters.DYE_BOAT.get().trigger(serverPlayer);
            }

            if(this.level().isClientSide()) {
                particles(this.random, this.level(), dye, BlockPos.containing(this.position()));
            }

            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand, location);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Color", this.getColor().getId());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        this.setColor(Dye.colorFromId(tag.getIntOr("Color", 0)));
    }
}
