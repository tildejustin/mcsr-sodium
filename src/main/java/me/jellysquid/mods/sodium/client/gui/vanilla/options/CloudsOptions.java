package me.jellysquid.mods.sodium.client.gui.vanilla.options;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions.GraphicsQuality;
import net.minecraft.client.resource.language.I18n;

public enum CloudsOptions implements IndexedOption {
    OFF(0, false, GraphicsQuality.DEFAULT, "options.off"),
    FAST(1, true, GraphicsQuality.FAST, "options.clouds.fast"),
    FANCY(2, true, GraphicsQuality.FANCY, "options.clouds.fancy");

    private final int index;
    private final boolean enabled;
    private final GraphicsQuality quality;
    private final String text;
    CloudsOptions(int index, boolean enabled, GraphicsQuality quality, String translationKey){
        this.index = index;
        this.enabled = enabled;
        this.quality = quality;
        this.text = I18n.translate(translationKey);
    }

    public GraphicsQuality getQuality() {
        return quality;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public String getText() {
        return text;
    }

    public static CloudsOptions getOption(boolean enabled, GraphicsQuality quality){
        if(enabled){
            if(quality == GraphicsQuality.FANCY){
                return FANCY;
            }
            return FAST;
        }
        return OFF;
    }
}
