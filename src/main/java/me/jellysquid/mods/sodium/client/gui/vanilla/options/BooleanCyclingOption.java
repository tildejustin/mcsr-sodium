package me.jellysquid.mods.sodium.client.gui.vanilla.options;

import net.minecraft.client.resource.language.I18n;

public enum BooleanCyclingOption implements IndexedOption {
    ON(0, true, "options.on"),
    OFF(1, false, "options.off");

    private final int index;
    private final boolean enabled;
    private final String text;

    BooleanCyclingOption(int index, boolean enabled, String translationKey) {
        this.index = index;
        this.enabled = enabled;
        this.text = I18n.translate(translationKey);
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

    public static BooleanCyclingOption getOption(boolean value) {
        if (value) {
            return ON;
        }
        return OFF;
    }
}
