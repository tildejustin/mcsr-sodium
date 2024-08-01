package me.jellysquid.mods.sodium.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.NotNull;
import org.mcsr.speedrunapi.config.api.SpeedrunConfigScreenProvider;

@SuppressWarnings("unused")
public class SodiumOptionsGUIProvider implements SpeedrunConfigScreenProvider {
    @Override
    public @NotNull Screen createConfigScreen(Screen parent) {
        return new SodiumOptionsGUI(parent);
    }

    @Override
    public boolean isAvailable() {
        return MinecraftClient.getInstance().world == null;
    }
}
