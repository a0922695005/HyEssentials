package cc.moecraft.hykilpikonna.essentials.Language;

import static cc.moecraft.hykilpikonna.essentials.Config.checkConfig;
import static cc.moecraft.hykilpikonna.essentials.Main.config;
import static cc.moecraft.hykilpikonna.essentials.Main.loglogger;
import static cc.moecraft.hykilpikonna.essentials.utils.StringUtils.removeColorCode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cc.moecraft.hykilpikonna.essentials.Language.Messages.Messages;
import cc.moecraft.hykilpikonna.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class LanguageAPI implements Listener
{

    //Console logging language
    private static String LoggingLanguage = "EN_US";
    

    
    //Message loading
    private Messages msg = new Messages("LanguageAPI");;
    
    public static LanguageAPI instance = null;

    /**
     * Get the instance for using the un-static methods in this class.
     * @return Instance.
     */
    public static LanguageAPI getInstance()
    {
        return instance;
    }

    /**
     * 获取玩家默认语言
     * @return 玩家的默认语言(在配置里)
     */
    public static String getPlayerDefaultLanguage()
    {
        return config.getConfig().getString("Language.DefaultPlayerLanguage");
    }
    
    /**
     * Set a player's selected language.
     * @param p The player.
     * @param lang The language that should be selected by that player.
     */
    public void setPlayerLang(Player p, String lang)
    {
        config.getLanguageDatabase().set(p.getName(), removeColorCode(lang));
        p.sendMessage(String.format(msg.getMsg("Lang_Selected", getPlayerLang(p)), lang));
        loglogger.log("Player " + p.getName() + " selected language " + lang);
        config.saveLanguageDatabase();
    }

    /**
     * Get the language that a player selected.
     * @param p The player.
     * @return The language that the player p selected.
     */
    public String getPlayerLang(Player p)
    {
        if (!config.getLanguageDatabase().contains(p.getName()))
        {
            return config.getConfig().getString("Language.DefaultLanguage");
        } 
        else
        {
            if (ArrayContains((ArrayList)(config.getConfig().getList("Language.EnabledLanguage")), removeColorCode(config.getLanguageDatabase().getString(p.getName()))))
            {
                return config.getLanguageDatabase().getString(p.getName());
            }
            else
            {
                return config.getConfig().getString("Language.DefaultLanguage");
            }
        }
    }
    
    GUI g = new GUI();
    

    
    private boolean ArrayContains(ArrayList a, String c)
    {
        boolean b = false;
        for (int i = 0; i < a.size(); i++)
        {
            if (a.get(i).equals(c))
            {
                b = true;
            }
        }
        return b;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("lang") || cmd.getName().equalsIgnoreCase("language"))
        {
            if (sender instanceof Player)
            {
                Player p = (Player) sender;
                switch (args.length) {
                    case 0:
                        g.ShowMenu(p);
                        break;
                    case 1:
                        msg.sendMsg("Help", getPlayerLang(p), p);
                        break;
                    case 2:
                        if (args[0].equals("set"))
                        {
                            ArrayList<String> TempAL = (ArrayList<String>)config.getConfig().getList("Language.EnabledLanguage");
                            if (TempAL.contains(args[1]))
                            {
                                setPlayerLang(p, args[1]);
                            }
                        }
                        break;
                    default:
                        g.ShowMenu(p);
                        break;
                }
            }
            else
            {
                //不是玩家输入指令
            }
        }
        return true;
    }
    
    @EventHandler
    public void onInventoryCloseE(InventoryCloseEvent e)
    {
        if (g.menu != null)
        {
            if (g.menu.isMenuOpen())
            {
                if (g.menu.ClickedButtonLore.isEmpty())
                {
                    return;
                }
                String tempLang = g.menu.ClickedButtonLore.get(0);
                if (!(tempLang.equals("")))
                {
                    String lang = removeColorCode(tempLang);
                    g.menu.ClickedButtonName = "";
                    g.menu.ClickedButtonLore = new ArrayList();
                    if (((ArrayList)(config.getConfig().getList("Language.EnabledLanguage"))).contains(lang))
                    {
                        setPlayerLang((Player)e.getPlayer(), lang);
                    }
                    else
                    {
                        e.getPlayer().sendMessage(String.format(msg.getMsg("Lang_Invalid_Not_Contained", getPlayerLang((Player) e.getPlayer())), lang));
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent e)
    {
        Debug("onPlayerLoginEvent");
        Player p = e.getPlayer();
        if (!db.contains(p.getName()))
        {
            String lang = getLocalLang(p);
            db.set(p.getName(), lang);
            Debug("onPlayerLoginEvent - Player: " + p.getName() + " Lang: " + lang + " Saved");
            saveDatabase();
        }
        else
        {
            Debug("DB contains p.getName(): " + p.getName());
        }
    }
}
