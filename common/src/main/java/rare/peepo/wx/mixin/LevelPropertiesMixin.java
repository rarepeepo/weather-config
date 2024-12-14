package rare.peepo.wx.mixin;

import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rare.peepo.wx.Log;
import rare.peepo.wx.config.WxConfig;
import rare.peepo.wx.config.WxMode;

@Mixin(LevelProperties.class)
public abstract class LevelPropertiesMixin {
    @Shadow
    public abstract void setRaining(boolean raining);
    @Shadow
    public abstract void setThundering(boolean thundering);
    
    @Inject(method = "isRaining", at = @At("TAIL"), cancellable = true)
    public final void isRaining(CallbackInfoReturnable<Boolean> ci) {
        var mode = WxConfig.getPrecipitation();
        if (mode == WxMode.UNSET)
            return;
        var isRaining = ci.getReturnValue();
        var targetRaining = mode == WxMode.FORCE;
        
        if (isRaining == targetRaining)
            return;
        Log.info("Changing rain to " + targetRaining);
        setRaining(targetRaining);
        ci.setReturnValue(targetRaining);
    }
    
    @Inject(method = "isThundering", at = @At("TAIL"), cancellable = true)
    public final void isThundering(CallbackInfoReturnable<Boolean> ci) {
        var mode = WxConfig.getThunder();
        if (mode == WxMode.UNSET)
            return;
        var isThundering = ci.getReturnValue();
        var targetThundering = mode == WxMode.FORCE;
        
        if (isThundering == targetThundering)
            return;
        Log.info("Changing thunder to " + targetThundering);
        setThundering(targetThundering);
        ci.setReturnValue(targetThundering);
    }
}
