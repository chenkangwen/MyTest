package com.example.testservice.dao;


import com.example.testapi.dto.MpUserDTO;
import com.example.testapi.vo.MpUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 融电小程序用户表数据处理Mapper接口
 *
 * @author chenkangwen
 * @version v9.13.5
 * @date 2024-09-29 18:13:39
 */
@Mapper
public interface MpUserMapper {


    /**
     * @description: 根据id查询小程序用户信息
     * @author: chenkangwen
     * @date: 2024/10/8
     * @param: [openId]
     */
    MpUserVO getVOById(@Param("id") Long id);


    /**
     * @description: 根据用户电话号码查询小程序用户信息
     * @author: chenkangwen
     * @date: 2024/10/8
     * @param: [phone]
     */
    MpUserVO getVOByPhone(@Param("phone") String phone);


    /**
     * @description:
     * @author: chenkangwen
     * @date: 2025/5/20
     * @param: [mpUserDTO]
     */
    void insertMpUserDTO(MpUserDTO mpUserDTO);

}
