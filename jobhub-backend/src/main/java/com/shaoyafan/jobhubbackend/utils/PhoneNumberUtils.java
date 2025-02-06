package com.shaoyafan.jobhubbackend.utils;

import java.util.regex.Pattern;

/**
 * 手机号工具类
 *
 * @author SYF
 */
public class PhoneNumberUtils {

    // 国内手机号码正则表达式
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    /**
     * 校验国内手机号码格式
     * @param phone 待校验的手机号码
     * @return 如果手机号码格式正确返回 true，否则返回 false
     */
    public static boolean isValidChinesePhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
}
