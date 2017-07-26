package cc.moecraft.hykilpikonna.essentials;

import org.bukkit.configuration.file.FileConfiguration;

import static cc.moecraft.hykilpikonna.essentials.Main.getMain;
import static cc.moecraft.hykilpikonna.essentials.Main.loglogger;

/**
 * This class is used to check, and save config, and database.
 */
public class Config
{
    private static FileConfiguration config;

    /**
     * Setup config class.
     */
    public Config()
    {
        config = getMain().getConfig();
        config.options().copyDefaults(true);

        checkConfig();
    }

    /**
     * Save config
     */
    public void saveConfig()
    {
        getMain().saveConfig();
    }

    /**
     * 检查配置是否是最新
     */
    public void checkConfig()
    {
        if (config.getBoolean("DefaultConfig") || !(config.contains("DefaultConfig"))) writeDefaultConfig();
        else if (!config.getString("ConfigVersion").equals(getMain().getDescription().getVersion())) writeDefaultConfig();
        saveConfig();
        readConfig();
    }

    private void readConfig()
    {
        loglogger.setDebug(config.getBoolean("Debug"));
    }

    private void writeDefaultConfig()
    {
        writeConfig();

        config.addDefault("Debug", false);
        config.addDefault("AutoUpdate.Enable", true);
        config.addDefault("AutoUpdate.Default.TimeoutInSeconds", 3);
        config.addDefault("AutoUpdate.Default.CheckDelayInSeconds", 3600);

        config.addDefault("AutoUpdate.YumNetworkCheckBypass", true);
    }

    private void writeConfig()
    {
        config.options().copyDefaults(true);

        //把默认设置设为否
        config.set("DefaultConfig", false);

        //配置版本
        config.set("ConfigVersion", getMain().getDescription().getVersion());
    }
}
