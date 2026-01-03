package com.fanhoufang.solver.entity.po;

import lombok.Data;

@Data
public class DateInfo {


    /**
     * 日期（如2019-05-01）
     */
    private String date;

    /**
     * 年份（如2019）
     */
    private Integer year;
    
    /**
     * 月份（如5）
     */
    private Integer month;
    
    /**
     * 日期中的天（如1）
     */
    private Integer day;
    
    /**
     * 0普通工作日
     * 1周末双休日
     * 2需要补班的工作日
     * 3法定节假日
     */
    private Integer status;
}
