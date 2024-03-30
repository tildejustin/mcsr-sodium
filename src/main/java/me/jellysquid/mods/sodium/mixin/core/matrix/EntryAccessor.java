package me.jellysquid.mods.sodium.mixin.core.matrix;

import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MatrixStack.Entry.class)
public interface EntryAccessor {
    @Accessor("canSkipNormalization")
    boolean canSkipNormalization();

    @Accessor("canSkipNormalization")
    void setSkipNormalization(boolean skip);
}
