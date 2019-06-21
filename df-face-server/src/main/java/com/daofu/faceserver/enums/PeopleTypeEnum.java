package com.daofu.faceserver.enums;

/**
 * @author lichuang
 * @description
 * @date 2019-01-29 10:52
 */
public enum PeopleTypeEnum {
    /**
     * 陌生人 0
     */
    STRANGER,
    /**
     * 员工 1
     */
    STAFF,
    /**
     * 会员 2
     */
    VIPUSER,
    /**
     * 黑名单 3
     */
    BLACKLIST,
    /**
     * 会员黑名单 4
     */
    VIPUSER_BLACKLIST,
    /**
     * 回头客 5
     */
    RETURNER_NOTIFY;

    public static int getType(Integer type){
        if(type == null){
            return STRANGER.ordinal();
        }
        for(PeopleTypeEnum enums : PeopleTypeEnum.values()){
            if(enums.ordinal() == type){
                return enums.ordinal();
            }
        }
        return STRANGER.ordinal();
    }
}
