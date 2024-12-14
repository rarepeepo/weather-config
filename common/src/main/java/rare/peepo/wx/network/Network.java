package rare.peepo.wx.network;

import dev.architectury.networking.NetworkChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import rare.peepo.wx.WxMod;

public final class Network {
    final static NetworkChannel channel = NetworkChannel.create(Identifier.of(WxMod.ID, "netchan"));
    
    public static void init() {
        // Register our packets.
        channel.register(WxPacket.class, WxPacket::encode,
                WxPacket::new, WxPacket::handle);

    }
    
    @Environment(EnvType.CLIENT)
    public static <T> void sendToServer(T packet) {
        channel.sendToServer(packet);
    }
    
    public static <T> void sendToPlayer(ServerPlayerEntity player, T packet) {
        channel.sendToPlayer(player, packet);
    }
}
