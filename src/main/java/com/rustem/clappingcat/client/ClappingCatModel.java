package com.rustem.clappingcat.client;

import com.rustem.clappingcat.entity.ClappingCatEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ClappingCatModel<T extends ClappingCatEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            new ResourceLocation("clappingcat", "clapping_cat"), "main");
    private final ModelPart root, body, head, leftArm, rightArm, leftLeg, rightLeg, tail;

    public ClappingCatModel(ModelPart root) {
        this.root = root;
        body = root.getChild("body");
        head = body.getChild("head");
        leftArm = body.getChild("left_arm");
        rightArm = body.getChild("right_arm");
        leftLeg = body.getChild("left_leg");
        rightLeg = body.getChild("right_leg");
        tail = body.getChild("tail");
    }

    // Texture layout (64x64) – every box has its own non-overlapping UV area:
    // body (0,0) | head (0,18) | arms (32,0)/(44,0) | legs (32,11)/(44,11)
    // ears (32,24)/(44,24) | tail (56,0)
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        // body pivot y=13 -> legs end exactly on y=24 (ground level of entity models)
        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-5, -6, -3, 10, 12, 6),
                PartPose.offset(0, 13, 0));
        body.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 18).addBox(-4, -8, -4, 8, 8, 8)
                .texOffs(32, 24).addBox(-4, -12, -3, 3, 4, 3)
                .texOffs(44, 24).addBox(1, -12, -3, 3, 4, 3),
                PartPose.offset(0, -5, -1));
        body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 0)
                .addBox(-1, -1, -1, 3, 8, 3), PartPose.offset(5, -3, -1));
        body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(44, 0)
                .addBox(-2, -1, -1, 3, 8, 3), PartPose.offset(-5, -3, -1));
        body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 11)
                .addBox(-1, 0, -1, 3, 6, 3), PartPose.offset(3, 5, 1));
        body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(44, 11)
                .addBox(-2, 0, -1, 3, 6, 3), PartPose.offset(-3, 5, 1));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(56, 0)
                .addBox(-1, -1, -1, 2, 9, 2), PartPose.offset(0, 4, 3));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        head.xRot = headPitch * ((float) Math.PI / 180F);

        // Real clap: arms are raised forward and the hands swing together / apart.
        // Idle = slow lazy clap, aggressive (chasing / hitting) = frantic clap.
        boolean angry = entity.isAggressive();
        float speed = angry ? 1.5F : 0.5F;
        float t = Mth.sin(age * speed) * 0.5F + 0.5F;     // 0..1
        float clap = t * t * (3.0F - 2.0F * t);            // smoothstep -> sharp "slap" feel
        float armUp = angry ? -1.40F : -1.05F;
        leftArm.xRot = armUp;
        rightArm.xRot = armUp;
        leftArm.zRot = 0.0F;
        rightArm.zRot = 0.0F;
        leftArm.yRot = Mth.lerp(clap, -0.30F, 0.52F);      // left arm: positive = towards the centre
        rightArm.yRot = -leftArm.yRot;                      // mirrored

        leftLeg.xRot = Mth.cos(limbSwing * 0.7F) * limbSwingAmount * 0.9F;
        rightLeg.xRot = Mth.cos(limbSwing * 0.7F + (float) Math.PI) * limbSwingAmount * 0.9F;
        tail.xRot = 0.4F + Mth.sin(age * 0.08F) * 0.15F;
        tail.yRot = Mth.sin(age * 0.05F) * 0.2F;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
