package net.abraxator.moresnifferflowers.client.model.entity;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.renderer.entity.SaltBubbleRenderer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class SaltBubbleModel extends Model<SaltBubbleRenderer.State> {
    public static final ModelLayerLocation SALT_BUBBLE = new ModelLayerLocation(MoreSnifferFlowers.loc("salt_bubble"), "main");

    public SaltBubbleModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        ModelPart root1 = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 17).addBox(-5.0F, 5.0F, -5.0F, 10.0F, -10.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));


        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
