package com.manage.contract.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : zhangH
 * @date : 2019/6/13 22:45
 * @description :
 */
public class PinYinUtil {

    private static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

    static Logger logger = LoggerFactory.getLogger(PinYinUtil.class);

    static {
        /*
         * 设置需要转换的拼音格式
         * 以天为例
         * HanyuPinyinToneType.WITHOUT_TONE 转换为tian
         * HanyuPinyinToneType.WITH_TONE_MARK 转换为tian1
         * HanyuPinyinVCharType.WITH_U_UNICODE 转换为tiān
         *
         */
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }


    public static String[] convertToJianPin(String str) {
        try {
            ArrayList<String> result = new ArrayList<>();
            for (int i = 0; i < str.length(); i++) {
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i), format);
                if (pinyins != null && pinyins.length > 0) {
                    Set<String> firstCharacters = new HashSet<>();
                    for (String pinyin : pinyins) {
                        firstCharacters.add(Character.toString(pinyin.charAt(0)));
                    }
                    if (result.isEmpty()) {
                        result.addAll(firstCharacters);
                    } else {
                        String[] temps = result.toArray(new String[0]);
                        result.clear();
                        for (String temp : temps) {
                            for (String firstCharacter : firstCharacters) {
                                result.add(temp + firstCharacter);
                            }
                        }

                    }
                }
            }
            return result.toArray(new String[0]);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    public static void main(String[] args) {
        String[] haha = convertToJianPin("乐王行");
        for (String s : haha) {
            System.out.println(s);
        }
    }

}
