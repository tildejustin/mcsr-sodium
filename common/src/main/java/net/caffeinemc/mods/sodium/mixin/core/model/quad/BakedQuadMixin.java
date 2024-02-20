package net.caffeinemc.mods.sodium.mixin.core.model.quad;

import net.caffeinemc.mods.sodium.client.model.quad.BakedQuadView;
import net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFlags;
import net.caffeinemc.mods.sodium.client.util.ModelQuadUtil;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BakedQuad.class)
public class BakedQuadMixin implements BakedQuadView {
    @Shadow
    @Final
    protected int[] vertices;

    @Shadow
    @Final
    protected TextureAtlasSprite sprite;

    @Shadow
    @Final
    protected int tintIndex;

    @Shadow
    @Final
    protected Direction direction; // This is really the light face, but we can't rename it.

    @Shadow
    @Final
    private boolean shade;

    @Unique
    private int flags;

    @Unique
    private int normal;

    @Unique
    private ModelQuadFacing normalFace = ModelQuadFacing.UNASSIGNED;

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = { "<init>", "<init>([IILnet/minecraft/core/Direction;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZZ)V" }, at = @At("RETURN"), require = 1)
    private void init(int[] vertexData, int colorIndex, Direction face, TextureAtlasSprite sprite, boolean shade, CallbackInfo ci) {
        this.normal = this.calculateNormal();
        this.normalFace = ModelQuadFacing.fromPackedNormal(this.normal);

        this.flags = ModelQuadFlags.getQuadFlags(this, face);
    }

    @Override
    public float getX(int idx) {
        return Float.intBitsToFloat(this.vertices[ModelQuadUtil.vertexOffset(idx) + ModelQuadUtil.POSITION_INDEX]);
    }

    @Override
    public float getY(int idx) {
        return Float.intBitsToFloat(this.vertices[ModelQuadUtil.vertexOffset(idx) + ModelQuadUtil.POSITION_INDEX + 1]);
    }

    @Override
    public float getZ(int idx) {
        return Float.intBitsToFloat(this.vertices[ModelQuadUtil.vertexOffset(idx) + ModelQuadUtil.POSITION_INDEX + 2]);
    }

    @Override
    public int getColor(int idx) {
        return this.vertices[ModelQuadUtil.vertexOffset(idx) + ModelQuadUtil.COLOR_INDEX];
    }

    @Override
    public TextureAtlasSprite getSprite() {
        return this.sprite;
    }

    @Override
    public float getTexU(int idx) {
        return Float.intBitsToFloat(this.vertices[ModelQuadUtil.vertexOffset(idx) + ModelQuadUtil.TEXTURE_INDEX]);
    }

    @Override
    public float getTexV(int idx) {
        return Float.intBitsToFloat(this.vertices[ModelQuadUtil.vertexOffset(idx) + ModelQuadUtil.TEXTURE_INDEX + 1]);
    }

    @Override
    public int getFlags() {
        return this.flags;
    }

    @Override
    public int getColorIndex() {
        return this.tintIndex;
    }

    @Override
    public ModelQuadFacing getNormalFace() {
        return this.normalFace;
    }

    @Override
    public int getNormal() {
        return this.normal;
    }

    @Override
    public Direction getLightFace() {
        return this.direction;
    }

    @Override
    @Unique(silent = true) // The target class has a function with the same name in a remapped environment
    public boolean hasShade() {
        return this.shade;
    }
}
