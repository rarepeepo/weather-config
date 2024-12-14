package rare.peepo.wx.network;

import dev.architectury.networking.NetworkManager.PacketContext;
import java.util.function.Supplier;
import net.minecraft.network.PacketByteBuf;
import rare.peepo.wx.Log;
import rare.peepo.wx.config.WxConfig;
import rare.peepo.wx.config.WxMode;

public class WxPacket {
    WxConfig config;

    /**
     * Called by network api to decode a new instance from the serialized
     * network data.
     * 
     * @param buf
     *  The serialized network data to initialize the instance with.
     */
    public WxPacket(PacketByteBuf buf) {
        config = new WxConfig(
                buf.readEnumConstant(WxMode.class),
                buf.readFloat(),
                buf.readEnumConstant(WxMode.class),
                buf.readEnumConstant(WxMode.class));
    }
 
    /**
     * Called by the user to construct a new sort command packet that can
     * be sent over the network.
     * 
     * @param config
     *  The weather configuration to send over the network.
     */
    public WxPacket() {
    }
 
    /**
     * Called by network api to serialize instance data into a buffer.
     * 
     * @param buf
     *  The buffer to serialize the instance data to.
     */
    public void encode(PacketByteBuf buf) {
        buf.writeEnumConstant(WxConfig.getBiome());
        buf.writeFloat(WxConfig.getTemperature());
        buf.writeEnumConstant(WxConfig.getPrecipitation());
        buf.writeEnumConstant(WxConfig.getThunder());
    }
 
    /**
     * Called by network api to process a received packet.
     * 
     * @param context
     *  The packet context providing access to the sender as well as the logical
     *  side the packet is being received on.
     *  
     *  @apiNote
     *  This method executes on the network thread. Any work to be done must be
     *  scheduled to run on the main thread.
     */
    public void handle(Supplier<PacketContext> context) {
        // Schedule to run on render thread.
        context.get().queue(() -> updateConfig());
    }
    
    /**
     * Updates the weather config on the client.
     * 
     * @param player
     *  The player whose inventory to sort.
     */
    void updateConfig() {
      // This runs on the client-side.
        Log.info("Received new weather configuration from the server");
        WxConfig.setInstance(config);
    }
}
