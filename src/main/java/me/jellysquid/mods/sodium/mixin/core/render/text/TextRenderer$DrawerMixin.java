package me.jellysquid.mods.sodium.mixin.core.render.text;

import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextRenderer.Drawer.class)
public abstract class TextRenderer$DrawerMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private static float fixTextHeight(float y) {
        return y + 3;
    }
}
