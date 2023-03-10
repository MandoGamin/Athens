package net.lipama.athens.modules;

import com.linkrbot.projects.orbit.EventHandler;

import net.lipama.athens.events.TickEvent;
import net.lipama.athens.AthensClient;

public class Flight extends Module {
    public Flight() {
        super("Flight");
        this.height = -20;
    }
    @Override
    public void onEnable() {
        AthensClient.LOG.info("Flight Enabled");
        if(AthensClient.MC.player != null) AthensClient.MC.player.getAbilities().allowFlying = true;
    }

    @Override
    public void onDisable() {
        AthensClient.LOG.info("Flight Disabled");
        if(AthensClient.MC.player != null) AthensClient.MC.player.getAbilities().allowFlying = false;
    }
}
