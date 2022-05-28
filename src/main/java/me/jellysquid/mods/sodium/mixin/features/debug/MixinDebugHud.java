package me.jellysquid.mods.sodium.mixin.features.debug;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderBackend;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

@Mixin(DebugHud.class)
public abstract class MixinDebugHud {
    @Shadow
    private static long toMiB(long bytes) {
        throw new UnsupportedOperationException();
    }

    @Redirect(method = "renderRightText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;getRightText()Ljava/util/List;"))
    private List<String> redirectRightTextEarly(DebugHud instance) {
        List<String> strings = ((DebugHudAccessor)instance).invokeGetRightText();
        strings.add("");
        strings.add("Sodium Speedrunning Build");
        strings.add(Formatting.UNDERLINE + getFormattedVersionText());
        strings.add("");

        for (int i = 0; i < strings.size(); i++) {
            String str = strings.get(i);

            if (str.startsWith("Allocated:")) {
                strings.add(i + 1, getNativeMemoryString());
                break;
            }
        }

        strings.add("");
        strings.addAll(getChunkRendererDebugStrings());
        strings.add("MrMangoHands' Unofficial Sodium");

        if (SodiumClientMod.options().advanced.disableDriverBlacklist) {
            strings.add(Formatting.RED + "(!!) Driver blacklist ignored");
        }
    }

    private static String getNativeMemoryString() {
        return "Off-Heap: +" + toMiB(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed()) + "MB";
    }
}
