package net.abraxator.moresnifferflowers.client.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.init.config.MSFClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import net.neoforged.neoforge.client.model.ao.EnhancedBlockModelLighter;
import net.neoforged.neoforge.client.model.quad.BakedColors;
import net.neoforged.neoforge.client.model.quad.BakedNormals;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockPatternRenderer {
    public static void renderAll(AddSectionGeometryEvent.@NotNull SectionRenderingContext context, Set<BlockPatternQuad> quads) {
        boolean isTransparent = MSFClientConfig.CLIENT_CONFIG.isLoaded() && MSFClientConfig.BLOCK_PATTERN_TRANSPARENCY.get();

        PoseStack poseStack = new PoseStack();

        for (BlockPatternQuad quad : quads) {
            BlockPos pos = quad.pos();
            poseStack.pushPose();
            poseStack.translate(SectionPos.sectionRelative(pos.getX()), SectionPos.sectionRelative(pos.getY()), SectionPos.sectionRelative(pos.getZ()));

            quad.render(poseStack, context.getOrCreateChunkBuffer(isTransparent ? ChunkSectionLayer.TRANSLUCENT : ChunkSectionLayer.CUTOUT), context);

            poseStack.popPose();
        }
    }

    public static boolean isInsideSection(BlockPos origin, BlockPos pos) {
        return SectionPos.of(origin).equals(SectionPos.of(pos));
    }

    public static Set<BlockPatternQuad> cache(Level level, BlockPos origin) {
        Set<BlockPatternQuad> quads = new HashSet<>();

        Map<BlockPos, BlockPatternCapability.PatternData> patterns = BlockPatternCapability.getPatterns(level, origin);
        for (BlockPos pos : patterns.keySet()) {
            if (!isInsideSection(origin, pos) ) continue;

            BlockPatternCapability.PatternData data = patterns.get(pos);

            Identifier Identifier = MoreSnifferFlowers.loc("block/block_pattern/" + BlockPattern.fromId(data.patternId()).getSerializedName());
            TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(Identifier);
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) continue;

            for (Direction dir : Direction.values()) {
                BlockPos relativePos = pos.relative(dir);
                BlockState relativeState = level.getBlockState(relativePos);

                boolean faceSturdy = state.isFaceSturdy(level, pos, dir);
                boolean notBlocked = !relativeState.isFaceSturdy(level, relativePos, dir.getOpposite());
                boolean noOcclusion = !relativeState.canOcclude();

                boolean canRenderFace = faceSturdy && (notBlocked || noOcclusion);
                if (!canRenderFace) continue;


                quads.add(new BlockPatternQuad(pos, dir, data.color(), sprite, MSFClientConfig.BLOCK_PATTERN_SMOOTH_LIGHTING.get(), data.direction(), data.isGlowing()));
            }
        }
        return quads;
    }


    public record BlockPatternQuad(BlockPos pos, Direction direction, int color, TextureAtlasSprite sprite, boolean smoothLighting, Direction rotation, boolean isGlowing) {
        private void render(PoseStack poseStack, VertexConsumer buffer, AddSectionGeometryEvent.SectionRenderingContext context) {
            poseStack.pushPose();

            int rgb = color;
            int r = ((rgb >> 16) & 0xFF) ;
            int g = ((rgb >> 8) & 0xFF) ;
            int b = (rgb & 0xFF);

            Matrix4f pose = poseStack.last().pose();

            /*  if (direction == Direction.UP) {
                   r = 1F;
                   g = 1F;
                   b = 1F;
               }*/

            float u0 = sprite.getU1();
            float u1 = sprite.getU0();
            float u2 = sprite.getU0();
            float u3 = sprite.getU1();

            float v0 = sprite.getV1();
            float v1 = sprite.getV1();
            float v2 = sprite.getV0();
            float v3 = sprite.getV0();

            if (rotation == Direction.EAST) {
                u0 = sprite.getU0();
                u1 = sprite.getU0();
                u2 = sprite.getU1();
                u3 = sprite.getU1();
                v0 = sprite.getV0();
                v1 = sprite.getV1();
                v2 = sprite.getV1();
                v3 = sprite.getV0();
            }
            if (rotation == Direction.SOUTH) {
                u0 = sprite.getU0();
                u1 = sprite.getU1();
                u2 = sprite.getU1();
                u3 = sprite.getU0();
                v0 = sprite.getV0();
                v1 = sprite.getV0();
                v2 = sprite.getV1();
                v3 = sprite.getV1();
            }
            if (rotation == Direction.WEST) {
                u0 = sprite.getU1();
                u1 = sprite.getU1();
                u2 = sprite.getU0();
                u3 = sprite.getU0();
                v0 = sprite.getV1();
                v1 = sprite.getV0();
                v2 = sprite.getV0();
                v3 = sprite.getV1();
            }

            boolean translucencyEnabled = MSFClientConfig.BLOCK_PATTERN_TRANSPARENCY.get();



            Vector3f[] vertices = faceVertices(direction);
            ChunkSectionLayer layer = translucencyEnabled ? ChunkSectionLayer.TRANSLUCENT : ChunkSectionLayer.CUTOUT;
            RenderType itemRenderType = translucencyEnabled ? RenderTypes.cutoutMovingBlock() : RenderTypes.translucentMovingBlock();


            BakedQuad quad = new BakedQuad(
                    vertices[0], vertices[1], vertices[2], vertices[3],
                    UVPair.pack(u1, v2), UVPair.pack(u2, v1), UVPair.pack(u3, v0), UVPair.pack(u0, v3),
                    direction,
                    new BakedQuad.MaterialInfo(
                            sprite,
                            layer,
                            itemRenderType,
                            -1, true, isGlowing ? 15 : 0, MSFClientConfig.BLOCK_PATTERN_SMOOTH_LIGHTING.getAsBoolean()
                    ),
                    BakedNormals.UNSPECIFIED,
                    BakedColors.of(ARGB.color(255, r, g , b))
            );

            QuadInstance quadInstance = new QuadInstance();
            BlockModelLighter blockModelLighter = EnhancedBlockModelLighter.newInstance();
            blockModelLighter.prepareQuadAmbientOcclusion(Minecraft.getInstance().level, Blocks.DIAMOND_BLOCK.defaultBlockState(), pos, quad, quadInstance);
            blockModelLighter.reset();

            buffer.putBakedQuad(poseStack.last(), quad, quadInstance);

            poseStack.popPose();
        }
    }

    private static Vector3f[] faceVertices(Direction face) {
        return switch (face) {
            case DOWN -> new Vector3f[]{
                    new Vector3f(0, 0, 1), new Vector3f(0, 0, 0),
                    new Vector3f(1, 0, 0), new Vector3f(1, 0, 1)
            };
            case UP -> new Vector3f[]{
                    new Vector3f(0, 1, 0), new Vector3f(0, 1, 1),
                    new Vector3f(1, 1, 1), new Vector3f(1, 1, 0)
            };
            case NORTH -> new Vector3f[]{
                    new Vector3f(0, 0, 0), new Vector3f(0, 1, 0),
                    new Vector3f(1, 1, 0), new Vector3f(1, 0, 0)
            };
            case SOUTH -> new Vector3f[]{
                    new Vector3f(1, 0, 1), new Vector3f(1, 1, 1),
                    new Vector3f(0, 1, 1), new Vector3f(0, 0, 1)
            };
            case WEST -> new Vector3f[]{
                    new Vector3f(0, 0, 1), new Vector3f(0, 1, 1),
                    new Vector3f(0, 1, 0), new Vector3f(0, 0, 0)
            };
            case EAST -> new Vector3f[]{
                    new Vector3f(1, 0, 0), new Vector3f(1, 1, 0),
                    new Vector3f(1, 1, 1), new Vector3f(1, 0, 1)
            };
        };
    }

    public static int getPackedLight(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightCoordsUtil.pack(blockLight, skyLight);
    }
}

