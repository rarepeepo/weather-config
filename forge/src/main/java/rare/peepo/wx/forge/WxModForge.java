package rare.peepo.wx.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rare.peepo.wx.WxMod;

@Mod(WxMod.ID)
public final class WxModForge {
    @SuppressWarnings("removal")
    public WxModForge() {
        // Submit our event bus to let Architectury API register our
        // content on the right time.
        EventBuses.registerModEventBus(WxMod.ID,
                FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        WxMod.onInitialize();
    }
}
