package com.wdiscute.starcatcher.shaders;

import org.joml.Vector3f;

public class GoldShader {

    public static final float HIGHLIGHT_LIMIT = 0.75f;
    public static final float SHADOW_LIMIT = 0.40f;
    public static final float[] GOLD_COLOR = new float[]{230f / 255f, 190f / 255f, 33f / 255f};


    // asked claude to port the original shader to java because my manual port sucked
    public static int recolorGold(int abgr) {
        int a  =  (abgr >> 24) & 0xFF;
        int r8 =   abgr        & 0xFF;        // R is lowest byte
        int g8 =  (abgr >> 8)  & 0xFF;
        int b8 =  (abgr >> 16) & 0xFF;        // B is third byte

        float r = r8 / 255f;
        float g = g8 / 255f;
        float b = b8 / 255f;

        float[] hsv = rgb2hsv(r, g, b);
        hsv[1] *= 0.20f;
        float[] rgb = hsv2rgb(hsv[0], hsv[1], hsv[2]);

        float rr = rgb[0] * GOLD_COLOR[0];
        float rg = rgb[1] * GOLD_COLOR[1];
        float rb = rgb[2] * GOLD_COLOR[2];

        float value = hsv[2];

        if (value > HIGHLIGHT_LIMIT) {
            float highlight = (value - HIGHLIGHT_LIMIT) * (1f / HIGHLIGHT_LIMIT);
            float t = highlight * 3f;
            rr = mix(rr, 1.0f, t);
            rg = mix(rg, 0.9f, t);
            rb = mix(rb, 0.7f, t);
        }

        if (value < SHADOW_LIMIT) {
            float shadow = (SHADOW_LIMIT - value) * (1f / SHADOW_LIMIT);
            float t = shadow * 2f;
            rr = mix(rr, 0.3f, t);
            rg = mix(rg, 0.1f, t);
            rb = mix(rb, 0.0f, t);
        }

        int outR = Math.round(clamp(rr, 0f, 1f) * 255f);
        int outG = Math.round(clamp(rg, 0f, 1f) * 255f);
        int outB = Math.round(clamp(rb, 0f, 1f) * 255f);

        // pack back as ABGR
        return (a << 24) | (outB << 16) | (outG << 8) | outR;
    }

    private static float clamp(float x, float lo, float hi) {
        return x < lo ? lo : (Math.min(x, hi));
    }

    private static float fract(float x) {
        return x - (float) Math.floor(x);
    }

    private static float mix(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static float[] rgb2hsv(float r, float g, float b) {
        final float Kx = 0f, Ky = -1f / 3f, Kz = 2f / 3f, Kw = -1f;

        float s1 = (g >= b) ? 1f : 0f;         // step(c.b, c.g)
        float px = mix(b,  g,  s1);
        float qy = mix(g,  b,  s1);
        float pz = mix(Kw, Kx, s1);
        float pw = mix(Kz, Ky, s1);

        float s2 = (r >= px) ? 1f : 0f;        // step(p.x, c.r)
        float v = mix(px, r,  s2);// mix(py, py, s2)
        float qz = mix(pw, pz, s2);
        float qw = mix(r,  px, s2);

        float d = v - Math.min(qw, qy);
        float e = 1.0e-10f;
        float h = Math.abs(qz + (qw - qy) / (6f * d + e));
        float s = d / (v + e);
        return new float[]{ h, s, v };
    }

    private static float[] hsv2rgb(float h, float s, float v) {
        final float Kx = 1f, Ky = 2f / 3f, Kz = 1f / 3f, Kw = 3f;

        float px = Math.abs(fract(h + Kx) * 6f - Kw);
        float py = Math.abs(fract(h + Ky) * 6f - Kw);
        float pz = Math.abs(fract(h + Kz) * 6f - Kw);

        float r = v * mix(1f, clamp(px - 1f, 0f, 1f), s);
        float g = v * mix(1f, clamp(py - 1f, 0f, 1f), s);
        float b = v * mix(1f, clamp(pz - 1f, 0f, 1f), s);
        return new float[]{ r, g, b };
    }
}
