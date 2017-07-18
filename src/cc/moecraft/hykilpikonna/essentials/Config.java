package cc.moecraft.hykilpikonna.essentials;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static cc.moecraft.hykilpikonna.essentials.Main.getMain;
import static cc.moecraft.hykilpikonna.essentials.Main.loglogger;

/**
 * This class is used to check, and save config, and database.
 */
public class Config
{
    private static File configFile;
    private static YamlConfiguration config;

    /**
     * Setup config class.
     */
    public Config()
    {
        configFile = new File(getMain().getDataFolder() + "\\Configuration.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Save config
     */
    public void saveConfig()
    {
        try
        {
            config.save(configFile);
        }
        catch (IOException e)
        {
            loglogger.Debug("[配置]保存失败");
            e.printStackTrace();
        }
    }

    /**
     * 获取配置变量
     * @return 配置变量
     */
    public YamlConfiguration getConfig()
    {
        return config;
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

    }

    private void writeDefaultConfig()
    {
        writeConfig();

        config.addDefault("AutoUpdate.Enable", true);
        config.addDefault("AutoUpdate.Downloader.TimeoutInSeconds", 3);
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
