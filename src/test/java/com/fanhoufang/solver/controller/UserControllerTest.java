package com.fanhoufang.solver.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fanhoufang.solver.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;
    @Autowired
    private UserMapper userMapper;

    @Test
    void testErr() {

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        for (int i = 0; i < 10; i++) {
            int FinalI = i;
            executor.submit(() -> {
                log.info("Task " + FinalI + " 在虚拟线程上运行");
                log.info("Task " + FinalI + " 虚拟线程工作完成。");
            });
        }

    }

    @Test
    void testTest() {
        userController.test();
    }

    @Test
    void testGet() {
        userController.get();
    }
}
