package me.jellysquid.mods.sodium.mixin.features.options;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.VanillaOptions;
import me.jellysquid.mods.sodium.client.gui.options.OptionFlag;
import me.jellysquid.mods.sodium.client.gui.vanilla.builders.OptionBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.VideoOptionsScreen;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Mixin(VideoOptionsScreen.class)
public class MixinVideoOptionsScreen extends GameOptionsScreen {

    public MixinVideoOptionsScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Redirect(method = "init", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonListWidget;addAll([Lnet/minecraft/client/options/Option;)V"))
    private void optionsSwap(ButtonListWidget list, Option[] old_options) {
        List<Option> options =  new ArrayList<>(Arrays.asList(old_options));
        SodiumGameOptions.SpeedrunSettings speedrunSettings = SodiumClientMod.options().speedrun;
        if (speedrunSettings.showEntityCulling) {
            options.add(VanillaOptions.ENTITY_CULLING);
        }
        if (speedrunSettings.showFogOcclusion) {
            options.add(VanillaOptions.FOG_OCCLUSION);
        }
        list.addAll(options.toArray(new Option[0]));
    }

    @Inject(method = "mouseReleased", at = @At("RETURN"))
    public void onRelease(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        VanillaOptions.applySettingsChanges();
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        boolean result = super.keyReleased(keyCode, scanCode, modifiers);
        VanillaOptions.applySettingsChanges();
        return result;
    }

    @Override
    public void removed(){
        MinecraftClient client = MinecraftClient.getInstance();

        Set<OptionFlag> flags = OptionBuilder.getFlags();
        if(flags.contains(OptionFlag.REQUIRES_RENDERER_RELOAD)){
            client.worldRenderer.reload();
        }

        SodiumClientMod.options().writeChanges();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
            VanillaOptions.applySettingsChanges();


        if(MinecraftClient.getInstance().world == null && keyCode == GLFW.GLFW_KEY_P && (modifiers & GLFW.GLFW_MOD_SHIFT) != 0) {
            MinecraftClient.getInstance().openScreen(new SodiumOptionsGUI(this.parent));
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
