package net.abraxator.moresnifferflowers.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

public class DragonflyModel extends Model {
	public static final ModelLayerLocation DRAGONFLY = new ModelLayerLocation(MoreSnifferFlowers.loc("dragonfly"), "main");

	private final ModelPart root;
	private final ModelPart wing1;
	private final ModelPart wing2;

	public DragonflyModel(ModelPart root) {
		super(root, RenderTypes::armorCutoutNoCull);
		this.root = root.getChild("root");
		this.wing1 = this.root.getChild("wing1");
		this.wing2 = this.root.getChild("wing2");
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, -6.0F, 3.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 4).addBox(-0.5F, 0.0F, -1.5F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0303F, -7.1768F, -6.5F, 0.0F, 0.0F, -2.3562F));

		PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 6).addBox(0.0F, 0.25F, -1.5F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -7.0F, -6.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r3 = root.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(10, 0).addBox(-1.0F, -1.0F, -1.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 4.4F, 0.0F, -0.7854F, 0.0F));

		PartDefinition wing1_r1 = root.addOrReplaceChild("wing1", CubeListBuilder.create().texOffs(0, 11).mirror().addBox(-4.5F, -8.5F, 0.0F, 9.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0544F, -6.9982F, -0.5F, 1.7975F, -1.5708F, -2.8975F));

		PartDefinition wing2_r1 = root.addOrReplaceChild("wing2", CubeListBuilder.create().texOffs(0, 11).addBox(-5.0F, -8.0F, 0.0F, 9.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -7.225F, -1.0F, 1.7975F, 1.5708F, 2.8975F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void animate(float partialTick) {
		this.wing1.zRot = (float) (Mth.cos(partialTick * 100F * Mth.PI / 180.0F) * Mth.PI * 0.25);
		this.wing2.zRot = (float) -(Mth.cos(partialTick * 100F * Mth.PI / 180.0F) * Mth.PI * 0.25);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int pColor) {
		root.render(poseStack, buffer, packedLight, packedOverlay, pColor);
	}
}