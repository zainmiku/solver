package com.fanhoufang.solver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fanhoufang.solver.entity.po.ComputeServer;

@Mapper
public interface ServerMapper extends BaseMapper<ComputeServer> {

    @Select("SELECT * FROM compute_servers WHERE gpu_count >= #{gpuCount}")
    Cursor<ComputeServer> getCursorMapper(Long gpuCount);

}
