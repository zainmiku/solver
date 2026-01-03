package com.fanhoufang.solver.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fanhoufang.solver.entity.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
