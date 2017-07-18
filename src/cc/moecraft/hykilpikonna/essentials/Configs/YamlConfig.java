package cc.moecraft.hykilpikonna.essentials.Configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static cc.moecraft.hykilpikonna.essentials.Main.getMain;
import static cc.moecraft.hykilpikonna.essentials.Main.loglogger;
import static org.bukkit.ChatColor.RED;

/**
 * 此类由 Hykilpikonna 在 2017/07/07 创建!
 * Created by Hykilpikonna on 2017/07/07!
 * Twitter: @Hykilpikonna
 * QQ/Wechat: 871674895
 */
public class YamlConfig
{
    private YamlConfiguration configuration;
    private File file;

    /**
     * 新建一个YAMLConfiguration
     * @param file 文件
     */
    public YamlConfig(File file)
    {
        this.file = file;
        configuration = YamlConfiguration.loadConfiguration(file);
        configuration.options().copyDefaults(true);
        add("DefaultConfig", true);
        save();
    }

    /**
     * 新建一个YAMLConfiguration
     * @param filePath 文件路径
     */
    public YamlConfig(String filePath)
    {
        this.file = new File(getMain().getDataFolder() + filePath);
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getConfig()
    {
        return configuration;
    }

    /**
     * 保存配置
     */
    public boolean save()
    {
        try
        {
            configuration.save(file);
            return true;
        }
        catch (IOException ignored)
        {
            loglogger.log(RED + "配置写入失败");
            return false;
        }
    }

    /**
     * 转发到YAMLConfiguration.addDefault()
     * @param path 路径
     * @param value 值
     */
    public void add(String path, Object value)
    {
        configuration.addDefault(path, value);
    }

    /**
     * 转发到YAMLConfiguration.addDefaults()
     * @param defaults 值
     */
    public void addDefaults(Map<String, Object> defaults)
    {
        configuration.addDefaults(defaults);
    }

    /**
     * 转发到YAMLConfiguration.addDefault()
     * @param path 路径
     * @param value 值
     */
    public void set(String path, Object value)
    {
        configuration.set(path, value);
    }

    /**
     * 检测是否是最新版本的配置
     * @param version 版本
     * @return 如果是最新, 就是True
     */
    public boolean isUpToDate(String version)
    {
        if (isUngenerated() || !getVersion().equals(version)) return false;
        return true;
    }

    /**
     * 检测是否是未生成的配置
     * @return 如果是未生成的, 就是True
     */
    public boolean isUngenerated()
    {
        if (configuration.getBoolean("DefaultConfig") || !(configuration.contains("DefaultConfig"))) return true;
        return false;
    }

    /**
     * 获取配置版本
     * @return 版本
     */
    public String getVersion()
    {
        return configuration.getString("ConfigVersion");
    }

    /**
     * 获取配置版本
     * @return 版本
     */
    public void setVersion(String version)
    {
        configuration.set("ConfigVersion", version);
    }

    /**
     * 通过路径获取ArrayList键值
     * @param path 路径
     * @param deep 是否深度搜索
     * @return 键值(ArrayList)
     */
    public ArrayList<String> getKeys(String path, boolean deep)
    {
        ArrayList<String> output = new ArrayList<>();
        output.addAll(configuration.getConfigurationSection(path).getKeys(deep));
        return output;
    }

    /**
     * 用ArrayList返回转发到.getStringList
     * @param path 路径
     * @return StringList(ArrayList)
     */
    public ArrayList<String> getStringList(String path)
    {
        ArrayList<String> output = new ArrayList<>();
        output.addAll(configuration.getStringList(path));
        return output;
    }

    /**
     * 用ArrayList返回转发到.getList
     * @param path 路径
     * @return List(ArrayList)
     */
    public ArrayList<Object> getList(String path)
    {
        ArrayList<Object> output = new ArrayList<>();
        output.addAll(configuration.getList(path));
        return output;
    }
}
