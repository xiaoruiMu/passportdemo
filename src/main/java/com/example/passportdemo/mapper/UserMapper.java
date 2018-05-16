package com.example.passportdemo.mapper;

import com.example.passportdemo.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

//List<UserEntity> getAll();

//UserEntity getOne();

@Insert("insert into t_user (name)values(#{name})")
void insert(String name);
@Select("select * from  t_user where name = #{name}")
UserEntity queryUser(String name);

}
