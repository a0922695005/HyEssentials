package cc.moecraft.hykilpikonna.essentials.Utils;

import static cc.moecraft.hykilpikonna.essentials.Main.loglogger;

/**
 * 此类由 Hykilpikonna 在 2017/06/11 创建!
 * Created by Hykilpikonna on 2017/06/11!
 * Twitter: @Hykilpikonna
 * QQ/Wechat: 871674895
 */
public class StringUtils
{
    /**
     * 将颜色代码从字符串中移除
     * @param s 字符串
     * @return 移除颜色代码后的字符串
     */
    public static String removeColorCode(String s)
    {
        char[] c = s.toCharArray();
        String RebuiltTemp = "";
        for (int i = 0; i < c.length; i++)
        {
            if (c[i] == '§' || c[i] == '&')
            {
                i += 1;
            }
            else
            {
                RebuiltTemp += c[i];
            }
        }
        return RebuiltTemp;
    }

    public static String removeInNumeric(String string)
    {
        StringBuilder output = new StringBuilder();
        for (Character aChar : string.toCharArray())
        {
            if (Character.isDigit(aChar)) output.append(aChar);
            if (aChar == '.') output.append('.');
        }
        loglogger.Debug("Final String: " + output.toString());
        return output.toString();
    }
}
