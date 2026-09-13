package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.*;
import net.abraxator.moresnifferflowers.entities.boat.VivicusBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.VivicusChestBoatEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.Supplier;

public interface MSFEntityTypes {
    DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MoreSnifferFlowers.MOD_ID);

    DeferredHolder<EntityType<?>, EntityType<BoblingEntity>> BOBLING =
            register("bobling", makeBuilder(BoblingEntity::new, MobCategory.CREATURE, 0.375F, 0.8125F));

    DeferredHolder<EntityType<?>, EntityType<DragonflyProjectile>> DRAGONFLY = 
            register("dragonfly", makeBuilder(DragonflyProjectile::new, MobCategory.MISC, 0.21875F, 0.21875F));

    DeferredHolder<EntityType<?>, EntityType<CorruptedProjectile>> CORRUPTED_SLIME_BALL = 
            register("corrupted_slime_ball", makeBuilder(CorruptedProjectile::new, MobCategory.MISC, 0.25F, 0.25F));

    DeferredHolder<EntityType<?>, EntityType<Boat>> CORRUPTED_BOAT =
            register("mod_corrupted_boat", withItemBuilder(Boat::new, MSFItems.CORRUPTED_BOAT, MobCategory.MISC, 1.375f, 0.5625f));

    DeferredHolder<EntityType<?>, EntityType<ChestBoat>> CORRUPTED_CHEST_BOAT =
            register("mod_corrupted_chest_boat", withItemBuilder(ChestBoat::new, MSFItems.CORRUPTED_CHEST_BOAT, MobCategory.MISC, 1.375f, 0.5625f));

    DeferredHolder<EntityType<?>, EntityType<VivicusBoatEntity>> VIVICUS_BOAT =
            register("mod_vivicus_boat", withItemBuilder(VivicusBoatEntity::new, MSFItems.VIVICUS_BOAT, MobCategory.MISC, 1.375f, 0.5625f));

    DeferredHolder<EntityType<?>, EntityType<VivicusChestBoatEntity>> VIVICUS_CHEST_BOAT =
            register("mod_vivicus_chest_boat", withItemBuilder(VivicusChestBoatEntity::new, MSFItems.VIVICUS_CHEST_BOAT, MobCategory.MISC, 1.375f, 0.5625f));

    DeferredHolder<EntityType<?>, EntityType<JarOfAcidProjectile>> JAR_OF_ACID =
            register("jar_of_acid", makeBuilder(JarOfAcidProjectile::new, MobCategory.MISC, 0.25F, 0.25F));

    DeferredHolder<EntityType<?>, EntityType<SaltBubbleProjectile>> SALT_BUBBLE =
            register("salt_bubble", makeBuilder(SaltBubbleProjectile::new, MobCategory.MISC, 0.7F, 0.7F));

    DeferredHolder<EntityType<?>, EntityType<SaltProjectile>> SALT_PROJECTILE =
            register("salt_projectile", makeBuilder(SaltProjectile::new, MobCategory.MISC, 0.25F, 0.25F));

    DeferredHolder<EntityType<?>, EntityType<GluingGumEntity>> GLUING_GUM_ENTITY =
            register("gluing_gum_entity", makeBuilder(GluingGumEntity::new, MobCategory.MISC, 0.25F, 0.25F));
    
    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> register(String id, EntityType.Builder<E> builder) {
        return ENTITIES.register(id, () -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, MoreSnifferFlowers.loc(id))));
    }

    private static <E extends Mob> DeferredHolder<EntityType<?>, EntityType<E>> registerWithEgg(String id, EntityType.Builder<E> builder, int primary, int secondary) {
        DeferredHolder<EntityType<?>, EntityType<E>> ret = ENTITIES.register(id, () -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, MoreSnifferFlowers.loc(id))));
        MSFItems.ITEMS.registerItem(id + "_spawn_egg", SpawnEggItem::new, properties -> properties.spawnEgg(ret.get()));
        return ret;
    }
    
    private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height) {
        return EntityType.Builder.of(factory, classification).
                sized(width, height);
    }

    private static <E extends Entity> EntityType.Builder<E> withItemBuilder(TriFunction<EntityType<E>, Level, Supplier<Item>, E> factory, Supplier<Item> item, MobCategory classification, float width, float height) {
        return makeBuilder((type, level) -> factory.apply(type, level, item), classification, width, height);
    }
}
