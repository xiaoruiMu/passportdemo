package com.example.passportdemo.mapper;

import com.example.passportdemo.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface UserMapper {

//List<UserEntity> getAll();

//UserEntity getOne();

@Insert("insert into t_user (name)values(#{name})")
void insert(String name);

}
