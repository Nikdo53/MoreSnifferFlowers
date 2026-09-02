package net.abraxator.moresnifferflowers.client.model.block;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class DyespriaModel {
    public static final ModelLayerLocation DYESPRIA = new ModelLayerLocation(MoreSnifferFlowers.loc("dyespria"), "main");

    private final ModelPart root;
    private final ModelPart bone;
    private final ModelPart bone3;
    private final ModelPart bone4;
    private final ModelPart bone2;

    public DyespriaModel(ModelPart root) {
        this.root = root.getChild("root");
        this.bone = this.root.getChild("bone");
        this.bone3 = this.root.getChild("bone3");
        this.bone4 = this.root.getChild("bone4");
        this.bone2 = this.root.getChild("bone2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(10, 13).addBox(-0.5F, 2.5F, 3.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, -3.5F));

        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, -2.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.2F, -0.525F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 9).addBox(-4.005F, -1.725F, -0.15F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.9126F, -3.3183F, 1.9635F, 0.0F, 0.0F));

        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -0.5F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone3 = root.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0017F, -0.7916F, 3.48F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r4 = bone3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, -2.0F, 0.35F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0017F, 0.5916F, 0.62F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r5 = bone3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 9).addBox(-4.005F, -1.7346F, -0.1731F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0017F, -1.146F, -1.7982F, 1.9635F, 0.0F, 0.0F));

        PartDefinition cube_r6 = bone3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -0.5F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0017F, 0.7916F, 1.52F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone4 = root.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0017F, -0.7916F, 3.48F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r7 = bone4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 13).mirror().addBox(-2.0F, -2.0F, 0.375F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0017F, 0.5916F, 0.62F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r8 = bone4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 9).mirror().addBox(-3.995F, -1.7346F, -0.1731F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0017F, -1.146F, -1.7982F, 1.9635F, 0.0F, 0.0F));

        PartDefinition cube_r9 = bone4.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -4.0F, -0.5F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0017F, 0.7916F, 1.52F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone2 = root.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(-0.0267F, -0.7916F, 8.52F));

        PartDefinition cube_r10 = bone2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 13).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0017F, 1.2987F, -1.02F, -3.1416F, 0.0F, -2.3562F));

        PartDefinition cube_r11 = bone2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 9).addBox(-4.0F, -1.5F, -0.5F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0033F, -1.3582F, 2.1401F, -1.1781F, 0.0F, 3.1416F));

        PartDefinition cube_r12 = bone2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -0.5F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0017F, 0.7916F, -1.52F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }


}
