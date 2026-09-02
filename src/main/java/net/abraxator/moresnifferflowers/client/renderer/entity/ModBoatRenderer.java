package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.boat.ModBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.ModChestBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.VivicusBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.VivicusChestBoatEntity;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.stream.Stream;

public class ModBoatRenderer extends BoatRenderer {
    public static final ModelLayerLocation CORRUPTED_BOAT_LAYER = new ModelLayerLocation(MoreSnifferFlowers.loc("boat/corrupted"), "main");
    public static final ModelLayerLocation CORRUPTED_CHEST_BOAT_LAYER = new ModelLayerLocation(MoreSnifferFlowers.loc("chest_boat/corrupted"), "main");
    public static final ModelLayerLocation VIVICUS_BOAT_LAYER = new ModelLayerLocation(MoreSnifferFlowers.loc("boat/vivicus"), "main");
    public static final ModelLayerLocation VIVICUS_CHEST_BOAT_LAYER = new ModelLayerLocation(MoreSnifferFlowers.loc("chest_boat/vivicus"), "main");

    private final Map<ModBoatEntity.Type, Pair<Identifier, ListModel<Boat>>> boatResources;

    public ModBoatRenderer(EntityRendererProvider.Context context, boolean pChestBoat) {
        super(context, pChestBoat);
        this.boatResources = Stream.of(ModBoatEntity.Type.values()).collect(ImmutableMap.toImmutableMap(type -> type,
               type -> Pair.of(Identifier.fromNamespaceAndPath(MoreSnifferFlowers.MOD_ID, getTextureLocation(type, pChestBoat)), this.createBoatModel(context, type, pChestBoat))));    
    }

    @Override
    public void render(Boat entity, float pEntityYaw, float pPartialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - pEntityYaw));
        float f = (float)entity.getHurtTime() - pPartialTicks;
        float f1 = entity.getDamage() - pPartialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float)entity.getHurtDir()));
        }

        float f2 = entity.getBubbleAngle(pPartialTicks);
        if (!Mth.equal(f2, 0.0F)) {
            poseStack.mulPose(new Quaternionf().setAngleAxis(entity.getBubbleAngle(pPartialTicks) * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
        }

        Pair<Identifier, ListModel<Boat>> pair = getModelWithLocation(entity);
        Identifier resourcelocation = pair.getFirst();
        ListModel<Boat> listmodel = pair.getSecond();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        listmodel.setupAnim(entity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = buffer.getBuffer(listmodel.renderType(resourcelocation));
        listmodel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, boatColor(entity));
        if (!entity.isUnderWater()) {
            VertexConsumer vertexconsumer1 = buffer.getBuffer(RenderType.waterMask());
            if (listmodel instanceof WaterPatchModel waterpatchmodel) {
                waterpatchmodel.waterPatch().render(poseStack, vertexconsumer1, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        poseStack.popPose();
    }

    private static String getTextureLocation(ModBoatEntity.Type type, boolean pChestBoat) {
        return pChestBoat ? "textures/entity/chest_boat/" + type.getName() + ".png" : "textures/entity/boat/" + type.getName() + ".png";
    }

    private ListModel<Boat> createBoatModel(EntityRendererProvider.Context context, ModBoatEntity.Type type, boolean pChestBoat) {
        ModelLayerLocation modellayerlocation = pChestBoat ? ModBoatRenderer.createChestBoatModelName(type) : ModBoatRenderer.createBoatModelName(type);
        ModelPart modelpart = context.bakeLayer(modellayerlocation);
        return pChestBoat ? new ChestBoatModel(modelpart) : new BoatModel(modelpart);
    }

    public static ModelLayerLocation createBoatModelName(ModBoatEntity.Type type) {
        return createLocation("boat/" + type.getName(), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(ModBoatEntity.Type type) {
        return createLocation("chest_boat/" + type.getName(), "main");
    }

    private static ModelLayerLocation createLocation(String pPath, String model) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(MoreSnifferFlowers.MOD_ID, pPath), model);
    }

    @Override
    public Pair<Identifier, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if(boat instanceof ModBoatEntity modBoat) {
            return this.boatResources.get(modBoat.getModVariant());
        } else if(boat instanceof ModChestBoatEntity modChestBoatEntity) {
            return this.boatResources.get(modChestBoatEntity.getModVariant());
        } else {
            return null;
        }
    }
    
    private ModBoatEntity.Type getType(Boat boat) {
        if(boat instanceof ModBoatEntity modBoat) {
            return modBoat.getModVariant();
        } else if(boat instanceof ModChestBoatEntity modChestBoatEntity) {
            return modChestBoatEntity.getModVariant();
        } else {
            return null;
        }
    }
    
    private int boatColor(Boat boat) {
        if(this.getType(boat).equals(ModBoatEntity.Type.VIVICUS)) {
            if(boat instanceof VivicusBoatEntity vivicusBoat) {
                return vivicusBoat.colorValues().get(vivicusBoat.getColor());
            } else if(boat instanceof VivicusChestBoatEntity vivicusChestBoat) {
                return vivicusChestBoat.colorValues().get(vivicusChestBoat.getColor());
            }
        }
        
        return -1;
    }
}
