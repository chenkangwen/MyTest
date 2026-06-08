package com.example.testapi.service;

import com.example.testapi.vo.MpUserVO;


public interface UserService {

    /**
     * 根据 ID 查询用户
     */
    MpUserVO getVOById(Long id);

    /**
     * 根据手机号查询用户
     */
    MpUserVO getVOByPhone(String phone);
}
