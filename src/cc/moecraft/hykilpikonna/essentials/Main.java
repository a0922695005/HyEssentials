package cc.moecraft.hykilpikonna.essentials;

import cc.moecraft.hykilpikonna.essentials.logger.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hykilpikonna的基础前置!
 */
public class Main extends JavaPlugin
{
    public Logger logger = new Logger("HyEssentials", true);

    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        logger.log("已加载此前置插件!");
    }

    public void onDisable()
    {
        logger.log("已卸载此前置插件!");
    }
}
