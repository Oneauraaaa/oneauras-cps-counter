package com.oneaura.cpscounter.mixin;

import com.oneaura.cpscounter.configlib.gui.CombatRuntime;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public final class MultiPlayerGameModeMixin {
    @Inject(method = "attack", at = @At("HEAD"))
    private void oneauras$a(Player player, Entity target, CallbackInfo ci) {
        CombatRuntime.mark(target);
    }
}
