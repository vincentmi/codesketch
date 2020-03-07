package com.vnzmi.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static Pattern linePattern = Pattern.compile("[_\\- ]+(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 字符串转成驼峰写法
     * @param str
     * @return
     */
    public static String toCamel(String str)
    {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰但是首字母大写
     * @param str
     * @return
     */
    public static String toCamelUpper(String str)
    {
        char[] cs=toCamel(str).toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    /**
     * 转换成下划线写法
     * @param str
     * @return
     */
    public static String toLine(String str){
        return toLine(str,'_');
    }

    /**
     * 转换成连接符
     * @param str
     * @param linkChar
     * @return
     */
    public static String toLine(String str,char linkChar){
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, linkChar + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return trim(sb.toString(),String.valueOf(linkChar));
    }

    /**
     * 左清除字符
     * @param str
     * @param splitChars
     * @return
     */
    public static String ltrim(String str , String splitChars)
    {
        char[] chars = str.toCharArray();
        int i = 0;
        for(;i< chars.length;i++)
        {
            if(splitChars.indexOf(chars[i]) == -1){
                break;
            }
        }
        return str.substring(i);
    }

    /**
     * 右清除字符
     * @param str
     * @param splitChars
     * @return
     */
    public static String rtrim(String str , String splitChars)
    {
        char[] chars = str.toCharArray();
        int i =  chars.length -1;
        for(;i>=0;i--)
        {
            if(splitChars.indexOf(chars[i]) == -1){
                break;
            }
        }
        return str.substring(0,i+1);
    }

    /**
     * 两边清除字符
     * @param str
     * @param splitChars
     * @return
     */
    public static String trim(String str , String splitChars)
    {
        str = ltrim(str,splitChars);
        return rtrim(str,splitChars);
    }

    /**
     * 获取文件扩展名
     * @param str
     * @return
     */
    public static String ext(String str)
    {
        char[] chars  = str.toCharArray();
        int index = -1;
        for(int i=chars.length-1;i>=0;i--)
        {
            if(chars[i] == '.')
            {
                index = i+1;
                break;
            }
        }

        if(index == -1){
            return "";
        }else{
            return str.substring(index).toLowerCase();
        }
    }

}
