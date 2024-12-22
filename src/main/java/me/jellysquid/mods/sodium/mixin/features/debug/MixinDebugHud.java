package me.jellysquid.mods.sodium.mixin.features.debug;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(DebugHud.class)
public abstract class MixinDebugHud {
    @Unique
    private static String cpuNameCache = null;

    // @ModifyExpressionValue(method = "getRightText", at = @At(value = "CONSTANT", args = "stringValue=CPU: %s"))
    // private String removeCPUprefix(String original) {
    //     return original.substring("CPU: ".length());
    // }

    @ModifyExpressionValue(method = "getRightText", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlDebugInfo;getCpuInfo()Ljava/lang/String;"))
    private String shortenCPUInfo(String text) {
        if (cpuNameCache != null) {
            return cpuNameCache;
        }

        text = text.replace("Ryzen Threadripper", "R RT").replace("Ryzen ", "R");
        text = text.replaceAll("AMD|Gen(?= Intel)|[1-9][0-9]th|Intel| Core|\\((?:TM|R)\\)|GHz|Processor|Snapdragon|Qualcomm| -|CPU|with Radeon (?:(?:Vega )?Graphics|Vega Mobile Gfx)|Xeon|RADEON|(?! ),|,(?= )|COMPUTE CORES", "")
                .trim()
                .replaceAll(" +", " ");

        return cpuNameCache = text;
    }

    @Redirect(method = "renderRightText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;getRightText()Ljava/util/List;"))
    private List<String> redirectRightTextEarly(DebugHud instance) {
        List<String> strings = ((DebugHudAccessor) instance).invokeGetRightText();
        strings.add("");
        strings.add("MCSR Sodium " + SodiumClientMod.getVersion());
        return strings;
    }
}
