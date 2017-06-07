package cc.moecraft.hykilpikonna.essentials.logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger
{
    private String pre = "";
    private boolean debug = false;

    /**
     * 一个萌萌的Logger
     * @param pre 插件名
     * @param debug Debug模式是否开启
     */
    public Logger(String pre, boolean debug)
    {
        this.pre = pre;
        this.debug = debug;
    }

    /**
     * 设置是否Debug
     * @param value 是否Debug
     */
    public void setDebug(boolean value)
    {
        debug = value;
    }

    /**
     * 发送一条log
     * @param s log消息
     */
    public void log(String s)
    {
        Bukkit.getConsoleSender().sendMessage("["
                        + pre
                        + "] "
                        + s
        );
    }

    /**
     * 发送一条Debug w/只有消息
     * @param s Debug消息
     */
    public void Debug(String s)
    {
        if (debug)
        {
            log("[" +
                    ChatColor.RED +
                    "DEBUG" +
                    ChatColor.WHITE +
                    "(" +
                    ChatColor.YELLOW +
                    Thread.currentThread().getStackTrace()[2].getClassName() +
                    "." +
                    Thread.currentThread().getStackTrace()[2].getMethodName() +
                    ":" +
                    Thread.currentThread().getStackTrace()[2].getLineNumber() +
                    ChatColor.WHITE +
                    ")] " +
                    s
            );
        }
    }

    /**
     * 发送一条Debug /w对象&消息
     * @param object 对象
     * @param message 消息
     */
    public void Debug(Object object, String message)
    {
        if (debug)
        {
            log("[" +
                    ChatColor.RED +
                    "DEBUG" +
                    ChatColor.WHITE +
                    "(" +
                    ChatColor.YELLOW +
                    Thread.currentThread().getStackTrace()[2].getClassName() +
                    "." +
                    Thread.currentThread().getStackTrace()[2].getMethodName() +
                    ":" +
                    Thread.currentThread().getStackTrace()[2].getLineNumber() +
                    ChatColor.WHITE +
                    ")] " +
                    object.getClass().getSimpleName() +
                    ": " +
                    message
            );
        }
    }
}

