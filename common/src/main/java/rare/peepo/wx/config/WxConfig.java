package rare.peepo.wx.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;
import java.nio.file.Files;
import rare.peepo.wx.Log;
import rare.peepo.wx.WxMod;

public class WxConfig {
    static WxConfig INSTANCE;
    
    WxMode biome;
    float temperature;
    WxMode precipitation;
    WxMode thunder;
    
    public static WxMode getBiome() {
        return INSTANCE.biome;
    }
    
    public static void setBiome(WxMode mode) {
        INSTANCE.biome = mode;
    }
    
    public static float getTemperature() {
        return INSTANCE.temperature;
    }
    
    public static void setTemperature(float degrees) {
        INSTANCE.temperature = degrees;
    }
    
    public static WxMode getPrecipitation() {
        return INSTANCE.precipitation;
    }
    
    public static void setPrecipitation(WxMode mode) {
        INSTANCE.precipitation = mode;
    }
    
    public static WxMode getThunder() {
        return INSTANCE.thunder;
    }
    
    public static void setThunder(WxMode mode) {
        INSTANCE.thunder = mode;
    }
    
    public static void setInstance (WxConfig config) {
        INSTANCE = config;
    }
    
    public WxConfig() {
        this(WxMode.UNSET, 0, WxMode.UNSET, WxMode.UNSET);
    }
    
    public WxConfig(WxMode biome, float temperature, WxMode precipitation, WxMode thunder) {
        this.biome = biome;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.thunder = thunder;
    }
    
    public static void init() {
        INSTANCE = new WxConfig();
        var file = Platform.getConfigFolder().resolve(WxMod.ID + ".json").toFile();
        try {
            if (file.exists()) {
                INSTANCE = new Gson().fromJson(
                        Files.readString(file.toPath()), WxConfig.class);
            }
        } catch (Exception e) {
            Log.error("Could not read data from config: {}", e);
        }
    }
    
    public static void save() {
        var path = Platform.getConfigFolder().resolve(WxMod.ID + ".json");
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path,
                    new GsonBuilder().setPrettyPrinting().create().toJson(INSTANCE));
        } catch (Exception e) {
            Log.error("Could not write data to config: {}", e);
        }
    }
}
