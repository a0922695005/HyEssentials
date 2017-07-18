package cc.moecraft.hykilpikonna.essentials.updater;

import cc.moecraft.hykilpikonna.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static cc.moecraft.hykilpikonna.essentials.Main.getMain;
import static cc.moecraft.hykilpikonna.essentials.Main.loglogger;
import static cc.moecraft.hykilpikonna.essentials.Utils.PluginUtil.reload;
import static cc.moecraft.hykilpikonna.essentials.Utils.StringUtils.removeInNumeric;

/**
 * 此类由 Hykilpikonna 在 2017/07/16 创建!
 * Created by Hykilpikonna on 2017/07/16!
 * Twitter: @Hykilpikonna
 * QQ/Wechat: 871674895
 */
public class UrlUpdater
{
    private Plugin currentPlugin;
    private File currentPluginFile;
    private URL latestPluginFile;
    private String currentVersion;
    private String latestVersion;

    private String tempFilePath;

    /**
     * URL插件更新器
     * @param currentPluginFile 更新前的文件路径
     * @param currentVersion 当前的插件
     * @param latestFileURL 最新的文件URL地址
     * @param latestPluginYmlURL 最新的Plugin.yml地址
     */
    public UrlUpdater(File currentPluginFile, Plugin currentVersion, URL latestFileURL, URL latestPluginYmlURL, boolean repeatUpdate)
    {
        this(currentPluginFile, currentVersion, latestFileURL, latestPluginYmlURL, repeatUpdate, getMain().getConfig().getInt("AutoUpdate.Repeat.DefaultPeriodInSeconds"));
    }

    /**
     * URL插件更新器
     * @param currentPluginFile 更新前的文件路径
     * @param currentVersion 当前的插件
     * @param latestFileURL 最新的文件URL地址
     * @param latestPluginYmlURL 最新的Plugin.yml地址
     * @param repeatUpdate 是否间隔时间检查更新
     * @param period 间隔的时间(秒)
     */
    public UrlUpdater(File currentPluginFile, Plugin currentVersion, URL latestFileURL, URL latestPluginYmlURL, boolean repeatUpdate, long period)
    {
        this.tempFilePath = Main.getMain().getDataFolder() + "/temp/" + currentVersion.getDescription().getName() + "/" + System.currentTimeMillis();

        this.currentPluginFile = currentPluginFile;
        this.currentVersion = currentVersion.getDescription().getVersion();

        this.latestPluginFile = latestFileURL;
        this.latestVersion = downloadPluginYML(latestPluginYmlURL);

        this.currentPlugin = currentVersion;

        if (repeatUpdate) repeatUpdate(period);
    }

    /**
     * 判断是否需要更新
     * @return 是否需要更新
     */
    private boolean checkUpdate()
    {
        loglogger.Debug("versionComparison(currentVersion, latestVersion) = " + versionComparison(currentVersion, latestVersion));
        return versionComparison(currentVersion, latestVersion) == 1;
    }

    /**
     * 自动更新
     */
    public void repeatUpdate(long period)
    {
        Thread thread = new Thread((Runnable) new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (getMain().getConfig().getBoolean("AutoUpdate.Enable"))
                {
                    if (checkUpdate())
                    {
                        downloadFile(latestPluginFile, currentPluginFile);
                        reload(currentPlugin);
                    }
                }
            }
            }.runTaskTimerAsynchronously(getMain(), 0L, period));
        thread.setName("Update Check");
        thread.start();
    }

    /**
     * 更新
     */
    public void update()
    {
        Thread thread = new Thread((Runnable) new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (getMain().getConfig().getBoolean("AutoUpdate.Enable"))
                {
                    if (checkUpdate())
                    {
                        downloadFile(latestPluginFile, currentPluginFile);
                        reload(currentPlugin);
                    }
                }
            }
            }.runTaskAsynchronously(getMain()));
        thread.setName("Update Check");
        thread.start();
    }

    /**
     * 下载Plugin.yml来获取最新版本
     * @param url 下载地址
     * @return 最新版本
     */
    private String downloadPluginYML(URL url)
    {
        File pluginYml = new File(tempFilePath + "/plugin.yml");
        downloadFile(url, pluginYml);
        return getVersionFromPluginYML(pluginYml);
    }

    /**
     * 下载文件
     * 归功于 @V乐乐
     * Credit to @Vlvxingze
     * @param url 下载地址
     * @param file 覆盖文件
     * @return 是否下载成功
     */
    private static boolean downloadFile(URL url, File file)
    {
        if (file.exists())
        {
            file.delete();
        }
        try
        {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(getMain().getConfig().getInt("AutoUpdate.Downloader.TimeoutInSeconds") * 1000);
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            InputStream inputStream = httpURLConnection.getInputStream();
            byte[] getData = readInputStream(inputStream);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(getData);
            fileOutputStream.close();

            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        catch (IOException ignored)
        {
            loglogger.Debug("下载失败");
            return false;
        }
        return true;
    }

    /**
     * 缓存下载文件?
     * 没学过IO, 看不懂_(:з」∠)_
     * 归功于 @V乐乐
     * Credit to @Vlvxingze
     * @param inputStream IDK! Ask @Vlvxingze
     * @return IDK! Ask @Vlvxingze
     * @throws IOException IDK! Ask @Vlvxingze
     */
    private static byte[] readInputStream(InputStream inputStream) throws IOException
    {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 获取Plugin.yml中的版本号
     * @param pluginYML Plugin.yml文件
     * @return 版本号
     */
    private static String getVersionFromPluginYML(File pluginYML)
    {
        return YamlConfiguration.loadConfiguration(pluginYML).getString("version");
    }

    /**
     * 判断当前版本和最新版本的关系
     *
     * 如果当前版本小于最新版本, 返回1,
     * 如果当前版本大于最新版本, 返回-1,
     * 如果当前版本等于最新版本, 返回0
     *
     * 注意: 所有非数字字符都会被忽略
     *
     * 例子: 0.1.6.9 和 0.1.6.9 返回0
     * 例子: 0.1.6.9 和 0.1.7.0 返回1
     * 例子: 0.1.69  和 0.17.0  返回1
     * 例子: 0.16.9  和 0.1.70  返回-1
     *
     * @param currentVersion 当前版本
     * @param latestVersion 最新版本
     * @return 对比值
     */
    public static int versionComparison(String currentVersion, String latestVersion)
    {
        String[] currentVersionAfterSplit = removeInNumeric(currentVersion).split("\\.");
        String[] latestVersionAfterSplit = removeInNumeric(latestVersion).split("\\.");

        int currentLength = currentVersionAfterSplit.length;
        int latestLength = latestVersionAfterSplit.length;

        for (int i = 0; i < Math.max(currentLength, latestLength); i++)
        {
            int currentVersionAtI = i < currentLength ? Integer.parseInt(currentVersionAfterSplit[i]) : 0;
            int latestVersionAtI = i < latestLength ? Integer.parseInt(latestVersionAfterSplit[i]) : 0;

            if (currentVersionAtI < latestVersionAtI) return -1;
            if (currentVersionAtI > latestVersionAtI) return 1;
        }

        return 0;
    }
}
