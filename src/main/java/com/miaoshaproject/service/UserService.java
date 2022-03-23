package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

/**
 * @author xyfWritor
 * @create 2022-03-07 18:53
 */
public interface UserService {
    //通过用户id获取用户对象的方法
    UserModel getUserById(Integer id);
    //用于用户的注册
    void register(UserModel userModel) throws BusinessException;

}
