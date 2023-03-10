package net.lipama.athens.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.hud.InGameHud;

import net.lipama.athens.screens.AthensHudOverlay;

@Mixin(InGameHud.class)
@SuppressWarnings("unused")
public class InGameHudMixin {
    @Inject(method = "render", at = @At("RETURN"))
    public void changeGamma(MatrixStack _matrices, float _tickDelta, CallbackInfo _ci) { AthensHudOverlay.gamma(); }

    @Inject(method = "render", at = @At("RETURN"), cancellable = true)
    public void onRender(MatrixStack matrices, float tickDelta, CallbackInfo info) { AthensHudOverlay.render(matrices, tickDelta, info); }
}
