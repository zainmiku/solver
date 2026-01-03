package com.fanhoufang.solver.repository;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanhoufang.solver.entity.po.ComputeServer;
import com.fanhoufang.solver.mapper.ServerMapper;

@Repository
public class SreverRepository extends ServiceImpl<ServerMapper,ComputeServer> {

}