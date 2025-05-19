package com.example.testservice.controller.login;

import com.example.testapi.service.UserService;
import com.example.testapi.vo.MpUserVO;
import com.example.testcommon.commom.eventListener.TwoEvent;
import com.example.testcommon.entity.Result;
import com.example.testservice.annotation.LogAnnotation;
import com.example.testservice.controller.BaseController;
import com.example.testservice.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chenkangwen
 * @CreateTime: 2024-12-18  16:52
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RequestMapping(value = "/user")
@RestController
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @LogAnnotation
    @RequestMapping("/login/{id}")
    public Result login(@PathVariable("id") Long id) {
        log.info("请求参数：{}", id);
        Result<MpUserVO> result = new Result<>();
        MpUserVO mpUserVO = userService.getVOById(id);
        result.setData(mpUserVO);
        return result;
    }


    @RequestMapping(value = "/login/event", method = RequestMethod.POST)
    public void event() {
        TwoEvent event1 = new TwoEvent();
        event1.setName("twoEvent");
        SpringContextUtils.getApplicationContext().publishEvent(event1);
    }
}
