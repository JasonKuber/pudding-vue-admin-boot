package com.pudding.domain.model.convert;

import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.domain.model.vo.LoginUserVO;

public class PuddingUserEntityConvert {


    public static LoginUserVO toVo(PuddingUserEntity entity) {
        LoginUserVO vo = new LoginUserVO();
        vo.setUserName(entity.getUserName());
        vo.setLoginTime(0L);
        return vo;
    }

}
