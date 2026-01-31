package com.fanhoufang.solver.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fanhoufang.solver.common.constant.ReturnCode;
import com.fanhoufang.solver.common.exception.BusinessException;
import com.fanhoufang.solver.entity.po.DateInfo;
import com.fanhoufang.solver.entity.po.User;
import com.fanhoufang.solver.remoteApi.DateRepository;
import com.fanhoufang.solver.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author fan
 * @since 2023-02-11
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor // 为所有final字段生成构造器注入
public class UserController {

    private final UserRepository userRepository;
    private final DateRepository dateRepository;

    @GetMapping(value = "test")
    public String test() {
        String rString = "test ok!";
        User user = new User();
        user.setId(111L);
        user.setUsername(rString);
        log.info(user.toString());
        // 序列化
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(new FileOutputStream("sysUser.ser"));
            out.writeObject(user);
            out.close();
            // 反序列化
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("sysUser.ser"));
            User auser = (User) in.readObject();
            log.info(auser.toString());
            in.close();
        } catch (Exception e) {
        }

        return rString;
    }

    @GetMapping("err")
    public String err() {
        throw new BusinessException(ReturnCode.CD999);
    }

    @GetMapping("get")
    public Long get() {
        Long selectCount = userRepository.count();
        // userRepository.lambdaQuery().eq(s -> s.getIsActive(), 1).list();
        return selectCount;
    }

        @GetMapping("getDateInfo")
    public List<DateInfo> getDateInfo(String date) {
        return dateRepository.getDateInfo(date);
    }
}
