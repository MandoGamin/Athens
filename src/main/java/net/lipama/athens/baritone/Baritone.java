package net.lipama.athens.baritone;

import baritone.api.*;

public class Baritone {
    private final IBaritoneProvider provider;
    private final Settings settings;
    public Baritone() {
        this.settings = BaritoneAPI.getSettings();
        this.provider = BaritoneAPI.getProvider();
    }

    public IBaritone baritone() {
        return this.provider.getPrimaryBaritone();
    }

    public Settings settings() {
        return this.settings;
    }

    public IBaritoneProvider baritoneProvider() {
        return this.provider;
    }

}
