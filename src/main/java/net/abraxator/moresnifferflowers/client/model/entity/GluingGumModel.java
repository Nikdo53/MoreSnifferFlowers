package net.abraxator.moresnifferflowers.client.model.entity;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class GluingGumModel {
	public static final ModelLayerLocation GLUING_GUM = new ModelLayerLocation(MoreSnifferFlowers.loc("gluing_gum"), "main");

	private final ModelPart root;

	public GluingGumModel(ModelPart root) {
        this.root = root.getChild("root");
	}
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 0.0F, -10.0F, 20.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 31).addBox(-14.5F, -5.5F, 0.0F, 29.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0429F, -5.5F, 0.1464F, 0.0F, -2.3562F, 0.0F));

		PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 20).addBox(-14.0F, -10.5F, 0.25F, 29.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1339F, -0.5F, -0.3839F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

}