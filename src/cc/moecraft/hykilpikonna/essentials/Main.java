package cc.moecraft.hykilpikonna.essentials;

import cc.moecraft.hykilpikonna.essentials.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static cc.moecraft.hykilpikonna.essentials.Utils.PluginUtil.reload;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

/**
 * Hykilpikonna的基础前置!
 */
public class Main extends JavaPlugin
{
    //Logger
    public static Logger loglogger = new Logger("HyEssentials", true);

    //实例接口
    private static Main instance = null;

    //配置文件
    public static Config config;

    /**
     * 加载插件
     */
    public void onEnable()
    {
        //注册实例接口
        instance = this;

        try {
            bypassYUM();
        } catch (IOException e) {
            e.printStackTrace();
        }

        config = new Config();

        loglogger.log("已加载此前置插件!");
    }

    /**
     * 获取实例
     * @return 实例
     */
    public static Main getMain()
    {
        return instance;
    }

    /**
     * 卸载插件
     */
    public void onDisable()
    {
        loglogger.log("已卸载此前置插件!");
    }

    /**
     * 绕过YUM的网络检测和线程检测
     * @throws IOException 读取YUM配置失败
     */
    private static void bypassYUM() throws IOException
    {
        tempLog("正在检测YUM....");
        Plugin yum = Bukkit.getPluginManager().getPlugin("Yum");
        if (yum != null)
        {
            boolean reloadYum = false;

            if (!(getMain().getConfig().contains("AutoUpdate.YumNetworkCheckBypass") || getMain().getConfig().getBoolean("AutoUpdate.YumNetworkCheckBypass")))
            {
                File yumNetworkFile = new File("plugins/Yum/network.yml");
                YamlConfiguration yumNetworkConfig = YamlConfiguration.loadConfiguration(yumNetworkFile);
                List<String> ignoreList = yumNetworkConfig.getStringList("Ignore");
                if (!(ignoreList.contains("HyUltimatePlugin")))
                {
                    tempLog("YUM拦截了HyUltimatePlugin对网络的访问, 正在解除拦截...");
                    ignoreList.add("HyUltimatePlugin");
                    yumNetworkConfig.set("Ignore", ignoreList);
                    yumNetworkConfig.save(yumNetworkFile);
                    reloadYum = true;
                }
                else
                {
                    tempLog(GREEN + "YUM配置中网络设置已有本插件的例外");
                }
            }
            else
            {
                tempLog(RED + "未开启YUM网络检测");
            }

            if (!(getMain().getConfig().contains("AutoUpdate.DisableYumThreadCheck") || getMain().getConfig().getBoolean("AutoUpdate.DisableYumThreadCheck")))
            {
                File yumNetworkFile = new File("plugins/Yum/thread.yml");
                YamlConfiguration yumThreadConfig = YamlConfiguration.loadConfiguration(yumNetworkFile);
                if (yumThreadConfig.getBoolean("MainThreadCheck") || yumThreadConfig.getBoolean("ThreadSafe"))
                {
                    tempLog("YUM拦截了同步下载, 正在解除拦截...");
                    yumThreadConfig.set("MainThreadCheck", false);
                    yumThreadConfig.set("ThreadSafe", false);
                    yumThreadConfig.save(yumNetworkFile);
                    reloadYum = true;
                }
                else
                {
                    tempLog(GREEN + "YUM配置中未开启线程检测");
                }
            }
            else
            {
                tempLog(RED + "未开启YUM线程检测");
            }

            if (reloadYum)
            {
                reload(yum);
                tempLog("拦截解除成功!");
            }
        }
        else
        {
            tempLog(GREEN + "未检测到YUM");
        }
    }

    private static void tempLog(String string)
    {
        Bukkit.getConsoleSender().sendMessage("[HyEssentials] " + ChatColor.YELLOW + string);
    }
}
