package rare.peepo.wx;

import dev.architectury.event.events.common.PlayerEvent;
import rare.peepo.wx.command.Commands;
import rare.peepo.wx.config.WxConfig;
import rare.peepo.wx.network.Network;
import rare.peepo.wx.network.WxPacket;

public final class WxMod {
    public static final String ID = "weatherconfig";

    public static void onInitialize() {
        Log.info("Initializing WeatherConfig");
        WxConfig.init();
        Network.init();
        Commands.init();
        
        // Note:
        // The weather temperature needs to be send to the client-side because the rendering
        // code calls into Biome.getTemperature to figure out whether to render precipitation
        // as rain or as snow.
        
        // Send the current weather configuration to clients upon connecting.
        PlayerEvent.PLAYER_JOIN.register(player -> {
          Network.sendToPlayer(player, new WxPacket());
        });
    }
}
