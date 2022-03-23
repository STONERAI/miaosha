package com.miaoshaproject.controller;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xyf
 * @create 2022-03-13-15:56
 */
@Controller
public class InsertUser {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserService userService;
    @RequestMapping("/insert")
    public String insertUser() throws BusinessException {
        UserModel userDO = new UserModel();
        userDO.setAge(23);
        userDO.setName("lucy");
        userDO.setGender((byte)1);
        userDO.setTelephone("12345");
        userDO.setEncrptPassword("123456");
        userDO.setThirdPartyId("bywechat");
        userService.register(userDO);
        return null;
    }
}
