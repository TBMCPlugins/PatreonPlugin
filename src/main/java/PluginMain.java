import buttondevteam.website.ButtonWebsiteModule;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin {
    @Override
    public void onEnable() {
        ButtonWebsiteModule.addPage(new PatreonPage(getConfig()));
    }
}
