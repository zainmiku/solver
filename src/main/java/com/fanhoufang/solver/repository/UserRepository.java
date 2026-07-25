package com.fanhoufang.solver.repository;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanhoufang.solver.entity.po.User;
import com.fanhoufang.solver.mapper.UserMapper;

@Repository
public class UserRepository extends ServiceImpl<UserMapper,User> {

}
