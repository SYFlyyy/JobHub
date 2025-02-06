package com.shaoyafan.jobhubbackend.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;

/**
 * 企业规模枚举
 *
 * @author SYF
 */
public enum CompanySizeEnum {
    // '0-20','20-99','100-499','500-999','1000-9999','10000+'
    SIZE_0_20("0-20"),
    SIZE_20_99("20-99"),
    SIZE_100_499("100-499"),
    SIZE_500_999("500-999"),
    SIZE_1000_9999("1000-9999"),
    SIZE_10000("10000+");

    private final String value;

    CompanySizeEnum(String value) {
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    @JsonCreator
    public static CompanySizeEnum getEnumByValue(String value) {
        if (value == null) {
            return null;
        }
        for (CompanySizeEnum anEnum : CompanySizeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业规模参数错误");
    }

    public String getValue() {
        return value;
    }
}
