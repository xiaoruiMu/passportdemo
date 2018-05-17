package com.example.passportdemo.web;

import com.example.passportdemo.entity.UserEntity;
import com.example.passportdemo.mapper.UserMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/hello")
public class HelloController {
    @Value("${author.name}")
    private String authorName;
    @Autowired
    private Environment env;

    @Resource
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String USER_REDIS_KEY="user_redis_key";

    @ApiOperation(value="測試01", notes="测试01")
  @RequestMapping(value={"hello1"},method= RequestMethod.GET)
    public String hello(){
        System.out.println("hello 请求");
        return  "/index";
    }


    @ApiOperation(value="查看配置文件", notes="测试02")
    @RequestMapping(value="/getProperties", method= RequestMethod.GET)
    public String getProperties(){
        return "authorName is "+authorName;
    }


    @ApiOperation(value="查看env配置文件", notes="测试03")
    @RequestMapping(value="/getEnvProperties", method= RequestMethod.GET)
    public String getEnvProperties(){
        return "authorName is "+env.getProperty("author.name");
    }
    @ApiOperation(value="新增用户", notes="测试04")
    @RequestMapping(value="/addUser",  method= RequestMethod.POST)
    @ResponseBody
    public void addUser( String userName){
        System.out.println(userName);
        userMapper.insert(userName);
    }


    @ApiOperation(value="查看用户", notes="测试05")
    @RequestMapping(value="/queryUser",  method= RequestMethod.POST)
    @ResponseBody
    public String queryUser( String userName){
        String userInfo="没查到此人信息:"+userName;
        UserEntity user=userMapper.queryUser(userName);
        if(null !=user){
            userInfo=user.toString();
        }
        System.out.println(userInfo);
        return  userInfo;
    }



    @ApiOperation(value="查看所有用户", notes="测试06")
    @RequestMapping(value="/queryAllUser",  method= RequestMethod.GET)
    @ResponseBody
    public String queryAllUser() {
        String userInfo = "没查用户信息。";
        try {
            if (stringRedisTemplate.hasKey(USER_REDIS_KEY)) {
                userInfo = "从redis缓存查到数据：" + stringRedisTemplate.opsForValue().get(USER_REDIS_KEY);
                System.out.println("走缓存");
            } else {
                List<UserEntity> userList = userMapper.queryAllUser();
                if (!CollectionUtils.isEmpty(userList)) {
                    userInfo = userList.toString();
                    stringRedisTemplate.opsForValue().set(USER_REDIS_KEY, userInfo,60 , TimeUnit.SECONDS);
                }
                System.out.println("缓存里没有数据");
            }
        } catch (Exception e) {
            userInfo = "啊哦，出现异常啦//n" + e.getMessage();
            stringRedisTemplate.delete(USER_REDIS_KEY);
        }
        System.out.println(userInfo);
        return userInfo;
    }
}
