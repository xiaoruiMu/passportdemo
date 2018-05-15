package com.example.passportdemo.web;

import com.example.passportdemo.mapper.UserMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;


@Controller
@RequestMapping("/hello")
public class HelloController {
    @Value("${author.name}")
    private String authorName;
    @Autowired
    private Environment env;

    @Resource
    private UserMapper userMapper;


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
    @RequestMapping(value="/addUser/{userName}",  method= RequestMethod.POST)
    public void addUser(@PathVariable String userName){
        userMapper.insert(userName);
    }
}
