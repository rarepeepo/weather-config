package rare.peepo.wx.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.server.command.ServerCommandSource;
import rare.peepo.wx.config.WxMode;

public class WxModeSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(
            CommandContext<ServerCommandSource> context,
            SuggestionsBuilder builder) {
        for (var v : WxMode.values())
            builder.suggest(v.toString().toLowerCase());
        return builder.buildFuture();
    }
}