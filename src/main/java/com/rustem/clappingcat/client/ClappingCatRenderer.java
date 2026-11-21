package com.rustem.clappingcat.client;

import com.rustem.clappingcat.ClappingCatMod;
import com.rustem.clappingcat.entity.ClappingCatEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ClappingCatRenderer extends MobRenderer<ClappingCatEntity, ClappingCatModel<ClappingCatEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            ClappingCatMod.MODID, "textures/entity/clapping_cat.png");

    public ClappingCatRenderer(EntityRendererProvider.Context context) {
        super(context, new ClappingCatModel<>(context.bakeLayer(ClappingCatModel.LAYER)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(ClappingCatEntity entity) {
        return TEXTURE;
    }
}
