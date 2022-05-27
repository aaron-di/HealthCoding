package com.example.healthmanagement.enums;

/**
 * 时间段（0:早餐 1:午餐 2:晚餐 3:加餐）
 */
public enum TimeBucketEnum {
    Breakfast ("早餐","500", 0),
    Lunch ("午餐", "600",1),
    Dinner ("晚餐", "450",2),
    Snacks ("加餐", "400",3),

    ;

    // 成员变量
    private String desc;
    private String tips;
    private int code;

    // 构造方法
    private TimeBucketEnum(String desc, String tips,Integer code) {
        this.desc = desc;
        this.tips = tips;
        this.code = code;
    }

    // 普通方法
    public static String getName(Integer code) {
        for (TimeBucketEnum c : TimeBucketEnum.values()) {
            if (c.getCode() == code) {
                return c.desc;
            }
        }
        return null;
    }


    // 普通方法
    public static String getTips(Integer code) {
        for (TimeBucketEnum c : TimeBucketEnum.values()) {
            if (c.getCode() == code) {
                return c.tips;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }


}



