package rare.peepo.wx.mixin;

import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rare.peepo.wx.config.WxConfig;
import rare.peepo.wx.config.WxMode;

@Mixin(Biome.class)
public class BiomeMixin {
    // The temperature determines whether precipitation is rain or snow and
    // whether snow accumulates on the ground, water freezes etc.
    @Inject(method = "getTemperature", at = @At("HEAD"), cancellable = true)
    private void getTemperature(CallbackInfoReturnable<Float> ci) {
        var t = WxConfig.getTemperature();
        if (t != 0)
            ci.setReturnValue(t);
    }
    
    // Determines whether the biome has precipitation at all.
    @Inject(method = "hasPrecipitation", at = @At("HEAD"), cancellable = true)
    private void hasPrecipitation(CallbackInfoReturnable<Boolean> ci) {
        var mode = WxConfig.getBiome();
        if (mode == WxMode.UNSET)
            return;
        ci.setReturnValue(
                mode == WxMode.FORCE
        );
    }
}