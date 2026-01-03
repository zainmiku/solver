package com.fanhoufang.solver.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fanhoufang.solver.entity.po.ComputeServer;

@Mapper
public interface ServerMapper extends BaseMapper<ComputeServer> {

}
