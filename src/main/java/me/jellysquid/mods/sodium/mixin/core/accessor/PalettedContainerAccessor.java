package me.jellysquid.mods.sodium.mixin.core.accessor;

import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.world.chunk.Palette;
import net.minecraft.world.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PalettedContainer.class)
public interface PalettedContainerAccessor {
    @Accessor("data")
    PackedIntegerArray getData();

    @Accessor("palette")
    <T> Palette<T> getPalette();
}
