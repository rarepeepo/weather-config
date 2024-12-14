package rare.peepo.wx.command;

import static com.mojang.brigadier.arguments.FloatArgumentType.getFloat;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.utils.GameInstance;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import rare.peepo.wx.config.WxConfig;
import rare.peepo.wx.config.WxMode;
import rare.peepo.wx.network.Network;
import rare.peepo.wx.network.WxPacket;

public final class Commands {
    public static void init() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> {
            var provider = new WxModeSuggestionProvider();
            // Don't you just love the beauty of Java?
            dispatcher.register(literal("setweather")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(literal("temperature")
                            .executes(ctx -> getValue(ctx))
                            .then(argument("degrees", FloatArgumentType.floatArg())
                                    .executes(ctx -> setValue(ctx))))
                    .then(literal("biome")
                            .executes(ctx -> getValue(ctx))
                            .then(argument("mode", StringArgumentType.greedyString())
                                    .suggests(provider)
                                    .executes(ctx -> setValue(ctx))))
                    .then(literal("precipitation")
                            .executes(ctx -> getValue(ctx))
                            .then(argument("mode", StringArgumentType.greedyString())
                                    .suggests(provider)
                                    .executes(ctx -> setValue(ctx))))
                    .then(literal("thunder")
                            .executes(ctx -> getValue(ctx))
                            .then(argument("mode", StringArgumentType.greedyString())
                                    .suggests(provider)
                                    .executes(ctx -> setValue(ctx))))
                    );
        });
    }
    
    static int getValue(CommandContext<ServerCommandSource> ctx) {
        var command = ctx.getNodes().get(1).getNode().getName();
        var s = "";
        switch(command) {
            case "temperature":
                s = "The current temperature is " + WxConfig.getTemperature();
                break;
            case "biome":
                s = "The current biome mode is " + WxConfig.getBiome();
                break;
            case "precipitation":
                s = "The current precipitation mode is " + WxConfig.getPrecipitation();
                break;
            case "thunder":
                s = "The current thunder mod is " + WxConfig.getThunder();
                break;
        }
        var lit = Text.literal(s);
        ctx.getSource().sendFeedback(() -> lit, false);
        return 1;
    }

    static int setValue(CommandContext<ServerCommandSource> ctx) {
        var command = ctx.getNodes().get(1).getNode().getName();
        String s = null;
        WxMode mode;
        switch (command) {
            case "temperature":
                WxConfig.setTemperature(getFloat(ctx, "degrees"));
                s = "Set global temperature to " + WxConfig.getTemperature();
                break;
            case "biome":
                mode = parseMode(ctx);
                WxConfig.setBiome(mode);
                s = "Set biome mode to " + mode;
                break;
            case "precipitation":
                mode = parseMode(ctx);
                WxConfig.setPrecipitation(mode);
                s = "Set precipitation mode to " + mode;
                break;
            case "thunder":
                mode = parseMode(ctx);
                WxConfig.setThunder(mode);
                s = "Set thunder mode to " + mode;
                break;
        }
        WxConfig.save();
        if (s != null) {
            var lit = Text.literal(s);
            ctx.getSource().sendFeedback(() -> lit, true);
        }
        // Propagate weather change to all players.
        for (var player : GameInstance.getServer().getPlayerManager().getPlayerList())
            Network.sendToPlayer(player, new WxPacket());
        return 1;
    }
    
    static WxMode parseMode(CommandContext<ServerCommandSource> ctx) {
        return WxMode.valueOf(getString(ctx, "mode").trim().toUpperCase());
    }
}
