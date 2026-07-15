package com.wdiscute.starcatcher.shaders;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BakedModelRemapper {
    // maybe this should be canceled for models that aren't SimpleBakedModel?
    public static Map<BakedModel, RemappedBakedModel> REMAPPED_MODELS = new HashMap<>();

    public static BakedModel getOrCreate(BakedModel original) {
        return REMAPPED_MODELS.computeIfAbsent(original, BakedModelRemapper::remapModel);
    }

    public static RemappedBakedModel remapModel(BakedModel original) {
        RandomSource random = RandomSource.create();
        random.setSeed(42L);

        Map<Direction, List<BakedQuad>> culledFaces = new HashMap<>();
        List<BakedQuad> unculledFaces = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            List<BakedQuad> culled = culledFaces.computeIfAbsent(direction, d -> new ArrayList<>());

            for (BakedQuad quad : original.getQuads(null, direction, random)) {
                culled.add(remapQuad(quad, original.getParticleIcon()));
            }
        }

        for (BakedQuad quad : original.getQuads(null, null, random)) {
            unculledFaces.add(remapQuad(quad, original.getParticleIcon()));
        }

        return new RemappedBakedModel(original, unculledFaces, culledFaces);
    }

    public static BakedQuad remapQuad(BakedQuad original, TextureAtlasSprite sprite) {
        int[] vertexData = original.getVertices().clone();

        // Vertex format: X Y Z Normal U V (UV at indices 4,5 per vertex, stride 8 ints)
        for (int i = 0; i < 4; i++) {
            int base = i * 8;
            float u = Float.intBitsToFloat(vertexData[base + 4]);
            float v = Float.intBitsToFloat(vertexData[base + 5]);

            vertexData[base + 4] = Float.floatToRawIntBits(remapU(u, sprite));
            vertexData[base + 5] = Float.floatToRawIntBits(remapV(v, sprite));
        }

        return new BakedQuad(vertexData, original.getTintIndex(),
                original.getDirection(), original.getSprite(), original.isShade());
    }

    public static float remapU(float atlasU, TextureAtlasSprite sprite) {
        return (atlasU - sprite.getU0()) / (sprite.getU1() - sprite.getU0());
    }

    public static float remapV(float atlasV, TextureAtlasSprite sprite) {
        return (atlasV - sprite.getV0()) / (sprite.getV1() - sprite.getV0());
    }

    public static class RemappedBakedModel extends BakedModelWrapper<BakedModel> {
        public final List<BakedQuad> unculledFaces;
        public final Map<Direction, List<BakedQuad>> culledFaces;

        public RemappedBakedModel(BakedModel original, List<BakedQuad> unculledFaces, Map<Direction, List<BakedQuad>> culledFaces) {
            super(original);
            this.unculledFaces = unculledFaces;
            this.culledFaces = culledFaces;
        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource rand) {
            return direction == null ? this.unculledFaces : this.culledFaces.get(direction);
        }
    }
}
