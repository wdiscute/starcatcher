package com.wdiscute.starcatcher.shaders;

import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;

import java.util.function.UnaryOperator;

public class BakedModelRemapper
{

    public static BakedQuad remapQuad(BakedQuad original, TextureAtlasSprite sprite, UnaryOperator<BakedQuad.MaterialInfo> materialInfoRemapper) {
        long[] uvs = new long[4];
        for (int i = 0; i < 4; i++) {
            long packedUv = original.packedUV(i);
            float u = UVPair.unpackU(packedUv);
            float v = UVPair.unpackV(packedUv);

            float newU = remapU(u, sprite);
            float newV = remapV(v, sprite);
            uvs[i] = UVPair.pack(newU, newV);
        }
        BakedQuad.MaterialInfo newMaterialInfo = materialInfoRemapper.apply(original.materialInfo());
        return new BakedQuad(original.position0(), original.position1(), original.position2(), original.position3(), uvs[0], uvs[1], uvs[2], uvs[3], original.direction(), newMaterialInfo, original.bakedNormals(), original.bakedColors());
    }

    public static float remapU(float atlasU, TextureAtlasSprite sprite)
    {
        return (atlasU - sprite.getU0()) / (sprite.getU1() - sprite.getU0());
    }

    public static float remapV(float atlasV, TextureAtlasSprite sprite)
    {
        return (atlasV - sprite.getV0()) / (sprite.getV1() - sprite.getV0());
    }

}
