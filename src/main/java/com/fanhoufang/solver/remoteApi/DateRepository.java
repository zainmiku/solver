package com.fanhoufang.solver.remoteApi;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.fanhoufang.solver.entity.po.DateInfo;

/**
 * 获取法定节假日信息
 */

@HttpExchange()
public interface DateRepository {   

    /**
     * 获取法定节假日信息
     *
     * @param date 日期
     * @return 节假日信息
     */
    @GetExchange(value = "/holiday")
    List<DateInfo> getDateInfo(@RequestParam("date") String date);

}
