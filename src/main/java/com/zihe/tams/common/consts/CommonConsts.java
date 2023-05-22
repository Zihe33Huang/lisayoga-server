package com.zihe.tams.common.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用常量
 */
public class CommonConsts {

    public static final Integer TRUE_VALUE_INT = 1;
    public static final Integer FALSE_VALUE_INT = 0;

    public static final String TIME_FORMATTER = "HH:mm:ss";
    public static final String DATE_FORMATTER = "yyyy-MM-dd";
    public static final String DATETIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public static final Map<Integer, String> cardType = new HashMap<Integer, String>();

    static {
        cardType.put(123, "常规团课卡");
        cardType.put(4, "vip团课卡");
        cardType.put(5, "常规私教卡");
        cardType.put(6, "普拉提器械私教卡");
        cardType.put(7, "普拉提器械团课卡");
        cardType.put(8, "充值卡");
        cardType.put(9, "大礼包充值卡");
        cardType.put(10, "孕产卡");
    }
}
