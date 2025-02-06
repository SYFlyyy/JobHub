package com.shaoyafan.jobhubbackend.utils;

import java.util.regex.Pattern;

/**
 * 邮件工具类
 *
 * @author SYF
 */
public class EmailUtils {

    // 邮件格式的正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // 编译正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * 验证邮件格式是否合法
     *
     * @param email 待验证的邮件地址
     * @return 如果邮件格式合法返回 true，否则返回 false
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
