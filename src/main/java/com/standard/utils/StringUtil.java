package com.standard.utils;


import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static final String DEFAULT_CHARTSET = "UTF-8";


    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    /**
     * 判断字符串是否不为空
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }

    /**
     * 转换为下划线
     */
    public static String underscoreName(String camelCaseName) {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * 转换为驼峰
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }


    /**
     * 根据模板生成生成字符串
     * @param template
     * @param params
     * @return
     */
    public static String processTemplate(String template, HashMap<Object, Object> params){
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("\\$\\{ {0,1}\\w+ {0,1}\\}").matcher(template);
        while (m.find()) {
            String param = m.group();
            Object value = params.get(param.substring(2, param.length() - 1).trim());
            m.appendReplacement(sb, value==null ? "" : value.toString());
        }
        return sb.toString();
    }

    /**
     * 简单判断text是否为json
     * @param text
     * @return
     */
    public static boolean isJson(String text) {
        text = text.trim();
        if (text.startsWith("{") && text.endsWith("}")){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 简单判断text是否为xml
     * @param text
     * @return
     */
    public static boolean isXml(String text) {
        text = text.trim();
        if (text.startsWith("<") && text.endsWith(">")){
            return true;
        } else {
            return false;
        }
    }


    public static boolean isMatches(String str, String regular){
        boolean flag = false;
        Pattern p = Pattern.compile(regular);
        Matcher m = p.matcher(str);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

}