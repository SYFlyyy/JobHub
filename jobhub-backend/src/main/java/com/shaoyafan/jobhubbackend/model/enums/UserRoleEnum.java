package com.shaoyafan.jobhubbackend.model.enums;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;

/**
 * 用户角色枚举
 *
 * @author SYF
 */
public enum UserRoleEnum {

    ADMIN("平台管理员", 0),
    CANDIDATE("求职者", 1),
    RECRUITER("招聘者", 2);

    private final String text;

    private final Integer value;

    UserRoleEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static UserRoleEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (Objects.equals(anEnum.value, value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }
}
