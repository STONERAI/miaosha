package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EumBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;



/**
 * @author xyfWritor
 * @create 2022-03-07 18:48
 */
@RestController("user")
@RequestMapping("/user")
@CrossOrigin //解决跨域请求的注解
public class UserController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest; //这里引用的是多例模式，可以应用多个请求

    //用户注册模块
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType register(@RequestParam(name="telephone") String telephone,
                                   @RequestParam(name="otpCode") String otpCode,
                                   @RequestParam(name="name") String name,
                                   @RequestParam(name="gender")Byte gender,
                                   @RequestParam(name="age") Integer age,
                                   @RequestParam(name="password")String password) throws BusinessException {
        //验证手机号和对应的otpcode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        //比较两个字符串是否相等
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EumBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");

        }
        //两个验证码相同，进入用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(MD5Encoder.encode(password.getBytes()));
        userService.register(userModel);

        return CommonReturnType.create(null);
    }
    //用户获取opt短信接口
    @RequestMapping(value="/getotp",method = {RequestMethod.POST},consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType getOtp(@RequestParam(name="telephone")String telephone){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(89999);
        randomInt += 10000; //此时随机数的范围是10000-99999
        String otpCode = String.valueOf(randomInt);//转化为一个字符串的方法。
        //将OTP验证码同对应用户手机关联,使用httpsession的方式绑定他的手机号与OTPCODE
        httpServletRequest.getSession().setAttribute(telephone,otpCode);

        //将OTP验证码通过短信通道发给用户,省略
        System.out.println("telephone = " + telephone + ",otpCode = " + otpCode);
        return CommonReturnType.create(null);
    }
    /**
     * 用于前端页面捕获异常，返回一个更好的页面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/get")
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service服务获取对应的id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
        if(userModel == null){
            //userModel.setEncrptPassword("123");//直接抛出空指针异常
            throw new BusinessException(EumBusinessError.USER_NOT_EXIST);
        }
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }
    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }


}
