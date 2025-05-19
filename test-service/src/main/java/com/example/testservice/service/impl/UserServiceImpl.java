package com.example.testservice.service.impl;

import com.example.testapi.service.UserService;
import com.example.testapi.vo.MpUserVO;
import com.example.testservice.dao.MpUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MpUserMapper mpUserMapper;


    @Override
    public MpUserVO getVOById(Long id) {
        return mpUserMapper.getVOById(id);
    }
}
