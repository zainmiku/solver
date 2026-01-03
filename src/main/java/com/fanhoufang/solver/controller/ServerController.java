package com.fanhoufang.solver.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fanhoufang.solver.entity.po.ComputeServer;

import com.fanhoufang.solver.repository.SreverRepository;

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
@RequestMapping("/server")
@RequiredArgsConstructor // 为所有final字段生成构造器注入
public class ServerController {

    private final SreverRepository sRepository;

    @GetMapping("get")
    public List<ComputeServer> get() {
        log.info("info get all compute servers");
        log.debug("debug get all compute servers");
        return sRepository.list();
    }

    @PostMapping("add")
    public boolean add(@RequestBody ComputeServer server) {
        server.setId(null);
        return sRepository.save(server);
    }
}
